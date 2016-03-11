/**
 * 
 */
package swt6.osgi.shape.rectangle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import swt6.osgi.shape.ShapeFactory;

/**
 * @author Thomas Herzog <s1310307011@students.fh-hagenberg.at>
 * @date Mar 11, 2016
 */
public class RectangleActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		context.registerService(ShapeFactory.class, new RectangleFactory(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}

}
