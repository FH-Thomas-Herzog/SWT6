package swt6.osgi.shape.rectangle;

import javafx.scene.image.Image;
import swt6.osgi.shape.Shape;
import swt6.osgi.shape.ShapeFactory;

public class RectangleFactory implements ShapeFactory {

  private Image icon;
  
  public RectangleFactory() {
    icon = new Image(this.getClass().getResourceAsStream("rectangle.png"));
  }

  @Override
  public Shape createShape() {
    return new Rectangle(this);
  }

  @Override
  public String getShapeType() {
    return "Rectangle";
  }

  @Override
  public Image getShapeIcon() {
    return icon;
  }

}
