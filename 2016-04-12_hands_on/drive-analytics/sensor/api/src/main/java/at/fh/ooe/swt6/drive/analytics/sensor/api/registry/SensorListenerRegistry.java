/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.sensor.api.registry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fh.ooe.swt6.drive.analytics.sensor.api.Sensor;
import at.fh.ooe.swt6.drive.analytics.sensor.api.SensorListener;

/**
 * This registry handles the registered sensor listeners and notifies them in
 * case of sensor value has changed. The registry get notified via the sensors
 * about value changes.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 18, 2016
 */
public class SensorListenerRegistry implements Observer {

	public final Set<SensorListener> sensorListeners = Collections.synchronizedSet(new HashSet<SensorListener>());

	private static SensorListenerRegistry instance;
	private static final Logger log = LoggerFactory.getLogger(SensorListenerRegistry.class);

	/**
	 * @return the singleton registry instance
	 */
	public static final SensorListenerRegistry getInstance() {
		if (instance == null) {
			instance = new SensorListenerRegistry();
		}
		return instance;
	}

	/**
	 * Registers the given sensor listener
	 * 
	 * @param sensorListener
	 *            the sensor listener to be registered
	 * @throws NullPointerException
	 *             if sensorListener is null
	 */
	public void registerSensorListener(final SensorListener sensorListener) {
		Objects.requireNonNull(sensorListener, "cannot register null sensor listener");

		final boolean added = sensorListeners.add(sensorListener);
		log.info("sensor listener '{}' registered (replaced={})", sensorListener.hashCode(), !added);
	}

	/**
	 * Replaces the given sensorListener.
	 * 
	 * @param sensorListener
	 *            the replacing sensor listener
	 * @throws NullPointerException
	 *             if the sensorListener is null
	 */
	public void replaceSensorListener(final SensorListener sensorListener) {
		Objects.requireNonNull(sensorListener, "cannot replace null sensor listener");

		final boolean replaced = sensorListeners.add(sensorListener);
		log.info("sensor listener '{}' replaced (present={})", sensorListener.hashCode(), replaced);
	}

	/**
	 * Removes a sensor listener from the registry.
	 * 
	 * @param sensorListener
	 *            the sensor listener to be removed
	 * @throws NullPointerException
	 *             if sensorListener is null
	 */
	public void removeSensorListener(final SensorListener sensorListener) {
		Objects.requireNonNull(sensorListener, "cannot remove null listener");

		final boolean removed = sensorListeners.remove(sensorListener);
		log.info("sensor listener '{}' removed (present={})", sensorListener.hashCode(), !removed);
	}

	/**
	 * Gets called from the sensors and will notify all listeners for the
	 * observed sensor.
	 * 
	 * @param observable
	 *            the sensor notifying its listeners
	 * @param argument
	 *            null because not needed
	 */
	public void update(final Observable observable, final Object argument) {
		final Sensor sensor = (Sensor) observable;

		// Notify the listeners
		sensorListeners.forEach(listener -> {
			try {
				listener.valueChanged(sensor);
			} catch (Exception e) {
				log.error("Notification of listener '" + ((listener == null) ? "null" : listener.getClass().getName())
						+ "' failed.", e);
			}
		});
	}
}
