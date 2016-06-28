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
package com.diffplug.rcpdemo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.ImmutableMap;

import com.diffplug.common.base.StringPrinter;
import com.diffplug.common.swt.Coat;
import com.diffplug.common.swt.CoatMux;
import com.diffplug.common.swt.Fonts;
import com.diffplug.common.swt.Layouts;
import com.diffplug.talks.rxjava_and_swt.XkcdColorPicker;

import needsboth.NeedsBoth;

public class AppGui {
	final ImmutableMap<String, Coat> modes = ImmutableMap.of(
			"xkcd color picker", cmp -> {
				Layouts.setFill(cmp).margin(0);
				new XkcdColorPicker(cmp, new RGB(0, 0, 255));
			},
			"needsBoth test", this::needsBoth);

	public AppGui(Composite wrapped) {
		Layouts.setGrid(wrapped);

		Composite header = new Composite(wrapped, SWT.NONE);
		Layouts.setGridData(header).grabHorizontal();
		Layouts.setGrid(header).margin(0).numColumns(modes.size());

		Label sep = new Label(wrapped, SWT.SEPARATOR | SWT.HORIZONTAL);
		Layouts.setGridData(sep).grabHorizontal();

		CoatMux content = new CoatMux(wrapped);
		Layouts.setGridData(content).grabAll();

		modes.forEach((label, coat) -> {
			Button button = new Button(header, SWT.RADIO);
			button.setText(label);
			button.setFont(Fonts.systemLarge());
			button.addListener(SWT.Selection, e -> {
				if (button.getSelection()) {
					content.setCoat(coat, null);
				}
			});
		});
	}

	private void needsBoth(Composite parent) {
		Layouts.setFill(parent);
		Text text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		text.setFont(Fonts.systemLarge());
		text.setText(StringPrinter.buildString(printer -> {
			try {
				printer.println("Calling NeedsBoth.parse...");
				printer.println("parse MAVEN: " + NeedsBoth.parse("MAVEN"));
				printer.println("parse P2: " + NeedsBoth.parse("P2"));
			} catch (Throwable e) {
				e.printStackTrace(printer.toPrintWriter());
			}
		}));
	}
}
