package swt6.osgi.shape;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

public abstract class AbstractShape implements Shape {

  private Point2D start = Point2D.ZERO;
  private Point2D end = Point2D.ZERO;
  private ShapeFactory shapeFactory;
  
  public AbstractShape(ShapeFactory sf) {
    this.shapeFactory = sf;
  }
  
  @Override
  public Point2D getStart() {
    return start;
  }

  @Override
  public void setStart(Point2D start) {
    this.start = start;
  }

  @Override
  public Point2D getEnd() {
    return end;
  }

  @Override
  public void setEnd(Point2D end) {
    this.end = end;
  }

  @Override
  public String getShapeType() {
    return shapeFactory.getShapeType();
  }
  
  public Rectangle2D getBoundingRectangle() {
    return new Rectangle2D(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()), 
                           Math.abs(start.getX() - end.getX()), Math.abs(start.getY() - end.getY()));
  }

}
