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

package com.matthewtamlin.multiplechoiceanswerview.library_tests.answer;

import com.matthewtamlin.multiple_choice_answer_view.library.answer.PojoAnswer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Unit tests for the {@link PojoAnswer} class.
 */
@RunWith(JUnit4.class)
public class TestPojoAnswer {
	/**
	 * Test to ensure the {@link PojoAnswer#PojoAnswer(CharSequence, boolean)} construction
	 * functions correctly when provided with null text. The test will only pass if the correct
	 * exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullText() {
		new PojoAnswer(null, true);
	}

	/**
	 * Test to ensure the {@link PojoAnswer#PojoAnswer(CharSequence, boolean)} constructor functions
	 * correctly when provided with non-null text. The test will only pass if the getters return the
	 * values passed the constructor.
	 */
	@Test
	public void testConstructor_validArguments() {
		final PojoAnswer answer = new PojoAnswer("test", true);

		assertThat("getter returned wrong text.", answer.getText(), is((CharSequence) "test"));
		assertThat("getter returned wrong correctness", answer.isCorrect(), is(true));
	}

	/**
	 * Test to ensure the {@link PojoAnswer#setText(CharSequence)} method functions correctly when
	 * provided with null text. The test will only pass if the correct exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetText_nullText() {
		final PojoAnswer answer = new PojoAnswer("test", true);

		answer.setText(null);
	}

	/**
	 * Test to ensure the {@link PojoAnswer#setText(CharSequence)} method functions correctly when
	 * provided with non-null text. The test will only pass if the getter returns the new value.
	 */
	@Test
	public void testSetText_validArguments() {
		final PojoAnswer answer = new PojoAnswer("test", true);

		answer.setText("42");

		assertThat("getter returned wrong text.", answer.getText(), is((CharSequence) "42"));

		answer.setText("test");

		assertThat("getter returned wrong text.", answer.getText(), is((CharSequence) "test"));
	}

	/**
	 * Test to ensure the {@link PojoAnswer#setCorrectness(boolean)} (CharSequence)} method
	 * functions correctly. The test will only pass if the getter returns the new value.
	 */
	@Test
	public void testSetCorrectness() {
		final PojoAnswer answer = new PojoAnswer("test", true);

		answer.setCorrectness(false);

		assertThat("getter returned wrong correctness.", answer.isCorrect(), is(false));

		answer.setCorrectness(true);

		assertThat("getter returned wrong correctness.", answer.isCorrect(), is(true));
	}
}