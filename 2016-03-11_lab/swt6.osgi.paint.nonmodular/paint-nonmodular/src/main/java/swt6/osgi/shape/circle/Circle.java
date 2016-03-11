package swt6.osgi.shape.circle;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import swt6.osgi.shape.AbstractShape;
import swt6.osgi.shape.ShapeFactory;

public class Circle extends AbstractShape {
  
  private static final Color FILL_COLOR = Color.rgb(253, 116, 104);
  
  public Circle(ShapeFactory sf) {
    super(sf);
  }
  
  @Override
  public void draw(GraphicsContext gc) {
    Rectangle2D rect = getBoundingRectangle();
    gc.setFill(FILL_COLOR);
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(2);
    
    gc.fillOval(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
    gc.strokeOval(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
  }

}
