/*
 * Copyright 2017 Matthew Tamlin
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

package com.matthewtamlin.fortytwo.library.util;

/**
 * An object which allows listeners to subscribe for callbacks. This interface does not define the
 * conditions which result in callbacks being delivered.
 *
 * @param <L>
 * 		the type of listener which can register for callbacks
 */
public interface Listenable<L> {
	/**
	 * Registers the supplied listener for future callbacks. If the supplied listener is null or is
	 * already registered, then the method returns normally.
	 *
	 * @param listener
	 * 		the listener to register
	 */
	void registerListener(L listener);

	/**
	 * Unregisters the supplied listener from receiving future callbacks. If the supplied listener
	 * is null or is not registered, then the method returns normally.
	 *
	 * @param listener
	 * 		the listener to unregister
	 */
	void unregisterListener(L listener);
}