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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import com.diffplug.common.swt.ControlWrapper;
import com.diffplug.common.swt.SwtRx;

import rx.Observable;

/** Shows a pane of CbCr at constant Y. */
public class ColorPicker extends ControlWrapper.AroundControl<Canvas> implements ColorPickerApis.Reactive {
	int luminance = 128;

	final Observable<RGB> mouseDown, mouseMove;

	public ColorPicker(Composite parent) {
		super(new Canvas(parent, SWT.DOUBLE_BUFFERED));
		setY(128);
		wrapped.addListener(SWT.Paint, e -> {
			Point size = wrapped.getSize();
			Image img = getMapFor(e.display);
			e.gc.drawImage(img, 0, 0, _256, _256, 0, 0, size.x, size.y);
		});
		mouseDown = SwtRx.addListener(wrapped, SWT.MouseDown).map(this::posToColor);
		mouseMove = SwtRx.addListener(wrapped, SWT.MouseMove).map(this::posToColor);
	}

	private RGB posToColor(Event e) {
		Point size = wrapped.getSize();
		int cb = limitInt(e.x * _256 / size.x);
		int cr = limitInt(e.y * _256 / size.y);
		return fromYCbCr(luminance, cb, cr);
	}

	@Override
	public Observable<RGB> rxMouseDown() {
		return mouseDown;
	}

	@Override
	public Observable<RGB> rxMouseMove() {
		return mouseMove;
	}

	private static final int _256 = 256;

	/** Sets the luminance value of the pane. */
	public void setY(int y) {
		this.luminance = limitInt(4 * limitRound(y / 4.0));
		wrapped.redraw();
	}

	///////////////////////////////////////////////////////
	// Calculate and cache CbCr map at a given luminance //
	///////////////////////////////////////////////////////
	private int lastLuminance = -1;
	private Image lastImage;

	private Image getMapFor(Display display) {
		if (luminance == lastLuminance) {
			return lastImage;
		}
		if (lastImage != null) {
			lastImage.dispose();
		}
		lastLuminance = luminance;
		lastImage = new Image(display, _256, _256);
		GC gc = new GC(lastImage);
		for (int x = 0; x < _256; ++x) {
			for (int y = 0; y < _256; ++y) {
				RGB rgb = fromYCbCr(new RGB(luminance, x, y));
				gc.setForeground(new Color(display, rgb));
				gc.drawPoint(x, y);
			}
		}
		gc.dispose();
		return lastImage;
	}

	////////////////////
	// RGB <--> YCrCb //
	////////////////////
	/** Converts an RGB value to YCrCb. */
	public static RGB toYCbCr(RGB rgb) {
		return toYCbCr(rgb.red, rgb.green, rgb.blue);
	}

	/** Converts an RGB value to YCrCb. */
	public static RGB toYCbCr(double r, double g, double b) {
		int y = limitRound(0.299 * r + 0.587 * g + 0.114 * b);
		int cb = limitRound(128 - 0.168736 * r - 0.331264 * g + 0.5 * b);
		int cr = limitRound(128 + 0.5 * r - 0.418688 * g - 0.081312 * b);
		return new RGB(y, cb, cr);
	}

	/** Converts a YCrCb value to RGB. */
	public static RGB fromYCbCr(RGB YCbCr) {
		return fromYCbCr(YCbCr.red, YCbCr.green, YCbCr.blue);
	}

	/** Converts a YCrCb value to RGB. */
	public static RGB fromYCbCr(double y, double cb, double cr) {
		int r = limitRound(y + 1.402 * (cr - 128));
		int g = limitRound(y - 0.34414 * (cb - 128) - 0.71414 * (cr - 128));
		int b = limitRound(y + 1.772 * (cb - 128));
		return new RGB(r, g, b);
	}

	/** Rounds and limits to the range 0-255. */
	private static int limitRound(double value) {
		return limitInt((int) Math.round(value));
	}

	/** Limits to the range 0-255. */
	private static int limitInt(int value) {
		if (value < 0) {
			return 0;
		} else if (value > 255) {
			return 255;
		} else {
			return value;
		}
	}
}
