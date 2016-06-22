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
package needsboth;

public class NeedsBoth {
	static DepSystem result;

	public static DepSystem parse(String input) {
		needs18.Guava18.sameThread().execute(() -> {
			result = needs17.Guava17.parse(DepSystem.class, input);
		});
		return result;
	}

	public static enum DepSystem {
		MAVEN, P2;

		public boolean supportsDiamondDependencyConflicts() {
			switch (this) {
			case MAVEN:
				return false;
			case P2:
				return true;
			default:
				throw new IllegalArgumentException("No such dependency management system.");
			}
		}
	}

	/** This will fail with NoSuchMethodError without OSGi magic. */
	public static void main(String[] args) {
		System.out.println(NeedsBoth.parse("MAVEN").name());
		System.out.println(NeedsBoth.parse("P2").name());
	}
}
