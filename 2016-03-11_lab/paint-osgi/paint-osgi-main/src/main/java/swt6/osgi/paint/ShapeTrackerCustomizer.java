package swt6.osgi.paint;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import swt6.osgi.shape.ShapeFactory;
import swt6.util.JavaFxUtils;

public class ShapeTrackerCustomizer implements ServiceTrackerCustomizer<ShapeFactory, ShapeFactory> {

  private static enum Action {
    ADDED, MODIFIED, REMOVED
  }

  private BundleContext context;
  private PaintWindow   window;

  public ShapeTrackerCustomizer(BundleContext context, PaintWindow window) {
    this.context = context;
    this.window = window;
  }

  @Override
  public ShapeFactory addingService(ServiceReference<ShapeFactory> ref) {
    ShapeFactory sf = context.getService(ref);
    processEventInUIThread(Action.ADDED, ref, sf);
    return sf;
  }

  @Override
  public void modifiedService(ServiceReference<ShapeFactory> ref, ShapeFactory sf) {
    processEventInUIThread(Action.MODIFIED, ref, sf);
  }

  @Override
  public void removedService(ServiceReference<ShapeFactory> ref, ShapeFactory sf) {
    processEventInUIThread(Action.REMOVED, ref, sf);
  }

  private void processEvent(Action action, ServiceReference<ShapeFactory> ref,
      ShapeFactory sf) {
    switch (action) {
    case MODIFIED:
      window.removeShapeFactory(sf);
      window.addShapeFactory(sf);
      break;
      
    case ADDED:
      window.addShapeFactory(sf);
      break;

    case REMOVED:
      window.removeShapeFactory(sf);
      break;
    }
  }

  private void processEventInUIThread(final Action action,
      final ServiceReference<ShapeFactory> ref, final ShapeFactory sf) {
    try {
      JavaFxUtils.runAndWait(() -> processEvent(action, ref, sf));
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

 }
