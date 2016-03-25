/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.sensor.impl.distance.osgi.activator;

import java.util.Objects;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fh.ooe.swt6.drive.analytics.sensor.api.Sensor;
import at.fh.ooe.swt6.drive.analytics.sensor.api.registry.SensorListenerRegistry;
import at.fh.ooe.swt6.drive.analytics.sensor.impl.distance.DistanceSensor;

/**
 * This activator registers the distance sensor as a service on bundle start.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 18, 2016
 */
public class DistanceSensorActivator implements BundleActivator {

	private static final Logger log = LoggerFactory.getLogger(DistanceSensorActivator.class);

	private static final String ID = "DISTANCE_SENSOR";

	private DistanceSensor sensor;
	private SensorListenerRegistry registry;

	@Override
	public void start(BundleContext context) throws Exception {
		log.info("Starting {}", context.getBundle().getSymbolicName());
		log.info("Registering sensor '{}' as a osgi service", DistanceSensor.class.getSimpleName());
		log.info("Registering sensor '{}' for listeners", DistanceSensor.class.getSimpleName());

		// Prepare sensor and registry
		sensor = new DistanceSensor(ID);
		registry = SensorListenerRegistry.getInstance();
		sensor.addObserver(registry);

		// Register distance sensor
		context.registerService(Sensor.class, sensor, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		log.info("Starting {}", context.getBundle().getSymbolicName());
		if ((!Objects.isNull(sensor)) && (!Objects.isNull(registry))) {
			sensor.deleteObserver(registry);
		}

		log.info("Stopping sensor-distance bundle");
	}
}
