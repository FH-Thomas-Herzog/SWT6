/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.ui.favafx.osgi.tracker;

import java.util.Objects;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import at.fh.ooe.swt6.drive.analytics.sensor.api.Sensor;
import at.fh.ooe.swt6.drive.analytics.ui.favafx.registry.SensorRegistry;

/**
 * This class represents the sensor service tracker which handles the service
 * tracker events for {@link Sensor} service events.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 12, 2016
 */
public class SensorServiceTracker implements ServiceTrackerCustomizer<Sensor, Sensor> {

	private final BundleContext ctx;
	private final SensorRegistry registry;

	/**
	 * @param ctx
	 *            the BundleContext
	 * @param registry
	 *            the sensor registry to manage sensors with
	 */
	public SensorServiceTracker(final BundleContext ctx, final SensorRegistry registry) {
		super();
		Objects.requireNonNull(ctx, "BundleContext must not be ull");
		Objects.requireNonNull(registry, "The sensor registry must not be null");

		this.ctx = ctx;
		this.registry = registry;
	}

	@Override
	public Sensor addingService(ServiceReference<Sensor> reference) {
		final Sensor sensor = ctx.getService(reference);
		registry.registerSensor(sensor);
		return sensor;
	}

	@Override
	public void modifiedService(ServiceReference<Sensor> reference, Sensor service) {
		registry.replaceSensor(service);
	}

	@Override
	public void removedService(ServiceReference<Sensor> reference, Sensor service) {
		registry.removeSensor(service);
	}
}
