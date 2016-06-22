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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.diffplug.common.rx.Rx;
import com.diffplug.common.rx.RxBox;
import com.diffplug.common.swt.InteractiveTest;
import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.SwtRx;

import rx.subjects.BehaviorSubject;

@Category(InteractiveTest.class)
public class EventVsFrpTwoWay {

	public static abstract class IntValue {
		final Text inputField;
		final Label outputField;
		final Scale scale;

		public IntValue(Composite parent, int initialValue) {
			inputField = new Text(parent, SWT.BORDER | SWT.SINGLE);
			outputField = new Label(parent, SWT.NONE);
			scale = new Scale(parent, SWT.HORIZONTAL);

			inputField.setText(Integer.toString(initialValue));
			outputField.setText(msgForValue(initialValue));
			scale.setMinimum(0);
			scale.setMaximum(100);
			scale.setSelection(initialValue);

			Layouts.setGrid(parent);
			Layouts.setGridData(inputField).grabHorizontal();
			Layouts.setGridData(outputField).grabHorizontal();
			Layouts.setGridData(scale).grabHorizontal();
		}

		protected String msgForValue(int value) {
			return "Value = " + value;
		}

		protected String msgForError(Exception error) {
			return "Error = " + error.getMessage();
		}
	}

	public static class EventBased extends IntValue {
		int value;

		public EventBased(Composite parent, int initialValue) {
			super(parent, initialValue);
			this.value = initialValue;
			inputField.addListener(SWT.Modify, e -> {
				try {
					int parsed = Integer.parseInt(inputField.getText());
					setValue(parsed);
				} catch (Exception error) {
					outputField.setText(msgForError(error));
				}
			});

			scale.addListener(SWT.Selection, e -> {
				setValue(scale.getSelection());
			});
		}

		public void setValue(int value) {
			if (this.value != value) {
				this.value = value;
				Point caretPos = inputField.getSelection();
				inputField.setText(Integer.toString(value));
				inputField.setSelection(caretPos);
				outputField.setText(msgForValue(value));
				scale.setSelection(value);
			}
		}

		public int getValue() {
			return value;
		}

		public void addValueListener(Object yetAnotherType) {
			// how many listeners will it take!!
		}
	}

	/** Small modification which keeps it working better. */
	public static class FrpBased extends IntValue {
		final BehaviorSubject<Integer> value;

		public FrpBased(Composite parent, int initialValue) {
			super(parent, initialValue);
			value = BehaviorSubject.create(initialValue);
			RxBox<String> rwText = SwtRx.textImmediate(inputField);
			Rx.subscribe(rwText, text -> {
				try {
					int parsed = Integer.parseInt(text);
					value.onNext(parsed);
				} catch (Exception error) {
					outputField.setText(msgForError(error));
				}
			});
			scale.addListener(SWT.Selection, e -> {
				value.onNext(scale.getSelection());
			});
			Rx.subscribe(value.map(Object::toString), rwText::set);
			Rx.subscribe(value.map(this::msgForValue), outputField::setText);
			Rx.subscribe(value, scale::setSelection);
		}

		public BehaviorSubject<Integer> rwValue() {
			return value;
		}
	}

	@Test
	public void eventBased() {
		InteractiveTest.testCoat("Event-based example", 20, 0, cmp -> {
			new EventBased(cmp, 10);
		});
	}

	@Test
	public void frpBased() {
		InteractiveTest.testCoat("FRP-based example", 20, 0, cmp -> {
			new FrpBased(cmp, 10);
		});
	}
}
