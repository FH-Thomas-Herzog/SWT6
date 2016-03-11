package swt6.osgi.shape.line;

import javafx.scene.image.Image;
import swt6.osgi.shape.Shape;
import swt6.osgi.shape.ShapeFactory;

public class LineFactory implements ShapeFactory {

  private Image icon;

  public LineFactory() {
    icon = new Image(this.getClass().getResourceAsStream("line.png"));
  }

  @Override
  public Shape createShape() {
    return new Line(this);
  }

  @Override
  public String getShapeType() {
    return "Line";
  }

  @Override
  public Image getShapeIcon() {
    return icon;
  }

}
