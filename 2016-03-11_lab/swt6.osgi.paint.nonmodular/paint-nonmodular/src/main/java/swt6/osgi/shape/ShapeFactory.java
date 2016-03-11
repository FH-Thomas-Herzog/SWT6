package swt6.osgi.shape;

import javafx.scene.image.Image;

public interface ShapeFactory {
  
  public Shape  createShape();
  public String getShapeType();
  public Image  getShapeIcon();
}
