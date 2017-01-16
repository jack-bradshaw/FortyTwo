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

package com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_view;

import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;

import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.SimpleAnswerCard;
import com.matthewtamlin.multiple_choice_answer_view.library.answer.Answer;
import com.matthewtamlin.multiple_choice_answer_view.library.answer.PojoAnswer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.matthewtamlin.android_testing_tools.library.EspressoHelper.viewToViewInteraction;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_view.SimpleAnswerCardViewActions.setAnimationDurationMs;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_view.SimpleAnswerCardViewActions.setAnswer;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_view.SimpleAnswerCardViewActions.setIdentifier;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_view.SimpleAnswerCardViewActions.setStatus;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_view.SimpleAnswerCardViewAssertions.animationDuration;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_view.SimpleAnswerCardViewAssertions.status;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.util.TextViewViewAssertions.hasText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;

/**
 * Instrumentation tests for the {@link SimpleAnswerCard} class. This class is abstract because the
 * view under test is abstract.
 */
@RunWith(AndroidJUnit4.class)
public abstract class TestSimpleAnswerCard {
	/**
	 * The view being tested, as a ViewInteractor.
	 */
	private ViewInteraction simpleAnswerCardEspresso;

	/**
	 * The answer container of the view under test, as a view interactor.
	 */
	private ViewInteraction answerContainerEspresso;

	/**
	 * The identifier container of the view under test, as a view interactor.
	 */
	private ViewInteraction identifierContainerEspresso;

	/**
	 * @return the view under test, not null
	 */
	public abstract SimpleAnswerCard getSimpleAnswerCardView();

	/**
	 * @return the view under test, as a view interaction
	 */
	public ViewInteraction getSimpleAnswerCardViewInteraction() {
		return simpleAnswerCardEspresso;
	}

	/**
	 * Performs initialisation before the tests cases run.
	 */
	@Before
	public void setup() {
		final SimpleAnswerCard simpleAnswerCardDirect = getSimpleAnswerCardView();

		simpleAnswerCardEspresso = viewToViewInteraction(simpleAnswerCardDirect, "1");
		answerContainerEspresso = viewToViewInteraction(simpleAnswerCardDirect.getAnswerContainer
				(), "2");
		identifierContainerEspresso = viewToViewInteraction(simpleAnswerCardDirect
				.getIdentifierContainer(), "3");
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setAnimationDurationMs(int)} method functions
	 * correctly when a negative duration is passed. The test will only pass if the correct
	 * exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetAnimationDuration_negativeDuration() {
		simpleAnswerCardEspresso.perform(setAnimationDurationMs(-1));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setAnimationDurationMs(int)} method functions
	 * correctly when a duration of 0 is passed. The test will only pass if the getter for the
	 * property returns 0.
	 */
	@Test
	public void testSetAnimationDuration_zeroDuration() {
		simpleAnswerCardEspresso.perform(setAnimationDurationMs(0));
		simpleAnswerCardEspresso.check(animationDuration(is(0)));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setAnimationDurationMs(int)} method functions
	 * correctly when a positive duration is passed. The test will only pass if the getter for the
	 * property returns the new value.
	 */
	@Test
	public void testSetAnimationDuration_positiveDuration() {
		simpleAnswerCardEspresso.perform(setAnimationDurationMs(1));
		simpleAnswerCardEspresso.check(animationDuration(is(1)));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setStatus(boolean, boolean, boolean)} method functions
	 * correctly. This test examines the case where both marked and selected are false, and
	 * animations are enabled. The test will only pass if the status is updated correctly.
	 */
	@Test
	public void testSetStatus_neitherMarkedNorSelected_usingAnimation() {
		simpleAnswerCardEspresso.perform(setStatus(false, false, true));
		simpleAnswerCardEspresso.check(status(is(false), is(false)));

		// Make sure a second call doesn't change anything
		simpleAnswerCardEspresso.perform(setStatus(false, false, true));
		simpleAnswerCardEspresso.check(status(is(false), is(false)));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setStatus(boolean, boolean, boolean)} method functions
	 * correctly. This test examines the case where both marked and selected are false, and
	 * animations are disabled. The test will only pass if the status is updated correctly.
	 */
	@Test
	public void testSetStatus_neitherMarkedNorSelected_noAnimation() {
		simpleAnswerCardEspresso.perform(setStatus(false, false, false));
		simpleAnswerCardEspresso.check(status(is(false), is(false)));

		// Make sure a second call doesn't change anything
		simpleAnswerCardEspresso.perform(setStatus(false, false, false));
		simpleAnswerCardEspresso.check(status(is(false), is(false)));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setStatus(boolean, boolean, boolean)} method functions
	 * correctly. This test examines the case where marked is true, selected is false, and
	 * animations are enabled. The test will only pass if the status is updated correctly.
	 */
	@Test
	public void testSetStatus_markedOnly_usingAnimation() {
		simpleAnswerCardEspresso.perform(setStatus(true, false, true));
		simpleAnswerCardEspresso.check(status(is(true), is(false)));

		// Make sure a second call doesn't change anything
		simpleAnswerCardEspresso.perform(setStatus(true, false, true));
		simpleAnswerCardEspresso.check(status(is(true), is(false)));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setStatus(boolean, boolean, boolean)} method functions
	 * correctly. This test examines the case where marked is true, selected is false, and
	 * animations are disabled. The test will only pass if the status is updated correctly.
	 */
	@Test
	public void testSetStatus_markedOnly_noAnimation() {
		simpleAnswerCardEspresso.perform(setStatus(true, false, false));
		simpleAnswerCardEspresso.check(status(is(true), is(false)));

		// Make sure a second call doesn't change anything
		simpleAnswerCardEspresso.perform(setStatus(true, false, false));
		simpleAnswerCardEspresso.check(status(is(true), is(false)));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setStatus(boolean, boolean, boolean)} method functions
	 * correctly. This test examines the case where marked is false, selected is true, and
	 * animations are enabled. The test will only pass if the status is updated correctly.
	 */
	@Test
	public void testSetStatus_selectedOnly_usingAnimation() {
		simpleAnswerCardEspresso.perform(setStatus(false, true, true));
		simpleAnswerCardEspresso.check(status(is(false), is(true)));

		// Make sure a second call doesn't change anything
		simpleAnswerCardEspresso.perform(setStatus(false, true, true));
		simpleAnswerCardEspresso.check(status(is(false), is(true)));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setStatus(boolean, boolean, boolean)} method functions
	 * correctly. This test examines the case where marked is false, selected is true, and
	 * animations are disabled. The test will only pass if the status is updated correctly.
	 */
	@Test
	public void testSetStatus_selectedOnly_noAnimation() {
		simpleAnswerCardEspresso.perform(setStatus(false, true, false));
		simpleAnswerCardEspresso.check(status(is(false), is(true)));

		// Make sure a second call doesn't change anything
		simpleAnswerCardEspresso.perform(setStatus(false, true, false));
		simpleAnswerCardEspresso.check(status(is(false), is(true)));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setStatus(boolean, boolean, boolean)} method functions
	 * correctly. This test examines the case where marked and selected are both true, and
	 * animations are enabled. The test will only pass if the status is updated correctly.
	 */
	@Test
	public void testSetStatus_markedAndSelected_usingAnimation() {
		simpleAnswerCardEspresso.perform(setStatus(true, true, true));
		simpleAnswerCardEspresso.check(status(is(true), is(true)));

		// Make sure a second call doesn't change anything
		simpleAnswerCardEspresso.perform(setStatus(true, true, true));
		simpleAnswerCardEspresso.check(status(is(true), is(true)));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setStatus(boolean, boolean, boolean)} method functions
	 * correctly. This test examines the case where marked and selected are both true, and
	 * animations are disabled. The test will only pass if the status is updated correctly.
	 */
	@Test
	public void testSetStatus_markedAndSelected_noAnimation() {
		simpleAnswerCardEspresso.perform(setStatus(true, true, false));
		simpleAnswerCardEspresso.check(status(is(true), is(true)));

		// Make sure a second call doesn't change anything
		simpleAnswerCardEspresso.perform(setStatus(true, true, false));
		simpleAnswerCardEspresso.check(status(is(true), is(true)));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setAnswer(Answer, boolean)} method functions
	 * correctly. This test examines multiple calls with multiple combinations of variables. In each
	 * call animations are enabled. The test will only pass if the answer is updated correctly each
	 * time the method is called.
	 */
	@Test
	public void testSetAnswer_multipleCalls_usingAnimation() {
		simpleAnswerCardEspresso.perform(setAnswer(null, true));
		answerContainerEspresso.check(hasText(isEmptyOrNullString()));

		simpleAnswerCardEspresso.perform(setAnswer(new PojoAnswer( "test 1", false), true));
		answerContainerEspresso.check(hasText(is("test 1")));

		simpleAnswerCardEspresso.perform(setAnswer(null, true));
		answerContainerEspresso.check(hasText(isEmptyOrNullString()));

		simpleAnswerCardEspresso.perform(setAnswer(new PojoAnswer("test 2", false), true));
		answerContainerEspresso.check(hasText(is("test 2")));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setAnswer(Answer, boolean)} method functions
	 * correctly. This test examines multiple calls with multiple combinations of variables. In each
	 * call animations are disabled. The test will only pass if the answer is updated correctly each
	 * time the method is called.
	 */
	@Test
	public void testSetAnswer_multipleCalls_noAnimation() {
		simpleAnswerCardEspresso.perform(setAnswer(null, false));
		answerContainerEspresso.check(hasText(isEmptyOrNullString()));

		simpleAnswerCardEspresso.perform(setAnswer(new PojoAnswer("test 1", false), false));
		answerContainerEspresso.check(hasText(is("test 1")));

		simpleAnswerCardEspresso.perform(setAnswer(null, false));
		answerContainerEspresso.check(hasText(isEmptyOrNullString()));

		simpleAnswerCardEspresso.perform(setAnswer(new PojoAnswer("test 2", false), false));
		answerContainerEspresso.check(hasText(is("test 2")));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setIdentifier(CharSequence, boolean)} method functions
	 * correctly. This test examines multiple calls with different indicators. In each call
	 * animations are enabled. The test will only pass if the identifier is updated correctly each
	 * time the method is called.
	 */
	@Test
	public void testSetIdentifier_multipleCalls_usingAnimation() {
		simpleAnswerCardEspresso.perform(setIdentifier(null, true));
		identifierContainerEspresso.check(hasText(isEmptyOrNullString()));

		simpleAnswerCardEspresso.perform(setIdentifier("test 1", true));
		identifierContainerEspresso.check(hasText(is("test 1")));

		simpleAnswerCardEspresso.perform(setIdentifier(null, true));
		identifierContainerEspresso.check(hasText(isEmptyOrNullString()));

		simpleAnswerCardEspresso.perform(setIdentifier("test 2", true));
		identifierContainerEspresso.check(hasText(is("test 2")));
	}

	/**
	 * Tests that the {@link SimpleAnswerCard#setIdentifier(CharSequence, boolean)} method functions
	 * correctly. This test examines multiple calls with different indicators. In each call
	 * animations are disabled. The test will only pass if the identifier is updated correctly each
	 * time the method is called.
	 */
	@Test
	public void testSetIndicator_multipleCalls_noAnimation() {
		simpleAnswerCardEspresso.perform(setIdentifier(null, false));
		identifierContainerEspresso.check(hasText(isEmptyOrNullString()));

		simpleAnswerCardEspresso.perform(setIdentifier("test 1", false));
		identifierContainerEspresso.check(hasText(is("test 1")));

		simpleAnswerCardEspresso.perform(setIdentifier(null, false));
		identifierContainerEspresso.check(hasText(isEmptyOrNullString()));

		simpleAnswerCardEspresso.perform(setIdentifier("test 2", false));
		identifierContainerEspresso.check(hasText(is("test 2")));
	}
}