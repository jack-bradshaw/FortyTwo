package com.matthewtamlin.multiplechoiceanswerview.library_tests.util;

import com.matthewtamlin.multiple_choice_answer_view.library.util.EvictingStackSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static com.matthewtamlin.multiple_choice_answer_view.library.util.EvictingStackSet.EvictionListener;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@RunWith(JUnit4.class)
public class TestEvictingStackSet {
	private EvictingStackSet.EvictionListener<Integer> listener1;

	private EvictingStackSet.EvictionListener<Integer> listener2;

	@SuppressWarnings("unchecked") // Not relevant for mocks
	@Before
	public void setup() {
		listener1 = mock(EvictionListener.class);
		listener2 = mock(EvictionListener.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor1_negativeSizeLimit() {
		new EvictingStackSet(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor1_zeroSizeLimit() {
		new EvictingStackSet(0);
	}

	@Test
	public void testConstructor1_positiveSizeLimit() {
		final EvictingStackSet evictingStackSet = new EvictingStackSet(10);

		assertThat("Max size was initialised incorrectly, or getter doesn't return correct value.",
				evictingStackSet.getMaxSize(), is(10));
		assertThat("Should be empty when initialised.", evictingStackSet.isEmpty(), is(true));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor2_negativeSizeLimit() {
		new EvictingStackSet<>(-1, new ArrayList<>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor2_zeroSizeLimit() {
		new EvictingStackSet<>(0, new ArrayList<>());
	}

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

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor2_nullContents() {
		new EvictingStackSet<>(1, null);
	}

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

	@Test
	public void testPush_capacityNotReached() {
		final int limit = 10;
		final int inserted = 9;

		final EvictingStackSet<Integer> evictingStackSet = new EvictingStackSet<>(limit);

		// Insert elements
		for (int i = 0; i < inserted; i++) {
			evictingStackSet.push(i);
		}

		// Check that all elements were retained
		for (int i = 0; i < inserted; i++) {
			final String message = String.format("\"%1$s\" should not have been evicted.", i);

			assertThat(message, evictingStackSet.contains(i), is(true));
		}
	}

	@Test
	public void testPush_capacityReachedButNotExceeded() {
		final int limit = 10;
		final int inserted = 10;

		final EvictingStackSet<Integer> evictingStackSet = new EvictingStackSet<>(limit);

		// Insert elements
		for (int i = 0; i < inserted; i++) {
			evictingStackSet.push(i);
		}

		// Check that all elements were retained
		for (int i = 0; i < inserted; i++) {
			final String message = String.format("\"%1$s\" should not have been evicted.", i);

			assertThat(message, evictingStackSet.contains(i), is(true));
		}
	}

	@Test
	public void testPush_capacityExceeded() {
		final int limit = 10;
		final int inserted = 15;

		final EvictingStackSet<Integer> evictingStackSet = new EvictingStackSet<>(limit);

		// Insert elements
		for (int i = 0; i < inserted; i++) {
			evictingStackSet.push(i);
		}

		// Check that the correct elements were evicted
		for (int i = 0; i < inserted - limit; i++) {
			final String message = String.format("\"%1$s\" should have been evicted.", i);

			assertThat(message, evictingStackSet.contains(i), is(false));
		}

		// Check that the correct elements were retained
		for (int i = inserted - limit; i < inserted; i++) {
			final String message = String.format("\"%1$s\" should not have been evicted.", i);

			assertThat(message, evictingStackSet.contains(i), is(true));
		}
	}

	private void registerListeners(final EvictingStackSet<Integer> evictingStackSet) {
		evictingStackSet.registerListener(listener1);
		evictingStackSet.registerListener(listener2);
		evictingStackSet.registerListener(null);
	}
}