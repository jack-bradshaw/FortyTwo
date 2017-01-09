package com.matthewtamlin.multiplechoiceanswerview.library_tests.answer.answer;

import com.matthewtamlin.multiple_choice_answer_view.library.answer.SimpleAnswer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(JUnit4.class)
public class TestSimpleAnswer {
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullText() {
		new SimpleAnswer(null, true);
	}

	@Test
	public void testConstructor_validArguments() {
		final SimpleAnswer answer = new SimpleAnswer("test", true);

		assertThat("getter returned wrong text.", answer.getText(), is((CharSequence) "test"));
		assertThat("getter returned wrong correctness", answer.isCorrect(), is(true));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetText_nullText() {
		final SimpleAnswer answer = new SimpleAnswer("test", true);

		answer.setText(null);
	}

	@Test
	public void testSetText_validArguments() {
		final SimpleAnswer answer = new SimpleAnswer("test", true);

		answer.setText("42");

		assertThat("getter returned wrong text.", answer.getText(), is((CharSequence) "42"));

		answer.setText("test");

		assertThat("getter returned wrong text.", answer.getText(), is((CharSequence) "test"));
	}

	@Test
	public void testSetCorrectness() {
		final SimpleAnswer answer = new SimpleAnswer("test", true);

		answer.setCorrectness(false);

		assertThat("getter returned wrong correctness.", answer.isCorrect(), is(false));

		answer.setCorrectness(true);

		assertThat("getter returned wrong correctness.", answer.isCorrect(), is(true));
	}
}