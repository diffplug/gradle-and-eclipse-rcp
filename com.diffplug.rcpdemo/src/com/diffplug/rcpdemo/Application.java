package com.diffplug.rcpdemo;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.diffplug.common.swt.Shells;
import com.diffplug.common.swt.SwtMisc;

public class Application implements IApplication {
	@Override
	public Object start(IApplicationContext context) throws Exception {
		Display.setAppName("RCP Demo");
		Shells.builder(SWT.SHELL_TRIM, AppGui::new)
		.setTitle("RCP Demo")
		.setSize(SwtMisc.scaleByFontHeight(40, 30))
		.openOnDisplayBlocking();
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {}
}
