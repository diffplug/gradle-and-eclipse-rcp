package com.diffplug.rcpdemo;

import java.util.Arrays;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class HeadlessApp implements IApplication {
	@Override
	public Object start(IApplicationContext context) throws Exception {
		String[] args = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		System.out.println(Arrays.asList(args));
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {}
}
