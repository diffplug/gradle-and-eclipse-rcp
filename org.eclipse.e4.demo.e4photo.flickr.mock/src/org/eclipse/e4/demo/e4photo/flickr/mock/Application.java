package org.eclipse.e4.demo.e4photo.flickr.mock;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.demo.e4photo.flickr.ui.Flickr;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	public Object start(IApplicationContext context) throws Exception {
		Display display = new Display();
		Shell shell = new Shell();
		
		Bundle bundle = FrameworkUtil.getBundle(Application.class);
		BundleContext bundleContext = bundle.getBundleContext();
		IEclipseContext eclipseCtx = EclipseContextFactory.getServiceContext(bundleContext);
		eclipseCtx.set(Composite.class.getName(), shell);
		
		ContextInjectionFactory.make(Flickr.class, eclipseCtx);
		
		shell.open();
		
		while( ! shell.isDisposed() ) {
			if( ! display.readAndDispatch() ) {
				display.sleep();
			}
		}
		
		display.dispose();
		
		return IApplication.EXIT_OK;
	}

	public void stop() {
		// nothing to do
	}
}
