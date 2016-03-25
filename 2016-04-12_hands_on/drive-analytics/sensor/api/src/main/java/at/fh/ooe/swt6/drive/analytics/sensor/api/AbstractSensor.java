/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.sensor.api;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Observable;

/**
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 18, 2016
 */
public abstract class AbstractSensor<T extends Number> extends Observable implements Sensor {

	private static final long serialVersionUID = 1005918513369504753L;

	protected final String id;
	protected T distance;
	protected final byte[] MIN = new byte[8];
	protected final byte[] MAX = new byte[8];

	public AbstractSensor(String id, T min, T max) {
		super();
		this.id = id;

		if (!isBoundaryValid(min, max)) {
			throw new IllegalArgumentException("Min overflows max value");
		}
		if (min instanceof Double) {
			ByteBuffer.wrap(MIN).putDouble((Double) min);
			ByteBuffer.wrap(MAX).putDouble((Double) max);
		} else if (min instanceof Long) {
			ByteBuffer.wrap(MIN).putLong((Long) min);
			ByteBuffer.wrap(MAX).putLong((Long) max);
		} else {
			throw new IllegalArgumentException("Only Double and Integer tpe are allowed");
		}
	}

	// -- Private Methods --
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

	protected void notifyListeners() {
		setChanged();
		notifyObservers();
	}
}
