/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.ui.favafx.osgi.activator;

import java.util.Observable;
import java.util.Observer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fh.ooe.swt6.drive.analytics.sensor.api.Sensor;
import at.fh.ooe.swt6.drive.analytics.sensor.api.SensorListener;
import at.fh.ooe.swt6.drive.analytics.ui.favafx.Main;
import at.fh.ooe.swt6.drive.analytics.ui.favafx.osgi.tracker.SensorServiceTracker;
import at.fh.ooe.swt6.drive.analytics.ui.favafx.registry.SensorRegistry;

/**
 * This activator class handles the sensor bundle activation events.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 12, 2016
 */
public class JavaFxUIBundleActivator implements BundleActivator, Observer {

	private Main instance;
	private BundleContext ctx;
	private ServiceTracker<Sensor, Sensor> tracker;

	private final SensorRegistry registry;

	private static final Logger log = LoggerFactory.getLogger(JavaFxUIBundleActivator.class);

	public JavaFxUIBundleActivator() {
		super();
		this.registry = SensorRegistry.getInstance();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		log.info("Starting {}", context.getBundle().getSymbolicName());

		ctx = context;
		instance = new Main();
		// Listen to main events
		instance.addObserver(this);
		// Listen to registry events
		registry.addObserver(instance);

		tracker = new ServiceTracker<>(context, Sensor.class, new SensorServiceTracker(context, registry));
		tracker.open();

		instance.start();

		// Register sensor listener
		context.registerService(SensorListener.class, new SensorListener() {
			@Override
			public void valueChanged(Sensor sensor) {
				log.info("Sensor '{}' value has changed", sensor.hashCode());
			}
		}, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		log.info("Stopping {}", context.getBundle().getSymbolicName());

		if (tracker != null) {
			tracker.close();
		}
		if (instance != null) {
			instance.close();
		}

		instance = null;
		ctx = null;
	}

	/**
	 * Observe Main by shutting down the bundle on Main close.
	 */
	@Override
	public void update(Observable o, Object arg) {
		log.info("Shuting down because '{}' intends so", o.getClass().getName());
		try {
			registry.deleteObserver(instance);
			instance = null;
			ctx.getBundle().stop();
		} catch (Exception e) {
			log.error("Error on bundle stop", e);
		}
	}
}
