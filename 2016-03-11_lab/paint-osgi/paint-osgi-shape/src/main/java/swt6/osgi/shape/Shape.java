package swt6.osgi.shape;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public interface Shape {
  
  public Point2D getStart();
  public void setStart(Point2D start);

  public Point2D getEnd();
  public void setEnd(Point2D end);
  
  public String getShapeType();

  public void draw(GraphicsContext gc);
}
