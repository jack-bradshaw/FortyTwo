package com.matthewtamlin.multiple_choice_answer_view.library.util;

import java.util.Collection;
import java.util.Stack;

import static com.matthewtamlin.java_utilities.checkers.IntChecker.checkGreaterThan;
import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * A stack which is limited in size, and does not allows duplicate insertions. Pushing an item which
 * is already in the stack will move it to the top. The size limit has been reached and an item is
 * pushed to the stack, the item at the bottom of the stack is evicted to make room.
 *
 * @param <T>
 * 		the type of the elements contained in the stack
 */
public class EvictingStackSet<T> extends Stack<T> {
	/**
	 * When the size of the stack exceeds this value, the bottom element is evicted.
	 */
	private int maxSize;

	/**
	 * Constructs a new empty EvictingStack.
	 *
	 * @param maxSize
	 * 		the size limit after which elements will be evicted, greater than or equal to zero
	 * @throws IllegalArgumentException
	 */
	public EvictingStackSet(final int maxSize) {
		super();

		this.maxSize = checkGreaterThan(maxSize, 0, "maxSize cannot be less than 1.");
	}

	/**
	 * Constructs a new EvictingStack from an existing collection. When the new stack is populated,
	 * elements are added using the total ordering of the supplied collection. This will affect
	 * which elements end up in the stack if the supplied collection is larger than the stack size
	 * limit.
	 *
	 * @param maxSize
	 * 		the size limit after which elements will be evicted, greater than or equal to zero
	 * @param contents
	 * 		the elements to add to the stack
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

	@Override
	public T push(final T object) {
		if (contains(object)) {
			remove(object);
		}

		// If the stack will become too big, remove elements until it's the right size
		while (this.size() >= maxSize) {
			this.remove(0);
		}

		return super.push(object);
	}
}