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

package com.matthewtamlin.multiple_choice_answer_view.library.util;

import com.matthewtamlin.android_utilities.library.testing.Tested;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import static com.matthewtamlin.java_utilities.checkers.IntChecker.checkGreaterThan;
import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * A stack which is limited in size and does not allows duplicate elements. Since there can be no
 * duplicates, pushing an item to the stack which is already contained will move it to the top. If
 * the size limit has been reached when an item is pushed to the stack, the item at the bottom of
 * the stack is evicted to make room.
 *
 * @param <T>
 * 		the type of elements contained in the stack
 */
@Tested(testMethod = "automated", requiresInstrumentation = false)
public class EvictingStackSet<T> extends Stack<T> implements Listenable<EvictingStackSet
		.EvictionListener<T>> {
	/**
	 * The listeners to call when elements are evicted.
	 */
	private final Set<EvictionListener<T>> listeners = new HashSet<>();

	/**
	 * When the size of the stack exceeds this value, the bottom element is evicted.
	 */
	private int maxSize;

	/**
	 * Constructs a new empty EvictingStack.
	 *
	 * @param maxSize
	 * 		the size limit of the stack, at least 1
	 * @throws IllegalArgumentException
	 */
	public EvictingStackSet(final int maxSize) {
		super();

		this.maxSize = checkGreaterThan(maxSize, 0, "maxSize cannot be less than 1.");
	}

	/**
	 * Constructs a new EvictingStack from an existing collection. When the new stack is populated,
	 * elements are added using the ordering of the supplied collection. This will affect which
	 * elements end up in the stack if the supplied collection is larger than the stack size limit.
	 *
	 * @param maxSize
	 * 		the size limit of the stack, at least 1
	 * @param contents
	 * 		the elements to add to the stack, not null
	 * @throws IllegalArgumentException
	 * 		if {@code maxSize} is less than 1
	 * @throws IllegalArgumentException
	 * 		if {@code contents} is null
	 */
	public EvictingStackSet(final int maxSize, final Collection<? extends T> contents) {
		this.maxSize = checkGreaterThan(maxSize, 0, "maxSize cannot be less than 1.");
		checkNotNull(contents, "contents cannot be null.");

		for (final T t : contents) {
			push(t);
		}
	}

	/**
	 * @return the size limit of the stack
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * Sets the size limit of the stack. If the stack contains more that this many elements, then
	 * elements are evicted from the bottom until the stack size matches the new max size.
	 *
	 * @param maxSize
	 * 		the size limit to use, at least 1
	 * @throws IllegalArgumentException
	 * 		if {@code maxSize} is less than 1
	 */
	public void setMaxSize(final int maxSize) {
		this.maxSize = checkGreaterThan(maxSize, 0, "maxSize cannot be less than 1.");

		// If the stack exceeds the new size limit, remove elements
		while (this.size() > maxSize) {
			final T bottomItem = get(0);
			remove(bottomItem);

			for (final EvictionListener<T> listener : listeners) {
				listener.onEviction(this, bottomItem);
			}
		}
	}

	@Override
	public T push(final T object) {
		if (contains(object)) {
			remove(object);
		}

		// If the stack will become too big, remove elements until it's the right size
		while (size() >= maxSize) {
			final T bottomItem = get(0);
			remove(bottomItem);

			for (final EvictionListener<T> listener : listeners) {
				listener.onEviction(this, bottomItem);
			}
		}

		return super.push(object);
	}

	@Override
	public void registerListener(final EvictionListener<T> listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	@Override
	public void unregisterListener(final EvictionListener<T> listener) {
		listeners.remove(listener);
	}

	/**
	 * Callback listener to be called when elements are evicted from an EvictingStackSet.
	 *
	 * @param <V>
	 * 		the type of elements in the stack set
	 */
	public interface EvictionListener<V> {
		/**
		 * Invoked when an item is removed from an EvictingStackSet this listener is register to.
		 *
		 * @param evictingStackSet
		 * 		the EvictingStackSet the element was evicted from, not null
		 * @param evicted
		 * 		the element which was evicted, may be null
		 */
		public void onEviction(EvictingStackSet<V> evictingStackSet, V evicted);
	}
}