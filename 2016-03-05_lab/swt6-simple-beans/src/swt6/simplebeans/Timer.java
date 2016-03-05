package swt6.simplebeans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import swt6.beans.TimerEvent;
import swt6.beans.TimerListener;

public class Timer {

	private int numTicks = 1;
	private final AtomicInteger tickInterval = new AtomicInteger(1000);
	private final AtomicInteger tickCount = new AtomicInteger(0);
	private final AtomicBoolean stopTimer = new AtomicBoolean(Boolean.FALSE);
	private final AtomicReference<Thread> tickerThread = new AtomicReference<>(null);

	private final Vector<TimerListener> listeners = new Vector<>();

	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public Timer() {
		super();
	}

	public boolean isRunning() {
		return tickerThread.get() != null;
	}

	public void stop() {
		stopTimer.set(Boolean.TRUE);
	}

	public void reset() {
		if (isRunning()) {
			throw new IllegalStateException("Cannot reset while timer is running");
		}

		tickCount.set(0);
	}

	// ############################################
	// PropertyChangeEvent
	// ############################################
	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void firePropertyChagneListener(String propertyName, Object oldValue, Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	// ############################################
	// EventListener Methods
	// ############################################
	public void addTimerListener(final TimerListener listener) {
		this.listeners.add(listener);
	}

	public void removeTimerListener(final TimerListener listener) {
		this.listeners.remove(listener);
	}

	public void fireEvent(TimerEvent evt) {
		((Vector<TimerListener>) listeners.clone()).forEach(item -> {
			item.expired(evt);
		});
	}

	public void start() {
		if (isRunning()) {
			throw new IllegalStateException("Cannot start an already started Thread");
		}

		final int ticks = numTicks;

		tickerThread.set(new Thread(() -> {
			tickCount.set(0);

			while ((!stopTimer.get()) && (tickCount.get() < ticks)) {
				try {
					Thread.sleep(tickInterval.get());
				} catch (Exception e) {
					// Ignored for now
				}

				if (!stopTimer.get()) {
					fireEvent(new TimerEvent(this, tickCount.incrementAndGet(), ticks));
				}
			}

			stopTimer.set(Boolean.FALSE);
			tickerThread.set(null);
		}));

		tickerThread.get().start();
	}

	// ############################################
	// Getter and Setter
	// ############################################
	public Integer getTickCount() {
		return tickCount.get();
	}

	public Integer getInterval() {
		return tickInterval.get();
	}

	public void setInterval(int value) {
		final int oldValue = tickInterval.get();
		if (oldValue != value) {
			tickInterval.set(value);
			firePropertyChagneListener("interval", oldValue, value);
		}
	}

	public int getNumTicks() {
		return numTicks;
	}

	public void setNumTicks(int numTicks) {
		final int oldValue = this.numTicks;
		this.numTicks = numTicks;
		// if (oldValue != numTicks) {
			firePropertyChagneListener("numTicks", oldValue, numTicks);
		// }
	}
}
