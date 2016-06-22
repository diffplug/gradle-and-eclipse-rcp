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
import org.junit.Assert;
import org.junit.Test;

public class YCbCrTest {
	@Test
	/** Round trip every RGB color through the YCbCr color space. */
	public void roundTrip() {
		for (int r = 0; r < 255; ++r) {
			for (int g = 0; g < 255; ++g) {
				for (int b = 0; b < 255; ++b) {
					roundTripTestCase(r, g, b);
				}
			}
		}
	}

	private void roundTripTestCase(int r, int g, int b) {
		RGB expected = new RGB(r, g, b);
		RGB yCbCr = ColorPicker.toYCbCr(expected);
		RGB roundTrip = ColorPicker.fromYCbCr(yCbCr);
		if (!expected.equals(roundTrip)) {
			// if they don't match exactly, allow a maximum delta of 1 for rounding errors 
			int deltaR = Math.abs(roundTrip.red - expected.red);
			int deltaG = Math.abs(roundTrip.green - expected.green);
			int deltaB = Math.abs(roundTrip.blue - expected.blue);
			int maxDelta = Math.max(Math.max(deltaR, deltaG), deltaB);
			Assert.assertTrue(maxDelta <= 1);
		}
	}
}
