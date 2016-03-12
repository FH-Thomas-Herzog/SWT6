/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.sensor.impl.distance;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Random;

import at.fh.ooe.swt6.drive.analytics.sensor.api.Sensor;

/**
 * This class represents a distance sensor.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 12, 2016
 */
public class DistanceSensor implements Sensor {

	private static final long serialVersionUID = 2542405301669418148L;

	private final String id;

	private static final Random RANDOM = new Random();

	/**
	 * @param id
	 *            the sensors id
	 */
	public DistanceSensor(String id) {
		super();
		Objects.requireNonNull(id, "Sensore must have an id set");
		this.id = id;
	}

	@Override
	public String getSensorId() {
		return id;
	}

	@Override
	public byte[] getData() {
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(Double.valueOf((RANDOM.nextDouble() * (RANDOM.nextInt(10) + 1))));
		return bytes;
	}

	@Override
	public SensorDataFormat getDataFormat() {
		return SensorDataFormat.PERCENT;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistanceSensor other = (DistanceSensor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
