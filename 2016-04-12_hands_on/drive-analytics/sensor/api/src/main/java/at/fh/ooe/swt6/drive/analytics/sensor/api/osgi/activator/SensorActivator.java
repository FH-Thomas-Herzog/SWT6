/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.sensor.api.osgi.activator;

import java.util.Objects;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fh.ooe.swt6.drive.analytics.sensor.api.SensorListener;
import at.fh.ooe.swt6.drive.analytics.sensor.api.osgi.tracker.SensorListenerServiceTracker;
import at.fh.ooe.swt6.drive.analytics.sensor.api.registry.SensorListenerRegistry;

/**
 * This activator registers the distance sensor as a service on bundle start.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 18, 2016
 */
public class SensorActivator implements BundleActivator {

	private static final Logger log = LoggerFactory.getLogger(SensorActivator.class);

	private ServiceTracker<SensorListener, SensorListener> tracker;
	private SensorListenerRegistry registry = SensorListenerRegistry.getInstance();

	@Override
	public void start(BundleContext context) throws Exception {
		log.info("Starting {}", context.getBundle().getSymbolicName());

		tracker = new ServiceTracker<>(context, SensorListener.class,
				new SensorListenerServiceTracker(context, registry));
		tracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (!Objects.isNull(tracker)) {
			tracker.close();
		}

		log.info("Stopping {} ", context.getBundle().getSymbolicName());
	}
}
