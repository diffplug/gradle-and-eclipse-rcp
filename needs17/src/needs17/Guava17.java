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
