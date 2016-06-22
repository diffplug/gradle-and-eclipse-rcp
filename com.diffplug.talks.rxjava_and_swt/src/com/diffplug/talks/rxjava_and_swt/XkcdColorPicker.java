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
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Scale;

import com.diffplug.common.rx.Rx;
import com.diffplug.common.rx.RxBox;
import com.diffplug.common.swt.ControlWrapper;
import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.SwtExec;

import rx.Observable;

public class XkcdColorPicker extends ControlWrapper.AroundControl<Composite> {
	public XkcdColorPicker(Composite parent, RGB initRGB) {
		super(new Composite(parent, SWT.NONE));
		RGB initYCbCr = ColorPicker.toYCbCr(initRGB);

		// create a scale and bind it to an RxBox<Integer>
		RxBox<Integer> luminance = RxBox.of(initYCbCr.red);

		// colorpanel in the center
		ColorPicker cbcrPanel = new ColorPicker(wrapped);
		Rx.subscribe(luminance, cbcrPanel::setY);

		// controls at the right
		Composite rightCmp = new Composite(wrapped, SWT.NONE);

		// scale below
		Scale scale = new Scale(wrapped, SWT.HORIZONTAL);
		scale.setMinimum(0);
		scale.setMaximum(255);
		Rx.subscribe(luminance, scale::setSelection);
		scale.addListener(SWT.Selection, e -> {
			luminance.set(scale.getSelection());
		});

		Layouts.setGrid(wrapped).numColumns(2);
		Layouts.setGridData(cbcrPanel).grabAll();
		Layouts.setGridData(rightCmp).grabVertical().verticalSpan(2);
		Layouts.setGridData(scale).grabHorizontal();

		// populate the bottom
		Layouts.setGrid(rightCmp).margin(0);
		XkcdColors.Lookup xkcdLookup = new XkcdColors.Lookup(rightCmp);

		Group hoverGrp = new Group(rightCmp, SWT.SHADOW_ETCHED_IN);
		hoverGrp.setText("Hover");
		createGroup(hoverGrp, cbcrPanel.rxMouseMove(), xkcdLookup);

		Group clickGrp = new Group(rightCmp, SWT.SHADOW_ETCHED_IN);
		clickGrp.setText("Click");
		createGroup(clickGrp, cbcrPanel.rxMouseDown(), xkcdLookup);
	}

	private void createGroup(Composite parent, Observable<RGB> rxRgb, XkcdColors.Lookup xkcdLookup) {
		Layouts.setFill(parent);
		ColorComparePanel colorCompare = new ColorComparePanel(parent);
		CancelIfStale cancelling = new CancelIfStale();

		SwtExec.Guarded guarded = SwtExec.async().guardOn(parent);
		guarded.subscribe(rxRgb, rgb -> {
			// set raw
			colorCompare.setActual(rgb);
			// clear empty, then start to look for the answer
			colorCompare.setNearestEmpty();
			guarded.subscribe(cancelling.filter(xkcdLookup.get(rgb)), entry -> {
				colorCompare.setNearest(entry.getKey(), entry.getValue());
			});
		});
	}
}
