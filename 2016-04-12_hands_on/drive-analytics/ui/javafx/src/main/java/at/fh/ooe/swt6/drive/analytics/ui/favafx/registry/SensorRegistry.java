/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.ui.favafx.registry;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fh.ooe.swt6.drive.analytics.sensor.api.Sensor;

/**
 * This class represents the Sensor registry which handles the registration and
 * removal of an {@link Sensor} type.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 12, 2016
 */
public class SensorRegistry extends Observable {

	public final Map<String, Sensor> sensorMap = new ConcurrentHashMap<>();

	private static SensorRegistry instance;
	private static final Logger log = LoggerFactory.getLogger(SensorRegistry.class);

	/**
	 * Specifies the event state this registry can notify to its observers.
	 * 
	 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
	 * @date Mar 12, 2016
	 */
	public static enum EventState {
		ADDED, MODIFIED, REMOVED;
	}

	private SensorRegistry() {
		super();
	}

	/**
	 * @return the singleton registry instance
	 */
	public static final SensorRegistry getInstance() {
		if (instance == null) {
			instance = new SensorRegistry();
		}
		return instance;
	}

	/**
	 * Registers the given sensor
	 * 
	 * @param sensor
	 *            the sensor to be registered
	 * @throws NullPointerException
	 *             if sensor is null
	 */
	public void registerSensor(final Sensor sensor) {
		Objects.requireNonNull(sensor, "cannot register null sensor");
		final boolean added = sensorMap.putIfAbsent(sensor.getSensorId(), sensor) != null;
		log.info("'{}' registered (replaced={})", sensor.getSensorId(), added);
		if (added) {
			log.warn("You should have used SensorRegistry#replaceSensor(sensor)");
			notifyObservers(EventState.ADDED);
		}
	}

	public void replaceSensor(final Sensor sensor) {
		Objects.requireNonNull(sensor, "cannot register null sensor");
		final boolean replaced = sensorMap.putIfAbsent(sensor.getSensorId(), sensor) != null;
		log.info("'{}' replaced (present={})", sensor.getSensorId(), replaced);
		if (replaced) {
			notifyObservers(EventState.MODIFIED);
		}
	}

	/**
	 * Removes a sensor from the registry
	 * 
	 * @param sensor
	 *            the sensor to be removed
	 * @throws NullPointerException
	 *             if sensor is null
	 */
	public void removeSensor(final Sensor sensor) {
		Objects.requireNonNull(sensor, "cannot remove null sensor");
		final boolean removed = sensorMap.remove(sensor.getSensorId()) != null;
		log.info("'{}' removed (present={})", sensor.getSensorId(), removed);
		if (removed) {
			notifyObservers(EventState.REMOVED);
		}
	}

	/**
	 * @param id
	 *            the sensor id
	 * @return the sensor instance or null if sensor is not registered
	 * @throws NullPointerException
	 *             if id is null
	 */
	public Sensor getSensor(final String id) {
		Objects.requireNonNull(id, "Cannot get sensor for null id");
		return sensorMap.get(id);
	}

	/**
	 * @return a list of the registered sensors
	 */
	public List<Sensor> getSensors() {
		return sensorMap.entrySet().parallelStream().map(entry -> entry.getValue()).collect(Collectors.toList());
	}
}
