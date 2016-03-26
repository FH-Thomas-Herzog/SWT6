/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.sensor.api.osgi.tracker;

import java.util.Objects;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import at.fh.ooe.swt6.drive.analytics.sensor.api.SensorListener;
import at.fh.ooe.swt6.drive.analytics.sensor.api.registry.SensorListenerRegistry;

/**
 * This class represents the tracker for sensor listeners.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 18, 2016
 */
public class SensorListenerServiceTracker implements ServiceTrackerCustomizer<SensorListener, SensorListener> {

	private final BundleContext ctx;
	private final SensorListenerRegistry registry;

	/**
	 * @param ctx
	 * @param registry
	 * @throws NullPointerException
	 *             if one of the parameters is null
	 */
	public SensorListenerServiceTracker(BundleContext ctx, SensorListenerRegistry registry) {
		super();
		Objects.requireNonNull(ctx, "BundleContext must not be null");
		Objects.requireNonNull(ctx, "Registry must not be null");

		this.ctx = ctx;
		this.registry = registry;
	}

	@Override
	public SensorListener addingService(ServiceReference<SensorListener> reference) {
		final SensorListener sensorListener = ctx.getService(reference);
		registry.registerSensorListener(sensorListener);
		return sensorListener;
	}

	@Override
	public void modifiedService(ServiceReference<SensorListener> reference, SensorListener service) {
		registry.replaceSensorListener(service);

	}

	@Override
	public void removedService(ServiceReference<SensorListener> reference, SensorListener service) {
		registry.removeSensorListener(service);
	}
}
