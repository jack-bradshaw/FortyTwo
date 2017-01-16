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

package com.matthewtamlin.responsum.library_tests.answer_view;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.matthewtamlin.responsum.library.answer.PojoAnswer;
import com.matthewtamlin.responsum.library.answer_view.AlphaDecorator;
import com.matthewtamlin.responsum.library.answer_view.AlphaDecorator.AlphaSupplier;
import com.matthewtamlin.responsum.library.answer_view.DecoratedAnswerCard;
import com.matthewtamlin.responsum.library_tests.DecoratedAnswerCardTestHarness;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.matthewtamlin.android_testing_tools.library.EspressoHelper.viewToViewInteraction;
import static com.matthewtamlin.responsum.library_tests.answer_view.DecoratedAnswerCardViewActions.addDecorator;
import static com.matthewtamlin.responsum.library_tests.answer_view.SimpleAnswerCardViewActions.setAnswer;
import static com.matthewtamlin.responsum.library_tests.answer_view.SimpleAnswerCardViewActions.setStatus;
import static com.matthewtamlin.responsum.library_tests.answer_view.SimpleAnswerCardViewAssertions.alpha;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

/**
 * Instrumentation tests for the {@link AlphaDecorator} class.
 */
public class TestAlphaDecorator {
	/**
	 * The tolerance to use for float equality when comparing expected alphas to actual alphas.
	 */
	private static final float FLOAT_EQUALITY_TOLERANCE = 0.05f;

	/**
	 * Supplies the alpha values to use in the tests.
	 */
	private static final AlphaSupplier ALPHA_SUPPLIER = new AlphaSupplier() {
		@Override
		public float getAlpha(final boolean marked, final boolean selected,
				final boolean answerIsCorrect) {
			if (marked) {
				if (selected) {
					return answerIsCorrect ? 0.1f : 0.2f;
				} else {
					return answerIsCorrect ? 0.3f : 0.4f;
				}
			} else {
				if (selected) {
					return answerIsCorrect ? 0.5f : 0.6f;
				} else {
					return answerIsCorrect ? 0.7f : 0.8f;
				}
			}
		}
	};

	/**
	 * Rule to always launch the DecoratedAnswerCardTestHarness before running the tests. This rule
	 * allows an actual instance of a DecoratedAnswerCard to be used in testing.
	 */
	@Rule
	public final ActivityTestRule<DecoratedAnswerCardTestHarness> rule = new
			ActivityTestRule<>(DecoratedAnswerCardTestHarness.class);

	/**
	 * A ViewInteractor which allows a DecoratedAnswerCard to be used with the Espresso framework.
	 */
	private ViewInteraction decoratedAnswerCardEspresso;

	/**
	 * The alpha decorated under test.
	 */
	private AlphaDecorator alphaDecorator;

	/**
	 * Performs initialisation before the test cases run.
	 */
	@Before
	public void setup() {
		final DecoratedAnswerCard answerCardDirect = rule.getActivity().getTestView();
		decoratedAnswerCardEspresso = viewToViewInteraction(answerCardDirect);

		alphaDecorator = new AlphaDecorator(ALPHA_SUPPLIER);
	}

	/**
	 * Test to ensure that the {@link AlphaDecorator#decorate(DecoratedAnswerCard, boolean)} method
	 * functions correctly when the target answer card is unmarked, deselected and incorrect. This
	 * test examines the case where animations are enabled. The test will only pass if the card
	 * alpha is changed to the expected value.
	 */
	@Test
	public void testUnmarkedDeselectedAndIncorrect_usingAnimation() {
		decoratedAnswerCardEspresso.perform(setAnswer(new PojoAnswer("test", false), true));
		decoratedAnswerCardEspresso.perform(setStatus(false, false, true));

		decoratedAnswerCardEspresso.perform(addDecorator(alphaDecorator, true));

		final float expectedAlpha = ALPHA_SUPPLIER.getAlpha(false, false, false);

		decoratedAnswerCardEspresso.check(alpha(is(both(
				greaterThan(expectedAlpha - FLOAT_EQUALITY_TOLERANCE))
				.and(lessThan(expectedAlpha + FLOAT_EQUALITY_TOLERANCE)))));
	}

	/**
	 * Test to ensure that the {@link AlphaDecorator#decorate(DecoratedAnswerCard, boolean)} method
	 * functions correctly when the target answer card is unmarked, deselected and incorrect. This
	 * test examines the case where animations are disabled. The test will only pass if the card
	 * alpha is changed to the expected value.
	 */
	@Test
	public void testUnmarkedDeselectedAndIncorrect_noAnimation() {
		decoratedAnswerCardEspresso.perform(setAnswer(new PojoAnswer("test", false), false));
		decoratedAnswerCardEspresso.perform(setStatus(false, false, false));

		decoratedAnswerCardEspresso.perform(addDecorator(alphaDecorator, false));

		final float expectedAlpha = ALPHA_SUPPLIER.getAlpha(false, false, false);

		decoratedAnswerCardEspresso.check(alpha(is(both(
				greaterThan(expectedAlpha - FLOAT_EQUALITY_TOLERANCE))
				.and(lessThan(expectedAlpha + FLOAT_EQUALITY_TOLERANCE)))));
	}

	/**
	 * Test to ensure that the {@link AlphaDecorator#decorate(DecoratedAnswerCard, boolean)} method
	 * functions correctly when the target answer card is marked, selected and correct. This test
	 * examines the case where animations are enabled. The test will only pass if the card alpha is
	 * changed to the expected value.
	 */
	@Test
	public void testMarkedSelectedAndCorrect_usingAnimation() {
		decoratedAnswerCardEspresso.perform(setAnswer(new PojoAnswer("test", true), true));
		decoratedAnswerCardEspresso.perform(setStatus(true, true, true));

		decoratedAnswerCardEspresso.perform(addDecorator(alphaDecorator, true));

		final float expectedAlpha = ALPHA_SUPPLIER.getAlpha(true, true, true);

		decoratedAnswerCardEspresso.check(alpha(is(both(
				greaterThan(expectedAlpha - FLOAT_EQUALITY_TOLERANCE))
				.and(lessThan(expectedAlpha + FLOAT_EQUALITY_TOLERANCE)))));
	}

	/**
	 * Test to ensure that the {@link AlphaDecorator#decorate(DecoratedAnswerCard, boolean)} method
	 * functions correctly when the target answer card is marked, selected and correct. This test
	 * examines the case where animations are disabled. The test will only pass if the card alpha is
	 * changed to the expected value.
	 */
	@Test
	public void testMarkedSelectedAndCorrect_noAnimation() {
		decoratedAnswerCardEspresso.perform(setAnswer(new PojoAnswer("test", true), false));
		decoratedAnswerCardEspresso.perform(setStatus(true, true, false));

		decoratedAnswerCardEspresso.perform(addDecorator(alphaDecorator, false));

		final float expectedAlpha = ALPHA_SUPPLIER.getAlpha(true, true, true);

		decoratedAnswerCardEspresso.check(alpha(is(both(
				greaterThan(expectedAlpha - FLOAT_EQUALITY_TOLERANCE))
				.and(lessThan(expectedAlpha + FLOAT_EQUALITY_TOLERANCE)))));
	}
}