/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.ui.favafx;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 18, 2016
 */
public class JFXMain {

	private Stage stage;
	private ToolBar toolBar;
	private VBox rootPane;
	private PaintCanvas canvas;
	private Shape currentShape;
	private Collection<Shape> shapes = new ArrayList<>();

	private class PaintCanvas extends Canvas {

		private GraphicsContext gc;

		public PaintCanvas() {
			gc = this.getGraphicsContext2D();
			gc.setLineWidth(2);

			// register for resize events
			this.widthProperty().addListener(event -> redraw());
			this.heightProperty().addListener(event -> redraw());
		}

		public void redraw() {
			gc.clearRect(0, 0, this.getWidth(), this.getHeight());
		}

	}

	public JFXMain() {
		toolBar = new ToolBar();
		canvas = new PaintCanvas();
		rootPane = new VBox(toolBar, canvas);

		canvas.widthProperty().bind(rootPane.widthProperty());
		canvas.heightProperty().bind(rootPane.heightProperty());

	}

	public void show() {
		if (stage == null) {
			stage = new Stage();
			stage.setScene(new Scene(rootPane, 500, 500));
			stage.setMinWidth(250);
			stage.setMinHeight(250);
			stage.setTitle("Simple Paint Application (non-modular)");
		}
		stage.show();
	}

	public void close() {
		if (stage != null)
			stage.close();
	}
}
