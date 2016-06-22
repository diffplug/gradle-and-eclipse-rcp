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

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Listener;

import rx.Observable;

// @formatter:off
// to keep the spacing tight for the slides
public interface ColorPickerApis {
	public interface ExposeDetails {
		/** Adds the given listener to the color canvas (MouseMove, MouseDown, etc). */
		void addListener(int eventType, Listener listener);
		/** Converts a coordinate to an RGB value. */
		RGB toColor(int x, int y);
	}

	public interface CustomEventListenerPair {
		public class ColorEvent {
			RGB color;
		}
		public interface ColorListener {
			void handle(ColorEvent event);
		}
		void addMouseMoveListener(ColorListener listener);
		void addMouseDownListener(ColorListener listener);
	}

	public interface Reactive {
		Observable<RGB> rxMouseMove();
		Observable<RGB> rxMouseDown();
	}
}
// @formatter:on
