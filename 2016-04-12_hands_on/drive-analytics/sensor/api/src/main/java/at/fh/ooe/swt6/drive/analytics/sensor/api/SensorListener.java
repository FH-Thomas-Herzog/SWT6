/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.sensor.api;

/**
 * This interface specifies a sensor listener which gets notified if the sensors
 * value has changed.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 18, 2016
 */
public interface SensorListener {

	/**
	 * Gets called by the sensor if the sensors value has changed
	 * 
	 * @param sensor
	 *            the sensor which value has changed
	 */
	void valueChanged(Sensor sensor);
}
