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
import org.eclipse.swt.widgets.Scale;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.diffplug.common.swt.InteractiveTest;
import com.diffplug.common.swt.Layouts;

@Category(InteractiveTest.class)
public class ColorPickerTest {
	@Test
	public void testControl() {
		InteractiveTest.testCoat("Should show the YCbCr plane at various values of Y", cmp -> {
			Layouts.setGrid(cmp);

			Scale scale = new Scale(cmp, SWT.HORIZONTAL);
			scale.setMinimum(0);
			scale.setMaximum(255);
			scale.setSelection(128);
			Layouts.setGridData(scale).grabHorizontal();

			ColorPicker colors = new ColorPicker(cmp);
			Layouts.setGridData(colors).grabAll();

			scale.addListener(SWT.Selection, e -> {
				colors.setY(scale.getSelection());
			});
		});
	}
}
