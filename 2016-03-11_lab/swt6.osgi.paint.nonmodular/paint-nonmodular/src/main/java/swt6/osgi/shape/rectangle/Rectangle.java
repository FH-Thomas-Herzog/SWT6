package swt6.osgi.shape.rectangle;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import swt6.osgi.shape.AbstractShape;
import swt6.osgi.shape.ShapeFactory;

public class Rectangle extends AbstractShape {

  private static final Color FILL_COLOR = Color.rgb(75, 172, 198);

  public Rectangle(ShapeFactory sf) {
    super(sf);
  }

  @Override
  public void draw(GraphicsContext gc) {
    Rectangle2D rect = getBoundingRectangle();
    gc.setFill(FILL_COLOR);
    gc.setStroke(Color.BLACK);    
    gc.fillRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
    gc.strokeRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());

  }

}
