package swt6.osgi.paint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import swt6.osgi.shape.Shape;
import swt6.osgi.shape.ShapeFactory;

public class PaintWindow {

  private Stage                           stage;
  private ToolBar                         toolBar;
  private VBox                            rootPane;
  private PaintCanvas                     canvas;
  private List<EventHandler<WindowEvent>> onCloseHandlers = new ArrayList<>();
  private Shape                           currentShape;
  private Collection<Shape>               shapes          = new ArrayList<>();
  private Collection<ShapeFactory>        shapeFactories  = new ArrayList<ShapeFactory>();
  private ShapeFactory                    currentShapeFactory;

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
      for (Shape s : shapes)
        s.draw(gc);
    }

  }

  public PaintWindow() {
    toolBar = new ToolBar();
    canvas = new PaintCanvas();
    rootPane = new VBox(toolBar, canvas);

    canvas.setOnMousePressed(evt -> mousePressed(evt));
    canvas.setOnMouseReleased(evt -> mouseReleased(evt));
    canvas.setOnMouseDragged(evt -> mouseDragged(evt));
    canvas.widthProperty().bind(rootPane.widthProperty());
    canvas.heightProperty().bind(rootPane.heightProperty());

  }

  public void show() {
    if (stage == null) {
      stage = new Stage();
      stage.setScene(new Scene(rootPane, 500, 500));
      stage.setMinWidth(250);
      stage.setMinHeight(250);
      stage.setOnCloseRequest(evt -> {
        onCloseHandlers.forEach(h -> h.handle(evt));
      });
      stage.setTitle("Simple Paint Application (non-modular)");
    }
    stage.show();
  }

  public void close() {
    if (stage != null) stage.close();
  }

  public void addOnCloseEventHandler(EventHandler<WindowEvent> evt) {
    onCloseHandlers.add(evt);
  }

  public void removeOnCloseEventHandler(EventHandler<WindowEvent> evt) {
    onCloseHandlers.remove(evt);
  }

  public void addShapeFactory(ShapeFactory sf) {
    // multiple shape factories of the same type are not allowed.
    if (getShapeFactoryByName(sf.getShapeType()) != null) return;

    // update shape factory collection
    shapeFactories.add(sf);
    if (currentShapeFactory == null) setCurrentShapeFactory(sf);

    // add toolbar button
    Button button = new Button();
    button.setTooltip(new Tooltip(sf.getShapeType()));
    button.setGraphic(new ImageView(sf.getShapeIcon()));
    button.setUserData(sf.getShapeType());
    button.setOnAction(evt -> toolbarButtonPressed(evt));
    toolBar.getItems().add(button);
  }

  public void removeShapeFactory(ShapeFactory sf) {
    String name = sf.getShapeType();

    // remove shape factory by name
    Iterator<ShapeFactory> sfit = shapeFactories.iterator();
    while (sfit.hasNext())
      if (sfit.next().getShapeType().equals(name)) sfit.remove();

    // remove toolbar item for shape factory.
    toolBar.getItems().remove(getToolBarButtonByName(sf.getShapeType()));

    // remove all shapes that were created by removed factory.
    Iterator<Shape> sit = shapes.iterator();
    while (sit.hasNext())
      if (sit.next().getShapeType().equals(name)) sit.remove();
    canvas.redraw();

    // select a different shape factory.
    if (shapeFactories.size() > 0)
      setCurrentShapeFactory(shapeFactories.iterator().next());
    else
      setCurrentShapeFactory(null);
  }

  private void mousePressed(MouseEvent evt) {
    if (currentShapeFactory == null) return;

    currentShape = currentShapeFactory.createShape();
    Point2D pnt = new Point2D(evt.getX(), evt.getY());
    currentShape.setStart(pnt);
    currentShape.setEnd(pnt);
    shapes.add(currentShape);

    canvas.redraw();
  }

  private void mouseReleased(MouseEvent evt) {
    if (currentShape == null || currentShapeFactory == null) return;

    currentShape.setEnd(new Point2D(evt.getX(), evt.getY()));
    canvas.redraw();

  }

  private void mouseDragged(MouseEvent evt) {
    if (currentShape == null || currentShapeFactory == null) return;

    currentShape.setEnd(new Point2D(evt.getX(), evt.getY()));
    canvas.redraw();
  }

  private void toolbarButtonPressed(ActionEvent evt) {
    String sfName = (String) ((Button) evt.getSource()).getUserData();
    setCurrentShapeFactory(getShapeFactoryByName(sfName));
  }

  private ShapeFactory getShapeFactoryByName(String name) {
    for (ShapeFactory sf : shapeFactories)
      if (sf.getShapeType().equals(name)) return sf;
    return null;
  }

  private Button getToolBarButtonByName(String name) {
    for (Node n : toolBar.getItems())
      if (name.equals(n.getUserData())) return (Button) n;

    return null;
  }

  private void setCurrentShapeFactory(ShapeFactory sf) {
    currentShapeFactory = sf;
    if (sf == null) return;
    Button button = getToolBarButtonByName(sf.getShapeType());
    if (button != null) button.requestFocus();
  }

}
