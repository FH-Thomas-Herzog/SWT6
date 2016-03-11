package swt6.osgi.shape.circle;

import javafx.scene.image.Image;
import swt6.osgi.shape.Shape;
import swt6.osgi.shape.ShapeFactory;

public class CircleFactory implements ShapeFactory {

  private Image icon;

  public CircleFactory() {
    icon = new Image(this.getClass().getResourceAsStream("circle.png"));
  }

  @Override
  public Shape createShape() {
    return new Circle(this);
  }

  @Override
  public String getShapeType() {
    return "Circle";
  }

  @Override
  public Image getShapeIcon() {
    return icon;
  }

}
