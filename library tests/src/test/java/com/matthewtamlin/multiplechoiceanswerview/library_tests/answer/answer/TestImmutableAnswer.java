package com.matthewtamlin.multiplechoiceanswerview.library_tests.answer.answer;

import com.matthewtamlin.multiple_choice_answer_view.library.answer.ImmutableAnswer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(JUnit4.class)
public class TestImmutableAnswer {
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullText() {
		new ImmutableAnswer(null, true);
	}

	@Test
	public void testConstructor_validArguments() {
		final ImmutableAnswer answer = new ImmutableAnswer("test", true);

		assertThat("getter returned wrong text.", answer.getText(), is((CharSequence) "test"));
		assertThat("getter returned wrong correctness", answer.isCorrect(), is(true));
	}
}