package com.matthewtamlin.multiplechoiceanswerview.library_tests.answer.answer.util;

import com.matthewtamlin.multiple_choice_answer_view.library.util.EvictingStackSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class TestEvictingStackSet {
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
		new EvictingStackSet(1);
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
		new EvictingStackSet<>(1, new ArrayList<>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor2_nullContents() {
		new EvictingStackSet<>(1, null);
	}

	@Test
	public void testGetSizeLimit() {
		final EvictingStackSet<Object> evictingStackSet = new EvictingStackSet<Object>(10);

		assertThat("Getter returned wrong max size.", evictingStackSet.getMaxSize(), is(10));
	}

	@Test
	public void testPush_capacityNotReached() {
		final int limit = 10;
		final int inserted = 9;

		final EvictingStackSet<Integer> evictingStackSet = new EvictingStackSet<Integer>(limit);

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

		final EvictingStackSet<Integer> evictingStackSet = new EvictingStackSet<Integer>(limit);

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

		final EvictingStackSet<Integer> evictingStackSet = new EvictingStackSet<Integer>(limit);

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
}