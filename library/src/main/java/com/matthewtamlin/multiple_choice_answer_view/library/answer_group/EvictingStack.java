package com.matthewtamlin.multiple_choice_answer_view.library.answer_group;

import java.util.Collection;
import java.util.Stack;

import static com.matthewtamlin.java_utilities.checkers.IntChecker.checkGreaterThanOrEqualTo;

/**
 * A stack which is limited in size. If an item is pushed to the stack when it is at full capacity,
 * the item at the bottom of the stack is evicted to make room.
 *
 * @param <T>
 * 		the type of the elements contained in the stack
 */
public class EvictingStack<T> extends Stack<T> {
	/**
	 * When the size of the stack exceeds this value, the bottom element is evicted.
	 */
	private int maxSize;

	/**
	 * Constructs a new empty EvictingStack.
	 *
	 * @param maxSize
	 * 		the size limit after which elements will be evicted, greater than or equal to zero
	 */
	public EvictingStack(final int maxSize) {
		super();

		this.maxSize = checkGreaterThanOrEqualTo(maxSize, 0, "maxSize cannot be less than zero.");
	}

	/**
	 * Constructs a new EvictingStack from an existing collection. When the stack is populated,
	 * elements are added using the total ordering of the supplied collection. This will affect
	 * which elements end up in the stack if the supplied collection is larger than the stack size
	 * limit.
	 *
	 * @param maxSize
	 * 		the size limit after which elements will be evicted, greater than or equal to zero
	 * @param contents
	 * 		the elements to add to the stack
	 */
	public EvictingStack(final int maxSize, final Collection<? extends T> contents) {
		this.maxSize = checkGreaterThanOrEqualTo(maxSize, 0, "maxSize cannot be less than zero.");

		for (final T t : contents) {
			push(t);
		}
	}

	/**
	 * @return the size limit of the stack.
	 */
	public int getMaxSize() {
		return maxSize;
	}

	@Override
	public T push(final T object) {
		// If the stack will become too bug, remove elements until it's the right size
		while (this.size() + 1 >= maxSize) {
			this.remove(0);
		}

		return super.push(object);
	}
}