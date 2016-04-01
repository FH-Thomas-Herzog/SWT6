/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.sensor.impl.nfc.osgi.activator;

import java.util.Objects;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fh.ooe.swt6.drive.analytics.sensor.api.Sensor;
import at.fh.ooe.swt6.drive.analytics.sensor.impl.nfc.NfcSensor;

/**
 * This activator registers the distance sensor as a service on bundle start.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 18, 2016
 */
public class NfcSensorActivator implements BundleActivator {

	private static final Logger log = LoggerFactory.getLogger(NfcSensorActivator.class);

	private ServiceRegistration<Sensor> registeredSensor;
	private static final String ID = "NFC_SENSOR";

	@Override
	public void start(BundleContext context) throws Exception {
		log.info("Starting bundle: {}", context.getBundle().getSymbolicName());
		log.info("Registering service: {} ", ID);

		registeredSensor = context.registerService(Sensor.class, new NfcSensor(ID), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		log.info("Stopping  bundle: {}", context.getBundle().getSymbolicName());
		
		// unregister service
		if(!Objects.isNull(registeredSensor)) {
			registeredSensor.unregister();
		}
	}
}
