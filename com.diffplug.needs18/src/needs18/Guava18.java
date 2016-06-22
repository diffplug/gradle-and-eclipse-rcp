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
