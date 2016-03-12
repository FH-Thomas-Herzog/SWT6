/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.sensor.impl.nfc;

import java.nio.ByteBuffer;

import at.fh.ooe.swt6.drive.analytics.sensor.api.Sensor;

/**
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 12, 2016
 */
public class NfcSensor implements Sensor {

	private static final long serialVersionUID = 2542405301669418148L;

	private final String id;

	public NfcSensor(String id) {
		super();
		this.id = id;
	}

	@Override
	public String getSensorId() {
		return id;
	}

	@Override
	public byte[] getData() {
		final byte[] data = new byte[8];
		ByteBuffer.wrap(data).putLong(1);
		return data;
	}

	@Override
	public SensorDataFormat getDataFormat() {
		return SensorDataFormat.ABSOLUTE_VALUE_LONG;
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
		NfcSensor other = (NfcSensor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
