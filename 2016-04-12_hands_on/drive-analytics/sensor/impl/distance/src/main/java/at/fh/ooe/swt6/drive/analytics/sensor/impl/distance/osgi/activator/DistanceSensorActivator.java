/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.sensor.impl.distance.osgi.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fh.ooe.swt6.drive.analytics.sensor.api.Sensor;
import at.fh.ooe.swt6.drive.analytics.sensor.impl.distance.DistanceSensor;

/**
 * This activator registers the distance sensor as a service on bundle start.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 18, 2016
 */
public class DistanceSensorActivator implements BundleActivator {

	private static final Logger log = LoggerFactory.getLogger(DistanceSensorActivator.class);

	@Override
	public void start(BundleContext context) throws Exception {
		log.info("Starting sensor-distance bundle");
		log.info("Registering service " + DistanceSensor.class.getName());
		context.registerService(Sensor.class, new DistanceSensor("SENSOR_DISTANCE"), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		log.info("Stopping sensor-distance bundle");
	}

}
