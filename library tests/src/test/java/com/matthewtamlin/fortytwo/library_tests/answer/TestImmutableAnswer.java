package com.matthewtamlin.fortytwo.library_tests.answer;

import com.matthewtamlin.fortytwo.library.answer.ImmutableAnswer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Unit tests for the {@link ImmutableAnswer} class.
 */
@RunWith(JUnit4.class)
public class TestImmutableAnswer {
	/**
	 * Test to ensure the {@link ImmutableAnswer#ImmutableAnswer(CharSequence, boolean)}
	 * construction functions correctly when provided with null text. The test will only pass if the
	 * correct exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullText() {
		new ImmutableAnswer(null, true);
	}

	/**
	 * Test to ensure the {@link ImmutableAnswer#ImmutableAnswer(CharSequence, boolean)}
	 * construction functions correctly when provided with non-null text. The test will only pass if
	 * the getters return the values passed the constructor.
	 */
	@Test
	public void testConstructor_validArguments() {
		final ImmutableAnswer answer = new ImmutableAnswer("test", true);

		assertThat("getter returned wrong text.", answer.getText(), is((CharSequence) "test"));
		assertThat("getter returned wrong correctness", answer.isCorrect(), is(true));
	}
}