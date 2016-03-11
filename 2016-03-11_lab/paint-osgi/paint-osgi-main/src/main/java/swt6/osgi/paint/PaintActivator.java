package swt6.osgi.paint;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import swt6.osgi.shape.ShapeFactory;
import swt6.util.JavaFxUtils;

/**
 * Bundle activator which handles the bundle activate and stop events.
 * 
 * @author Thomas Herzog <s1310307011@students.fh-hagenberg.at>
 *
 */
public class PaintActivator implements BundleActivator {

	private PaintWindow window;
	private ServiceTracker<ShapeFactory, ShapeFactory> shapeTracker;

	@Override
	public void start(BundleContext arg0) throws Exception {
		JavaFxUtils.initJavaFx();
		JavaFxUtils.runAndWait(() -> startUI(arg0));

		shapeTracker = new ServiceTracker<>(arg0, ShapeFactory.class, new ShapeTrackerCustomizer(arg0, window));
		shapeTracker.open();
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		JavaFxUtils.runAndWait(() -> stopUI(arg0));

		if (shapeTracker != null) {
			shapeTracker.close();
		}
	}

	private void stopUI(BundleContext arg0) {
		if (window != null) {
			window.close();
		}
	}

	private void startUI(BundleContext ctx) {
		window = new PaintWindow();
		window.show();
		window.addOnCloseEventHandler((event) -> {
			try {
				ctx.getBundle().stop();
				stopUI(ctx);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		// window.addShapeFactory(new RectangleFactory());
	}
}
