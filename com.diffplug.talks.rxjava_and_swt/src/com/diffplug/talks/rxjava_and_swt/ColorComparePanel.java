/*
 * Copyright 2016 DiffPlug
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diffplug.talks.rxjava_and_swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.diffplug.common.base.TreeStream;
import com.diffplug.common.swt.ControlWrapper;
import com.diffplug.common.swt.Fonts;
import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.SwtMisc;

public class ColorComparePanel extends ControlWrapper.AroundControl<Composite> {

	private final Label swatchActual, swatchNearest;
	private final Text rgbActual, rgbNearest;
	private final Text nameNearest;

	public ColorComparePanel(Composite parent) {
		super(new Composite(parent, SWT.NONE));
		Layouts.setGrid(wrapped)
				.numColumns(2)
				.columnsEqualWidth(true)
				.horizontalSpacing(0);
		swatchActual = new Label(wrapped, SWT.NONE);
		swatchNearest = new Label(wrapped, SWT.NONE);
		Layouts.setGridData(swatchActual).grabHorizontal();
		Layouts.setGridData(swatchNearest).grabHorizontal();

		Label actualLbl = new Label(wrapped, SWT.NONE);
		actualLbl.setText("Actual");
		Label nearestLbl = new Label(wrapped, SWT.NONE);
		nearestLbl.setText("Nearest");

		rgbActual = new Text(wrapped, SWT.BORDER | SWT.READ_ONLY);
		rgbNearest = new Text(wrapped, SWT.BORDER | SWT.READ_ONLY);
		Layouts.setGridData(rgbActual).grabHorizontal();
		Layouts.setGridData(rgbNearest).grabHorizontal();

		nameNearest = new Text(wrapped, SWT.BORDER | SWT.READ_ONLY);
		Layouts.setGridData(nameNearest).horizontalSpan(2).grabHorizontal();

		// set all fonts to system large
		TreeStream.breadthFirst(SwtMisc.treeDefControl(), parent)
				.forEach(ctl -> ctl.setFont(Fonts.systemLarge()));

		rgbActual.setText("                    ");
		rgbNearest.setText("                    ");
	}

	public void setActual(RGB rgb) {
		swatchActual.setBackground(new Color(wrapped.getDisplay(), rgb));
		rgbActual.setText(rgb.red + " " + rgb.green + " " + rgb.blue);
	}

	public void setNearest(String name, RGB rgb) {
		swatchNearest.setBackground(new Color(wrapped.getDisplay(), rgb));
		rgbNearest.setText(rgb.red + " " + rgb.green + " " + rgb.blue);
		nameNearest.setText(name);
	}

	public void setNearestEmpty() {
		swatchNearest.setBackground(SwtMisc.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		rgbNearest.setText("");
		nameNearest.setText("");
	}
}
