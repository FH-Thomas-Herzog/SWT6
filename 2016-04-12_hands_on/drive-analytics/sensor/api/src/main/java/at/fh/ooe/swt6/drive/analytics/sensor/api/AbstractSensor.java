/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.sensor.api;

import java.util.Observable;

/**
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 18, 2016
 */
public abstract class AbstractSensor extends Observable implements Sensor {

	private static final long serialVersionUID = 1005918513369504753L;

	protected final String id;

	public AbstractSensor(String id) {
		super();
		this.id = id;
	}

	@Override
	public String getSensorId() {
		return id;
	}

	protected void notifyListeners() {
		notifyObservers();
	}
}
