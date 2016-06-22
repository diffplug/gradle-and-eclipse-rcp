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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.diffplug.common.base.Either;
import com.diffplug.common.rx.Rx;
import com.diffplug.common.swt.InteractiveTest;
import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.SwtRx;

import rx.Observable;
import rx.subjects.BehaviorSubject;

@Category(InteractiveTest.class)
public class EventVsFrpOneWay {
	public static abstract class IntValue {
		final Text inputField;
		final Label outputField;

		public IntValue(Composite parent, int initialValue) {
			inputField = new Text(parent, SWT.BORDER | SWT.SINGLE);
			outputField = new Label(parent, SWT.NONE);

			inputField.setText(Integer.toString(initialValue));
			outputField.setText(msgForValue(initialValue));

			Layouts.setGrid(parent);
			Layouts.setGridData(inputField).grabHorizontal();
			Layouts.setGridData(outputField).grabHorizontal();
		}

		protected String msgForValue(int value) {
			return "Value = " + value;
		}

		protected String msgForError(Exception error) {
			return "Error = " + error.getMessage();
		}
	}

	public static class EventBased extends IntValue {
		public EventBased(Composite parent, int initialValue) {
			super(parent, initialValue);
			inputField.addListener(SWT.Modify, e -> {
				try {
					int parsed = Integer.parseInt(inputField.getText());
					outputField.setText(msgForValue(parsed));
				} catch (Exception error) {
					outputField.setText(msgForError(error));
				}
			});
		}
	}

	public static class FrpBased extends IntValue {
		public FrpBased(Composite parent, int initialValue) {
			super(parent, initialValue);
			BehaviorSubject<Integer> value = BehaviorSubject.create(initialValue);
			inputField.addListener(SWT.Modify, e -> {
				try {
					int parsed = Integer.parseInt(inputField.getText());
					value.onNext(parsed);
				} catch (Exception error) {
					outputField.setText(msgForError(error));
				}
			});
			// react to the change
			Rx.subscribe(value.map(this::msgForValue), outputField::setText);
		}
	}

	public static class PureFrpBased extends IntValue {
		public PureFrpBased(Composite parent, int initialValue) {
			super(parent, initialValue);
			Observable<Either<Integer, Exception>> observable = SwtRx.addListener(inputField, SWT.Modify).map(event -> {
				try {
					return Either.createLeft(Integer.parseInt(inputField.getText()));
				} catch (Exception error) {
					return Either.createRight(error);
				}
			});
			Rx.subscribe(observable, either -> {
				String msg = either.fold(this::msgForValue, this::msgForError);
				outputField.setText(msg);
			});
		}
	}

	@Test
	public void eventBased() {
		InteractiveTest.testCoat("Event-based example", 20, 0, cmp -> {
			new EventBased(cmp, 0);
		});
	}

	@Test
	public void frpBased() {
		InteractiveTest.testCoat("FRP-based example", 20, 0, cmp -> {
			new FrpBased(cmp, 0);
		});
	}

	@Test
	public void pureFrpBased() {
		InteractiveTest.testCoat("Pure FRP-based example", 20, 0, cmp -> {
			new FrpBased(cmp, 0);
		});
	}
}
