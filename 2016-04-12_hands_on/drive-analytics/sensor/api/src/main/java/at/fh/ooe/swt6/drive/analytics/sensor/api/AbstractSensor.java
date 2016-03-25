/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.sensor.api;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Observable;

import at.fh.ooe.swt6.drive.analytics.util.Timer;
import at.fh.ooe.swt6.drive.analytics.util.TimerEvent;
import at.fh.ooe.swt6.drive.analytics.util.TimerListener;

/**
 * This class represents the base class for all sensor implementations. It
 * provides utilities for handling the data type and timer for sensor listener
 * notifications.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 18, 2016
 */
public abstract class AbstractSensor<T extends Number> extends Observable implements Sensor, TimerListener {

	private static final long serialVersionUID = 1005918513369504753L;

	private Timer timer;

	protected final String id;
	protected T distance;
	protected final byte[] MIN = new byte[8];
	protected final byte[] MAX = new byte[8];

	/**
	 * @param id
	 *            the sensor id
	 * @param min
	 *            the min value
	 * @param max
	 *            the max value
	 */
	public AbstractSensor(String id, T min, T max) {
		this(id, min, max, null);
	}

	/**
	 * @param id
	 *            the sensor id
	 * @param min
	 *            the min value
	 * @param max
	 *            the max value
	 * @param interval
	 *            the interval, may be null if no timer is used
	 */
	public AbstractSensor(String id, T min, T max, Integer interval) {
		super();
		this.id = id;

		// check boundaries
		if (!isBoundaryValid(min, max)) {
			throw new IllegalArgumentException("Min overflows max value");
		}
		// Set boundaries on byte[] for instance type
		if (min instanceof Double) {
			ByteBuffer.wrap(MIN).putDouble((Double) min);
			ByteBuffer.wrap(MAX).putDouble((Double) max);
		} else if (min instanceof Long) {
			ByteBuffer.wrap(MIN).putLong((Long) min);
			ByteBuffer.wrap(MAX).putLong((Long) max);
		} else {
			throw new IllegalArgumentException("Only Double and Integer tpe are allowed");
		}

		// If interval is given then the timer shall be activated
		if (!Objects.isNull(interval)) {
			if (interval <= 0) {
				throw new IllegalArgumentException("Interval must not be negativ	");
			}
			timer = new Timer();
			timer.setInterval(interval);
			timer.addTimerListener(this);
			timer.start();
		}
	}

	// -- Private Methods --
	/**
	 * Validates the boundaries if they are valid.
	 * 
	 * @param min
	 *            the min number
	 * @param max
	 *            the max number
	 * @return true if valid, false otherwise (if distance is 0)
	 */
	private boolean isBoundaryValid(T min, T max) {
		Objects.requireNonNull(min);
		Objects.requireNonNull(max);

		if (min instanceof Double) {
			distance = (T) (Double) (((Double) max - (Double) min));
			return ((Double) min).compareTo((Double) max) < 0;
		} else if (min instanceof Long) {
			distance = (T) (Long) (((Long) max - (Long) min));
			return ((Long) min).compareTo((Long) max) < 0;
		} else {
			throw new IllegalArgumentException("Only Double and Long values are supported");
		}
	}

	// -- Timer methods --
	/**
	 * Notifies the observers about a sensor value change.
	 * 
	 * @param event
	 *            the timer event object
	 */
	@Override
	public void expired(TimerEvent event) {
		setChanged();
		notifyObservers();
	}

	// -- Getter and Setter --
	public T getDistance() {
		return distance;
	}

	@Override
	public String getSensorId() {
		return id;
	}

	@Override
	public byte[] getMin() {
		return MIN;
	}

	@Override
	public byte[] getMax() {
		return MAX;
	}
}
