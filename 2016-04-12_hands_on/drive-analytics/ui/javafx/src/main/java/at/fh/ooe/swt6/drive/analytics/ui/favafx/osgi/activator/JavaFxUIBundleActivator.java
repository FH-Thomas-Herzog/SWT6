/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.ui.favafx.osgi.activator;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fh.ooe.swt6.drive.analytics.sensor.api.Sensor;
import at.fh.ooe.swt6.drive.analytics.sensor.api.SensorListener;
import at.fh.ooe.swt6.drive.analytics.ui.favafx.Main;
import at.fh.ooe.swt6.drive.analytics.ui.favafx.Main.Event;
import at.fh.ooe.swt6.drive.analytics.ui.favafx.osgi.tracker.SensorServiceTracker;
import at.fh.ooe.swt6.drive.analytics.ui.favafx.registry.SensorRegistry;
import at.fh.ooe.swt6.drive.analytics.ui.favafx.util.JavaFXUtils;

/**
 * This activator class handles the javafx-ui bundle events.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 12, 2016
 */
public class JavaFxUIBundleActivator implements BundleActivator, Observer {

	public static final class SimpleSensorListener implements SensorListener {
		@Override
		public void valueChanged(Sensor sensor) {
			log.info("Sensor '{}' value has changed", sensor.getSensorId());
		}
	}

	private Main instance;
	private BundleContext ctx;
	private ServiceTracker<Sensor, Sensor> tracker;
	private ServiceRegistration<SensorListener> sensorListenerRegistration;

	private final SensorListener sensorListener;
	private final SensorRegistry registry;

	private static final Logger log = LoggerFactory.getLogger(JavaFxUIBundleActivator.class);

	/**
	 * Gets the registry singleton instance
	 */
	public JavaFxUIBundleActivator() {
		super();
		this.registry = SensorRegistry.getInstance();
		this.sensorListener = new SimpleSensorListener();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		log.info("Starting bundle: {}", context.getBundle().getSymbolicName());

		ctx = context;
		instance = new Main();

		// Initialize toolkit
		JavaFXUtils.initJavaFx();

		// Listen to main events
		instance.addObserver(this);
		// Listen to registry events
		registry.addObserver(instance);

		// Initialize and open the tracker for sensor services
		tracker = new ServiceTracker<>(context, Sensor.class, new SensorServiceTracker(context, registry));
		tracker.open();

		// Start the UI
		JavaFXUtils.runAndWait(() -> instance.start());

		// Register sensor listener
		sensorListenerRegistration = context.registerService(SensorListener.class, sensorListener, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		log.info("Stopping bundle: {}", context.getBundle().getSymbolicName());

		// unget registered sensor listener
		if (sensorListenerRegistration != null) {
			sensorListenerRegistration.unregister();
		}

		// Close tracker
		if (tracker != null) {
			tracker.close();
		}

		// Stop UI
		stopUI();

		// Release context reference
		ctx = null;
	}

	/**
	 * Observe Main by shutting down the bundle on Main close.
	 */
	@Override
	public void update(Observable o, Object arg) {
		Objects.requireNonNull(o, "Cannot listen to event for null observable");
		Objects.requireNonNull(arg, "Cannot observe if null argumnets are given");

		final Event event = (Event) arg;
		switch (event) {
		case CLOSE:
			log.info("Stopping bundle: '{}', because '{}' intends so", ctx.getBundle().getSymbolicName(),
					o.getClass().getName());
			try {
				// Already shutdown
				instance = null;
				ctx.getBundle().stop();
			} catch (Exception e) {
				log.error("Error on bundle stop via UI", e);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknonw Event '" + event.name() + "'");
		}
	}

	// -- Private helpers --
	/**
	 * Stops the UI and unregisters the observers on the sensor registry and UI.
	 */
	private void stopUI() {
		if (instance != null) {
			try {
				registry.deleteObserver(instance);
				instance.deleteObserver(this);
				JavaFXUtils.runAndWait(() -> {
					instance.shutdown();
				});
			} catch (Exception e) {
				log.error("Stopping of UI threw error", e);
			}
		}
	}
}
