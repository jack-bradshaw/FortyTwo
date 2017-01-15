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

package com.matthewtamlin.multiplechoiceanswerview.library_tests.util;

import com.matthewtamlin.multiple_choice_answer_view.library.util.EvictingStackSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collection;

import static com.matthewtamlin.multiple_choice_answer_view.library.util.EvictingStackSet.EvictionListener;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit test for the {@link EvictingStackSet} class.
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@RunWith(JUnit4.class)
public class TestEvictingStackSet {
	/**
	 * A mock listener.
	 */
	private EvictingStackSet.EvictionListener<Integer> listener1;

	/**
	 * Another mock listener.
	 */
	private EvictingStackSet.EvictionListener<Integer> listener2;

	/**
	 * Performs initialisation before the test run by creating the mock listeners.
	 */
	@SuppressWarnings("unchecked") // Not relevant for mocks
	@Before
	public void setup() {
		listener1 = mock(EvictionListener.class);
		listener2 = mock(EvictionListener.class);
	}

	/**
	 * Test to ensure that the {@link EvictingStackSet#EvictingStackSet(int)} constructor functions
	 * correctly when provided with a negative max size. The test will only pass if the correct
	 * exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor1_negativeSizeLimit() {
		new EvictingStackSet(-1);
	}

	/**
	 * Test to ensure that the {@link EvictingStackSet#EvictingStackSet(int)} constructor functions
	 * correctly when provided with a max size if zero. The test will only pass if the correct
	 * exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor1_zeroSizeLimit() {
		new EvictingStackSet(0);
	}

	/**
	 * Test to ensure that the {@link EvictingStackSet#EvictingStackSet(int)} constructor functions
	 * correctly when provided with a positive max size. The test will only pass if the getter
	 * returns the correct max size value. Furthermore, the stack set must be empty after
	 * construction.
	 */
	@Test
	public void testConstructor1_positiveSizeLimit() {
		final EvictingStackSet evictingStackSet = new EvictingStackSet(10);

		assertThat("Max size was initialised incorrectly, or getter doesn't return correct value.",
				evictingStackSet.getMaxSize(), is(10));
		assertThat("Should be empty when initialised.", evictingStackSet.isEmpty(), is(true));
	}

	/**
	 * Test to ensure that the {@link EvictingStackSet#EvictingStackSet(int, Collection)}
	 * constructor functions correctly when provided with a negative max size. The test will only
	 * pass if the correct exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor2_negativeSizeLimit() {
		new EvictingStackSet<>(-1, new ArrayList<>());
	}

	/**
	 * Test to ensure that the {@link EvictingStackSet#EvictingStackSet(int, Collection)}
	 * constructor functions correctly when provided with a max size if zero. The test will only
	 * pass if the correct exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor2_zeroSizeLimit() {
		new EvictingStackSet<>(0, new ArrayList<>());
	}

	/**
	 * Test to ensure that the {@link EvictingStackSet#EvictingStackSet(int, Collection)}
	 * constructor functions correctly when provided with a positive max size. The test will only
	 * pass if the getter returns the correct max size value. Furthermore, the stack set must be
	 * empty after construction.
	 */
	@Test
	public void testConstructor2_positiveSizeLimit() {
		final ArrayList<String> contents = new ArrayList<>();
		contents.add("Test1");
		contents.add("Test2");

		final EvictingStackSet<String> evictingStackSet = new EvictingStackSet<>(10, contents);

		assertThat("Max size was initialised incorrectly, or getter doesn't return correct value.",
				evictingStackSet.getMaxSize(), is(10));
		assertThat("Should contain all elements in passed collection.",
				evictingStackSet.size(), is(contents.size()));

		for (final String s : contents) {
			assertThat("Element of contents is not contained.", evictingStackSet.contains(s), is
					(true));
		}
	}

	/**
	 * Test to ensure that the {@link EvictingStackSet#EvictingStackSet(int)} (int)} constructor
	 * functions correctly when provided with a collection which contains zero. The test will only
	 * pass if the correct exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor2_nullContents() {
		new EvictingStackSet<>(1, null);
	}

	/**
	 * Test to ensure that the {@link EvictingStackSet#setMaxSize(int)} method functions correctly
	 * when provided with a value which is smaller than the current number of elements. The test
	 * will only pass if the correct elements are evicted and the correct callbacks are delivered.
	 */
	@Test
	public void testSetMaxSize_newMaxSmallerThanCurrentSize() {
		final int initialLimit = 10;
		final int newLimit = 5;
		final int inserted = 7;

		final EvictingStackSet<Integer> evictingStackSet = new EvictingStackSet<>(initialLimit);
		registerListeners(evictingStackSet);

		for (int i = 0; i < inserted; i++) {
			evictingStackSet.push(i);
		}

		assertThat(inserted + " elements should be in the collection initially.",
				evictingStackSet.size(), is(inserted));

		evictingStackSet.setMaxSize(newLimit);

		assertThat("Size should match the new limit.", evictingStackSet.size(), is(newLimit));

		for (int i = 0; i < inserted - newLimit; i++) {
			verify(listener1, times(1)).onEviction(evictingStackSet, i);
			verify(listener2, times(1)).onEviction(evictingStackSet, i);

			assertThat("Elements which should have been evicted are still contained.",
					evictingStackSet.contains(i), is(false));
		}

		for (int i = inserted - newLimit; i < inserted; i++) {
			verify(listener1, never()).onEviction(evictingStackSet, i);
			verify(listener2, never()).onEviction(evictingStackSet, i);

			assertThat("Elements which should still be contained were evicted.",
					evictingStackSet.contains(i), is(true));
		}
	}

	/**
	 * Test to ensure that the {@link EvictingStackSet#setMaxSize(int)} method functions correctly
	 * when provided with a value which is equal to the current number of elements. The test will
	 * only pass if no elements are evicted and no callbacks are delivered.
	 */
	@Test
	public void testSetMaxSize_newMaxEqualToCurrentSize() {
		final int initialLimit = 10;
		final int newLimit = 7;
		final int inserted = 7;

		final EvictingStackSet<Integer> evictingStackSet = new EvictingStackSet<>(initialLimit);
		registerListeners(evictingStackSet);

		for (int i = 0; i < inserted; i++) {
			evictingStackSet.push(i);
		}

		assertThat(inserted + " elements should be in the collection initially.",
				evictingStackSet.size(), is(inserted));

		evictingStackSet.setMaxSize(newLimit);

		assertThat("Size should match the new limit.", evictingStackSet.size(), is(newLimit));

		for (int i = 0; i < inserted; i++) {
			verify(listener1, never()).onEviction(evictingStackSet, i);
			verify(listener2, never()).onEviction(evictingStackSet, i);

			assertThat("Elements which should still be contained were evicted.",
					evictingStackSet.contains(i), is(true));
		}
	}

	/**
	 * Test to ensure that the {@link EvictingStackSet#setMaxSize(int)} method functions correctly
	 * when provided with a value which is greater than the current number of elements. The test
	 * will only pass if no elements are evicted and no callbacks are delivered.
	 */
	@Test
	public void testSetMaxSize_newMaxGreaterThanCurrentSize() {
		final int initialLimit = 10;
		final int newLimit = 12;
		final int inserted = 7;

		final EvictingStackSet<Integer> evictingStackSet = new EvictingStackSet<>(initialLimit);
		registerListeners(evictingStackSet);

		for (int i = 0; i < inserted; i++) {
			evictingStackSet.push(i);
		}

		assertThat(inserted + " elements should be in the collection initially.",
				evictingStackSet.size(), is(inserted));

		evictingStackSet.setMaxSize(newLimit);

		assertThat(inserted + " elements should still be in the collection after setting the size.",
				evictingStackSet.size(), is(inserted));

		for (int i = 0; i < inserted; i++) {
			verify(listener1, never()).onEviction(evictingStackSet, i);
			verify(listener2, never()).onEviction(evictingStackSet, i);

			assertThat("Elements which should still be contained were evicted.",
					evictingStackSet.contains(i), is(true));
		}
	}

	/**
	 * Test to ensure that the {@link EvictingStackSet#push(Object)} method functions correctly when
	 * pushing a new element does not cause the capacity to be reached or exceeded. The test will
	 * only pass if no elements are evicted and no callbacks are delivered.
	 */
	@Test
	public void testPush_capacityNotReached() {
		final int limit = 10;
		final int inserted = 9;

		final EvictingStackSet<Integer> evictingStackSet = new EvictingStackSet<>(limit);
		registerListeners(evictingStackSet);

		// Insert elements
		for (int i = 0; i < inserted; i++) {
			evictingStackSet.push(i);
		}

		// Check that all elements were retained
		for (int i = 0; i < inserted; i++) {
			final String message = String.format("\"%1$s\" should not have been evicted.", i);

			assertThat(message, evictingStackSet.contains(i), is(true));
		}

		verify(listener1, never()).onEviction(eq(evictingStackSet), anyInt());
		verify(listener2, never()).onEviction(eq(evictingStackSet), anyInt());
	}

	/**
	 * Test to ensure that the {@link EvictingStackSet#push(Object)} method functions correctly when
	 * pushing a new element causes the capacity to be reached but not exceeded. The test will only
	 * pass if no elements are evicted and no callbacks are delivered.
	 */
	@Test
	public void testPush_capacityReachedButNotExceeded() {
		final int limit = 10;
		final int inserted = 10;

		final EvictingStackSet<Integer> evictingStackSet = new EvictingStackSet<>(limit);
		registerListeners(evictingStackSet);

		// Insert elements
		for (int i = 0; i < inserted; i++) {
			evictingStackSet.push(i);
		}

		// Check that all elements were retained
		for (int i = 0; i < inserted; i++) {
			final String message = String.format("\"%1$s\" should not have been evicted.", i);

			assertThat(message, evictingStackSet.contains(i), is(true));
		}

		verify(listener1, never()).onEviction(eq(evictingStackSet), anyInt());
		verify(listener2, never()).onEviction(eq(evictingStackSet), anyInt());
	}

	/**
	 * Test to ensure that the {@link EvictingStackSet#push(Object)} method functions correctly when
	 * pushing a new element causes the capacity to be exceeded. The test will only pass if the
	 * correct elements are evicted and the correct callbacks are delivered.
	 */
	@Test
	public void testPush_capacityExceeded() {
		final int limit = 10;
		final int inserted = 15;

		final EvictingStackSet<Integer> evictingStackSet = new EvictingStackSet<>(limit);
		registerListeners(evictingStackSet);

		// Insert elements
		for (int i = 0; i < inserted; i++) {
			evictingStackSet.push(i);
		}

		// Check that the correct elements were evicted
		for (int i = 0; i < inserted - limit; i++) {
			final String message = String.format("\"%1$s\" should have been evicted.", i);

			assertThat(message, evictingStackSet.contains(i), is(false));

			verify(listener1, times(1)).onEviction(evictingStackSet, i);
			verify(listener2, times(1)).onEviction(evictingStackSet, i);
		}

		// Check that the correct elements were retained
		for (int i = inserted - limit; i < inserted; i++) {
			final String message = String.format("\"%1$s\" should not have been evicted.", i);

			assertThat(message, evictingStackSet.contains(i), is(true));

			verify(listener1, never()).onEviction(evictingStackSet, i);
			verify(listener2, never()).onEviction(evictingStackSet, i);
		}
	}

	/**
	 * Registers the mock listeners as well as a null listener to the supplied EvictingStackSet.
	 *
	 * @param evictingStackSet
	 * 		the stack set to register the listeners to, not null
	 */
	private void registerListeners(final EvictingStackSet<Integer> evictingStackSet) {
		evictingStackSet.registerListener(listener1);
		evictingStackSet.registerListener(listener2);
		evictingStackSet.registerListener(null); // To check null safety
	}
}