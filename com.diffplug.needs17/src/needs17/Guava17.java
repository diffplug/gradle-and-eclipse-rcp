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
package needs17;

import com.google.common.base.Enums;

public class Guava17 {
	/**
	 * Returns an enum value from its name, using a function which is
	 * present in Guava 17, but absent in 18.
	 *
	 * http://google.github.io/guava/releases/18.0/api/diffs/changes/com.google.common.base.Enums.html#com.google.common.base.Enums.valueOfFunction_removed(java.lang.Class<T>)
	 */
	@SuppressWarnings("deprecation")
	public static <T extends Enum<T>> T parse(Class<T> clazz, String name) {
		return Enums.valueOfFunction(clazz).apply(name);
	}
}
