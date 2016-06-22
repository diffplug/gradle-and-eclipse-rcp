package com.diffplug.rcpdemo;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

import com.diffplug.common.swt.Coat;
import com.diffplug.common.swt.CoatMux;
import com.diffplug.common.swt.ControlWrapper;
import com.diffplug.common.swt.Layouts;
import com.diffplug.talks.rxjava_and_swt.XkcdColorPicker;

import com.google.common.collect.ImmutableMap;

public class AppGui extends ControlWrapper.AroundControl<Composite> {
	final ImmutableMap<String, Coat> modes = ImmutableMap.of(
			"xkcd color picker", cmp -> new XkcdColorPicker(cmp, new RGB(0, 0, 255)),
			"needsBoth test", this::needsBoth);

	public AppGui(Composite wrapped) {
		super(wrapped);

		Layouts.setGrid(wrapped);
		
		Composite header = new Composite(wrapped, SWT.NONE);
		Layouts.setGridData(header).grabHorizontal();
		Layouts.setGrid(header).margin(0).numColumns(modes.size());

		CoatMux content = new CoatMux(wrapped);
		Layouts.setGridData(content).grabAll();

		modes.forEach((label, coat) -> {
			Button button = new Button(header, SWT.RADIO);
			button.setText(label);
			button.addListener(SWT.Selection, e -> {
				content.setCoat(coat, null);
			});
		});
	}

	private void needsBoth(Composite parent) {
		
	}
}
