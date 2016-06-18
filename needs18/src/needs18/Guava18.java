package needs18;

import java.util.concurrent.Executor;

import com.google.common.util.concurrent.MoreExecutors;

public class Guava18 {
	/**
	 * Returns an executor that runs immediately, which is
	 * present in Guava 18, but absent in 17.
	 * 
	 * http://google.github.io/guava/releases/18.0/api/diffs/changes/com.google.common.util.concurrent.MoreExecutors.html#com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService_added()
	 */
	public static Executor sameThread() {
		return MoreExecutors.directExecutor();
	}
}
