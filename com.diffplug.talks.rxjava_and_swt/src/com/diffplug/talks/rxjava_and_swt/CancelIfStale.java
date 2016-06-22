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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.diffplug.common.base.Box;

public class CancelIfStale {
	private final Box<CompletionStage<?>> delegate = new Box.Default<CompletionStage<?>>(new CompletableFuture<>()) {
		@Override
		public void set(CompletionStage<?> obj) {
			this.obj.toCompletableFuture().cancel(true);
			this.obj = obj;
		}
	};

	public <T> CompletionStage<T> filter(CompletionStage<T> obj) {
		delegate.set(obj.toCompletableFuture());
		return obj;
	}
}
