/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.ui.favafx;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fh.ooe.swt6.drive.analytics.sensor.api.Sensor;
import at.fh.ooe.swt6.drive.analytics.ui.favafx.registry.SensorRegistry;
import at.fh.ooe.swt6.drive.analytics.ui.favafx.registry.SensorRegistry.SensorRegistryEvent;
import at.fh.ooe.swt6.drive.analytics.ui.favafx.util.JavaFXUtils;
import at.fh.ooe.swt6.drive.analytics.util.Timer;
import at.fh.ooe.swt6.drive.analytics.util.TimerEvent;
import at.fh.ooe.swt6.drive.analytics.util.TimerListener;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 12, 2016
 */
public class Main extends Observable implements Observer, TimerListener {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	/**
	 * The Javafx-ui possible events.
	 * 
	 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
	 * @date Mar 26, 2016
	 */
	public static enum Event {
		CLOSE
	}

	private Timer timer;
	private Stage stage;
	private VBox root;

	private final SensorRegistry sensorRegistry;
	private static final int DEFAULT_TICK_INTERVAL = 1000;

	public Main() {
		super();
		sensorRegistry = SensorRegistry.getInstance();
	}

	// -- UI methods --
	private Node createSensor(final Sensor sensor) {
		Objects.requireNonNull("sensor", "Cannot create sensor component for null sensor");

		final HBox container = new HBox();
		container.setUserData(sensor.getSensorId());
		container.setSpacing(5.0);
		final Label label = new Label(sensor.getSensorId());
		final ProgressBar bar = new ProgressBar(0);
		bar.setProgress(getFormattedSensorValue(sensor));
		container.getChildren().addAll(label, bar);

		return container;
	}

	// -- Life cycle methods --
	/**
	 * Shuts the javafx application down
	 */
	public void shutdown() {
		if (stage != null) {
			stage.close();
		}
		if (timer != null) {
			timer.stop();
		}

		stage = null;
		root = null;
		timer = null;

		notify(Event.CLOSE);
	}

	/**
	 * Starts the javafx application by opening the main scene.
	 */
	public void start() {
		if (stage != null) {
			shutdown();
		}

		// Create and configure the update timer
		timer = new Timer();
		timer.setInterval(DEFAULT_TICK_INTERVAL);
		timer.addTimerListener(this);

		// Create main scene
		root = new VBox();
		root.setSpacing(5.0);
		final Scene scene = new Scene(root, 500, 500);
		stage = new Stage();
		stage.setScene(scene);
		stage.setOnCloseRequest(event -> shutdown());

		// Place sensors which are not place on scene yet
		final java.util.List<String> placedIds = root.getChildren().parallelStream()
				.map(node -> (String) node.getUserData()).filter(item -> !Objects.isNull(item))
				.collect(Collectors.toList());
		root.getChildren()
				.addAll(sensorRegistry.getSensors().parallelStream()
						.filter(sensor -> !placedIds.contains(sensor.getSensorId())).map(sensor -> createSensor(sensor))
						.collect(Collectors.toList()));
		stage.show();

		timer.start();
	}

	// -- Timer methods --
	@Override
	public void expired(TimerEvent event) {
		try {
			JavaFXUtils.runLater(() -> {
				if (root != null) {
					root.getChildren().parallelStream().filter(node -> !Objects.isNull(node.getUserData()))
							.forEach(node -> {
						final Pane pane = (Pane) node;
						final String sensorId = (String) node.getUserData();
						final ProgressBar bar = (ProgressBar) pane.getChildren().get(1);
						final Sensor sensor = sensorRegistry.getSensor(sensorId);
						bar.setProgress(getFormattedSensorValue(sensor));
					});
				}
			});
		} catch (Exception e) {
			log.error("Timer expired event listener method failed", e);
		}
	}

	// -- Observer methods --
	/**
	 * Attention: Needs to run UI-Thread !!!
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (!(o instanceof SensorRegistry)) {
			log.error("Did you forget to implement %s#update(observable, arg) for '%s'", Main.class.getSimpleName(),
					o.getClass().getName());
		}

		// Cannot register if UI not initialized
		if (stage == null) {
			return;
		}

		// Invoke update method on UI-Thread
		try {
			JavaFXUtils.runAndWait(() -> updateForSensorRegistry((SensorRegistryEvent) arg));
		} catch (Exception e) {
			log.error("Error on update for observable '" + o.getClass().getName() + "'", e);
		}
	}

	/**
	 * Listens to {@link SensorRegistry} notifications.
	 * 
	 * @param model
	 *            the {@link SensorRegistryEvent} which holds the relevant event
	 *            information
	 */
	private void updateForSensorRegistry(SensorRegistryEvent model) {
		switch (model.state) {
		case ADDED:
			root.getChildren().add(createSensor(sensorRegistry.getSensor(model.sensorId)));
			break;
		case MODIFIED:
			final Node toModifyNode = root.getChildren().parallelStream()
					.filter(item -> model.sensorId.equals((String) item.getUserData())).findFirst()
					.orElseGet(() -> null);
			if (toModifyNode != null) {
				toModifyNode.setUserData(sensorRegistry.getSensor(model.sensorId));
			} else {

			}
			break;
		case REMOVED:
			final Node toRemoveNode = root.getChildren().parallelStream()
					.filter(item -> model.sensorId.equals((String) item.getUserData())).findFirst()
					.orElseGet(() -> null);
			if (toRemoveNode != null) {
				root.getChildren().remove(toRemoveNode);
			}
			break;
		default:
			break;
		}
	}

	// -- Private Utils --
	private double getFormattedSensorValue(final Sensor sensor) {
		Objects.requireNonNull(sensor, "Cannot format null data");

		switch (sensor.getDataFormat()) {
		case PERCENT:
			final Double doubleDistance = ByteBuffer.wrap(sensor.getMax()).getDouble()
					- ByteBuffer.wrap(sensor.getMin()).getDouble();
			final Double doubleSensorValue = ByteBuffer.wrap(sensor.getData()).getDouble();
			return (double) (doubleSensorValue / doubleDistance);
		case ABSOLUTE_VALUE_LONG:
			final Long longDistance = ByteBuffer.wrap(sensor.getMax()).getLong()
					- ByteBuffer.wrap(sensor.getMin()).getLong();
			final Long longSensorValue = ByteBuffer.wrap(sensor.getData()).getLong();
			return (double) (longSensorValue / longDistance);
		default:
			throw new IllegalArgumentException("SensorDataFormat '" + sensor.getDataFormat().name() + "' not handled");
		}
	}

	private void notify(final Event event) {
		setChanged();
		notifyObservers(event);
	}
}
