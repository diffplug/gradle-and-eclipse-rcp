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
package com.diffplug.talks.rxjava_and_swt.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.junit.Test;

import com.diffplug.common.swt.SwtRx;
import com.diffplug.common.swt.jface.Actions;

import rx.Observable;

public class EventVsFrp {
	@Test(expected = ClassCastException.class)
	public void eventBased() {
		Widget widget = (Widget) new Object();
		widget.addListener(SWT.KeyDown, e -> {
			// this is our chance to react
			System.out.println("keyCode=" + e.keyCode);
			// this is how we do filtering
			if (e.keyCode == 'q') {
				System.out.println("user wants to leave talk");
			}
			// this is how we do mapping
			int accel = e.keyCode | e.stateMask;
			String keyPress = Actions.getAcceleratorString(accel);
			System.out.println(keyPress);
		});
	}

	@Test(expected = ClassCastException.class)
	public void frpBased() {
		Widget widget = (Widget) new Object();
		Observable<Event> keyDownStream = SwtRx.addListener(widget, SWT.KeyDown);
		keyDownStream.subscribe(e -> {
			// this is our chance to react
			System.out.println("keyCode=" + e.keyCode);
		});
		// this is how we do filtering
		keyDownStream.filter(e -> e.keyCode == 'q').subscribe(e -> {
			System.out.println("user wants to leave talk");
		});
		// this is how we do mapping
		keyDownStream.map(e -> e.keyCode | e.stateMask).subscribe(accel -> {
			System.out.println(Actions.getAcceleratorString(accel));
		});
	}
}
