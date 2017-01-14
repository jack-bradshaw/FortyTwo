package com.matthewtamlin.multiple_choice_answer_view.library.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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
public class EvictingStackSet<T> extends Stack<T> implements Listenable<EvictingStackSet
		.EvictionListener<T>> {
	private final Set<EvictionListener<T>> listeners = new HashSet<>();

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
	 * 		the size limit after which elements will be evicted, greater than zero
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

	/**
	 * Sets the size limit of the stack. If the stack contains more that this many elements,
	 * elements will be evicted from the bottom until the stack reaches the new max size.
	 *
	 * @param maxSize
	 * 		the size limit to use, greater than zero
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
		while (this.size() >= maxSize) {
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

	public interface EvictionListener<V> {
		public void onEviction(EvictingStackSet<V> evictingStackSet, V evicted);
	}
}