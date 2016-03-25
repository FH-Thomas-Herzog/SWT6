package at.fh.ooe.swt6.drive.analytics.ui.favafx.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Timer class which allows registration of event listeners.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 25, 2016
 */
public class Timer {

	private final AtomicInteger tickInterval = new AtomicInteger(100);
	private final AtomicBoolean stopTimer = new AtomicBoolean(false);
	private final AtomicReference<Thread> tickerThread = new AtomicReference<Thread>(null);

	private final Vector<TimerListener> listener = new Vector<>();

	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public Timer() {
	}

	public boolean isRunning() {
		return tickerThread.get() != null;
	}

	public void stop() {
		stopTimer.set(true);
	}

	public void reset() {
		if (isRunning()) {
			throw new IllegalStateException("Cannot reset while timer is running");
		}
	}

	/**
	 * @return the tick interval in milliseconds.
	 */
	public int getInterval() {
		return tickInterval.get();
	}

	/**
	 * @param interval
	 *            the tick interval in milliseconds.
	 */
	public void setInterval(int interval) {
		int oldValue = tickInterval.get();
		if (interval != oldValue) {
			tickInterval.set(interval);
			firePropertyChange("interval", oldValue, interval);
		}
	}

	public void addTimerListener(TimerListener listener) {
		this.listener.add(listener);
	}

	public void removeTimerListener(TimerListener listener) {
		this.listener.remove(listener);
	}

	protected void fireEvent(TimerEvent e) {
		for (TimerListener l : (Vector<TimerListener>) listener.clone()) {
			l.expired(e);
		}
	}

	public void start() {
		if (isRunning()) {
			throw new IllegalStateException("Cannot start: timer is already running");
		}

		tickerThread.set(new Thread(() -> {
			while (!stopTimer.get()) {
				try {
					Thread.sleep(tickInterval.get());
				} catch (Exception e) {
				}

				if (!stopTimer.get()) {
					fireEvent(new TimerEvent(this));
				}

			}

			stopTimer.set(false);
			tickerThread.set(null);

		}));

		tickerThread.get().start();

	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener(l);
	}

	private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

}
