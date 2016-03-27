/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.ui.favafx;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
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
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This is the main class for the javafx ui.
 * 
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

	private final Timer timer;
	private Stage stage;
	private VBox root;

	private final SensorRegistry sensorRegistry;
	private static final int DEFAULT_TICK_INTERVAL = 200;

	/**
	 * Initializes the ui.
	 */
	public Main() {
		super();
		sensorRegistry = SensorRegistry.getInstance();
		timer = new Timer();
		timer.setInterval(DEFAULT_TICK_INTERVAL);
		timer.addTimerListener(this);
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

		notify(Event.CLOSE);
	}

	/**
	 * Starts the javafx application by creating the ui and opening the main
	 * scene.
	 */
	public void start() {
		if (stage != null) {
			shutdown();
		}

		// All container
		VBox container = new VBox();
		container.setSpacing(5.0);

		root = new VBox();
		root.setSpacing(5.0);

		// Main scene
		final Scene scene = new Scene(container, 500, 500);

		// Main stage
		stage = new Stage();
		stage.setScene(scene);
		stage.setHeight(400.0);
		stage.setWidth(400.0);
		// shutdown on close
		stage.setOnCloseRequest(event -> shutdown());

		// Place sensors which are not place on scene yet
		final java.util.List<String> placedIds = root.getChildren().parallelStream()
				.map(node -> (String) node.getUserData()).filter(item -> !Objects.isNull(item))
				.collect(Collectors.toList());
		root.getChildren()
				.addAll(sensorRegistry.getSensors().parallelStream()
						.filter(sensor -> !placedIds.contains(sensor.getSensorId())).map(sensor -> createSensor(sensor))
						.collect(Collectors.toList()));

		// Add control buttons
		container.getChildren().addAll(createButtons(), root);

		stage.show();

		timer.start();
	}

	// -- Timer methods --
	/**
	 * Handles the timer expired even which indicates to pool data from the
	 * sensors.
	 * 
	 * @param event
	 *            the TimerEvent object
	 */
	@Override
	public void expired(TimerEvent event) {
		try {
			// Run later and do not wait for the execution
			JavaFXUtils.runLater(() -> {
				// Could be null due to later execution
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
	/**
	 * Gets the formatted value from the sensor for the ui.
	 * 
	 * @param sensor
	 *            the sensor to get data from
	 * @return the formatted sensor data represented by an double
	 */
	private double getFormattedSensorValue(final Sensor sensor) {
		// Sensor could be null due to later execution
		if (Objects.isNull(sensor)) {
			return 0.0;
		}

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

	/**
	 * Notifies all observers about the fired event
	 * 
	 * @param event
	 *            the event representing the event type
	 */
	private void notify(final Event event) {
		setChanged();
		notifyObservers(event);
	}

	// -- UI methods --
	/**
	 * Creates the sensor ui component.
	 * 
	 * @param sensor
	 *            the sensor to display on the ui
	 * @return the sensor ui component
	 */
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

	/**
	 * Creates the button controls for the controlling of the pooling
	 * 
	 * @return the {@link HBox} holding the button controls
	 */
	private HBox createButtons() {
		final HBox container = new HBox();
		container.setSpacing(5.0);

		// Display current interval
		final Label label = new Label();
		label.setText(timer.getInterval() + " ms");
		label.setMinWidth(100.0);

		final Button plusBtn = new Button("+ (10ms)");
		final Button minusBtn = new Button("- (10ms)");
		final Button disableBtn = new Button("stop");

		// Increase pool interval
		plusBtn.setOnMouseClicked(event -> {
			timer.setInterval(timer.getInterval() + 10);
			label.setText(timer.getInterval() + " ms");
			minusBtn.setDisable(Boolean.FALSE);
		});

		// Decrease pool interval
		minusBtn.setOnMouseClicked(event -> {
			if (timer.getInterval() > 10) {
				timer.setInterval(timer.getInterval() - 10);
				minusBtn.setDisable(Boolean.FALSE);
				label.setText(timer.getInterval() + " ms");
			} else {
				minusBtn.setDisable(Boolean.TRUE);
			}
		});

		// Disable pooling
		disableBtn.setOnMouseClicked(event -> {
			try {
				if (timer.isRunning()) {
					disableBtn.setText("start");
					timer.stop();
					label.setText("0 ms");
				} else {
					disableBtn.setText("stop");
					timer.start();
					label.setText(timer.getInterval() + " ms");
				}
			} catch (Exception e) {
				log.error("Could not start or stop the timer via button", e);
			}
		});

		container.getChildren().addAll(label, plusBtn, minusBtn, disableBtn);

		return container;
	}

}
