/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.ui.favafx;

import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fh.ooe.swt6.drive.analytics.ui.favafx.registry.SensorRegistry;
import at.fh.ooe.swt6.drive.analytics.ui.favafx.registry.SensorRegistry.EventState;

/**
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 12, 2016
 */
public class Main extends Observable implements Observer {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	
	public Main() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void close() {
		notifyObservers();
	}

	public void start() {

	}

	/**
	 * Attention: Needs to run UI-Thread !!!
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (!(o instanceof SensorRegistry)) {
			log.error("Did you forget to implement %s#update(observable, arg) for '%s'", Main.class.getSimpleName(),
					o.getClass().getName());
		}

		final EventState state = (EventState) arg;
		switch (state) {
		case ADDED:

			break;
		case MODIFIED:

			break;
		case REMOVED:

			break;
		default:
			break;
		}
	}

}
