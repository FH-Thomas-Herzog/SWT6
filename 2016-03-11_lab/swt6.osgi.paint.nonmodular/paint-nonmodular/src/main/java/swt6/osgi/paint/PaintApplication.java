package swt6.osgi.paint;

import java.util.logging.LogManager;
import swt6.osgi.shape.circle.CircleFactory;
import swt6.osgi.shape.line.LineFactory;
import swt6.osgi.shape.rectangle.RectangleFactory;
import swt6.util.JavaFxUtils;

public class PaintApplication {

  public static void main(String[] args) throws Exception {
    LogManager.getLogManager().reset(); // turn off JDK logging
    JavaFxUtils.initJavaFx();

    JavaFxUtils.runLater(() -> {
      PaintWindow paintWindow = new PaintWindow();
      paintWindow.addOnCloseEventHandler(evt -> JavaFxUtils.exitJavaFx());
      paintWindow.show();

      paintWindow.addShapeFactory(new RectangleFactory());
      paintWindow.addShapeFactory(new CircleFactory());
      paintWindow.addShapeFactory(new LineFactory());
    });
  }

}
