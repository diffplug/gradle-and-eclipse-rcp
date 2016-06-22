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

import java.util.Optional;

import org.eclipse.swt.graphics.RGB;
import org.junit.Assert;
import org.junit.Test;

public class XkcdColorsTest {
	@Test
	public void parse() {
		parseCase("ff0000", 255, 0, 0);
		parseCase("00ff00", 0, 255, 0);
		parseCase("0000ff", 0, 0, 255);
		parseCase("ff0101", 255, 1, 1);
		parseCase("01ff01", 1, 255, 1);
		parseCase("0101ff", 1, 1, 255);
	}

	private void parseCase(String hex, int r, int g, int b) {
		RGB actual = XkcdColors.parse(hex);
		RGB expected = new RGB(r, g, b);
		Assert.assertEquals(expected, actual);
	}

	XkcdColors xkcd = XkcdColors.load();

	@Test
	public void load() {
		loadCaseEmpty("xkdkdjsdf");
		loadCase("purple", 126, 30, 156);
	}

	private void loadCase(String name, int r, int g, int b) {
		loadCase(name, Optional.of(new RGB(r, g, b)));
	}

	private void loadCaseEmpty(String name) {
		loadCase(name, Optional.empty());
	}

	private void loadCase(String name, Optional<RGB> expected) {
		Optional<RGB> actual = xkcd.rgbForName(name);
		Assert.assertEquals(expected, actual);
	}
}
