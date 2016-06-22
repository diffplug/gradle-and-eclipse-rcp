package com.diffplug.rcpdemo;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.diffplug.common.swt.InteractiveTest;

@Category(InteractiveTest.class)
public class AppGuiTest {
	@Test
	public void test() {
		InteractiveTest.testCoat("AppGui", AppGui::new);
	}
}
