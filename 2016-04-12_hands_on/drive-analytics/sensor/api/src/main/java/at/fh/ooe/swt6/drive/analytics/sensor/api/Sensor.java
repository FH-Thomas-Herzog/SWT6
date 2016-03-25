/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.sensor.api;

import java.io.Serializable;

/**
 * This class specifies a sensor.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 12, 2016
 */
public interface Sensor extends Serializable {

	/**
	 * Enumeration which specifies the data format a sensor provides.
	 * 
	 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
	 * @date Mar 12, 2016
	 */
	public static enum SensorDataFormat {
		PERCENT, ABSOLUTE_VALUE_LONG;
	}

	/**
	 * @return the unique sensor id
	 */
	String getSensorId();

	/**
	 * @return the byte data representing the sensor result
	 */
	byte[] getData();

	/**
	 * @return the sensor used data format.
	 * @see SensorDataFormat
	 */
	SensorDataFormat getDataFormat();

	/**
	 * @return the minimum value of this sensor
	 */
	byte[] getMin();

	/**
	 * @return the maximum value of this sensor
	 */
	byte[] getMax();
}
