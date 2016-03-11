package swt6.osgi.shape.line;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import swt6.osgi.shape.AbstractShape;
import swt6.osgi.shape.ShapeFactory;

public class Line extends AbstractShape {
  
  private static final Color LINE_COLOR = Color.BLACK;
  
  public Line(ShapeFactory sf) {
    super(sf);
  }
  
  @Override
  public void draw(GraphicsContext gc) {
    gc.setStroke(LINE_COLOR);
    gc.setLineWidth(2);
    gc.strokeLine(getStart().getX(), getStart().getY(), 
                  getEnd().getX(), getEnd().getY());
  }

}
