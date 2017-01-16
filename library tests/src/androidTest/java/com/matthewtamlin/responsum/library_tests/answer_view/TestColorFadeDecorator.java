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

import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.matthewtamlin.responsum.library.answer.PojoAnswer;
import com.matthewtamlin.responsum.library.answer_view.ColorFadeDecorator;
import com.matthewtamlin.responsum.library.answer_view.ColorFadeDecorator.ColorSupplier;
import com.matthewtamlin.responsum.library.answer_view.DecoratedAnswerCard;
import com.matthewtamlin.responsum.library_tests.DecoratedAnswerCardTestHarness;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.matthewtamlin.android_testing_tools.library.EspressoHelper.viewToViewInteraction;
import static com.matthewtamlin.responsum.library_tests.answer_view.DecoratedAnswerCardViewActions.addDecorator;
import static com.matthewtamlin.responsum.library_tests.answer_view.SimpleAnswerCardViewActions.setAnswer;
import static com.matthewtamlin.responsum.library_tests.answer_view.SimpleAnswerCardViewActions.setStatus;
import static com.matthewtamlin.responsum.library_tests.answer_view.SimpleAnswerCardViewAssertions.cardBackgroundColor;
import static org.hamcrest.Matchers.is;

/**
 * Instrumentation tests for the {@link ColorFadeDecorator} class.
 */
public class TestColorFadeDecorator {
	/**
	 * Supplies the color hex codes to use in the tests.
	 */
	private static final ColorSupplier COLOR_SUPPLIER = new ColorSupplier() {
		@Override
		public int getColor(final boolean marked, final boolean selected,
				final boolean answerIsCorrect) {
			if (marked) {
				if (selected) {
					return answerIsCorrect ? Color.RED : Color.GREEN;
				} else {
					return answerIsCorrect ? Color.BLACK : Color.BLUE;
				}
			} else {
				if (selected) {
					return answerIsCorrect ? Color.CYAN : Color.GRAY;
				} else {
					return answerIsCorrect ? Color.MAGENTA : Color.YELLOW;
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
	 * The color fade decorated under test.
	 */
	private ColorFadeDecorator colorFadeDecorator;

	/**
	 * Performs initialisation before the test cases run.
	 */
	@Before
	public void setup() {
		final DecoratedAnswerCard answerCardDirect = rule.getActivity().getTestView();
		decoratedAnswerCardEspresso = viewToViewInteraction(answerCardDirect);

		colorFadeDecorator = new ColorFadeDecorator(COLOR_SUPPLIER);
	}

	/**
	 * Test to ensure that the {@link ColorFadeDecorator#decorate(DecoratedAnswerCard, boolean)}
	 * method functions correctly when the target answer card is unmarked, deselected and incorrect.
	 * This test examines the case where animations are enabled. The test will only pass if the card
	 * background color is changed to the expected value.
	 */
	@Test
	public void testUnmarkedDeselectedAndIncorrect_usingAnimation() {
		decoratedAnswerCardEspresso.perform(setAnswer(new PojoAnswer("test", false), true));
		decoratedAnswerCardEspresso.perform(setStatus(false, false, true));

		decoratedAnswerCardEspresso.perform(addDecorator(colorFadeDecorator, true));

		final int expectedColor = COLOR_SUPPLIER.getColor(false, false, false);

		decoratedAnswerCardEspresso.check(cardBackgroundColor(is(expectedColor)));
	}

	/**
	 * Test to ensure that the {@link ColorFadeDecorator#decorate(DecoratedAnswerCard, boolean)}
	 * method functions correctly when the target answer card is unmarked, deselected and incorrect.
	 * This test examines the case where animations are disabled. The test will only pass if the
	 * card background color is changed to the expected value.
	 */
	@Test
	public void testUnmarkedDeselectedAndIncorrect_noAnimation() {
		decoratedAnswerCardEspresso.perform(setAnswer(new PojoAnswer("test", false), false));
		decoratedAnswerCardEspresso.perform(setStatus(false, false, false));

		decoratedAnswerCardEspresso.perform(addDecorator(colorFadeDecorator, false));

		final int expectedColor = COLOR_SUPPLIER.getColor(false, false, false);

		decoratedAnswerCardEspresso.check(cardBackgroundColor(is(expectedColor)));
	}

	/**
	 * Test to ensure that the {@link ColorFadeDecorator#decorate(DecoratedAnswerCard, boolean)}
	 * method functions correctly when the target answer card is marked, selected and correct. This
	 * test examines the case where animations are enabled. The test will only pass if the card
	 * background color is changed to the expected value.
	 */
	@Test
	public void testMarkedSelectedAndCorrect_usingAnimation() {
		decoratedAnswerCardEspresso.perform(setAnswer(new PojoAnswer("test", true), true));
		decoratedAnswerCardEspresso.perform(setStatus(true, true, true));

		decoratedAnswerCardEspresso.perform(addDecorator(colorFadeDecorator, true));

		final int expectedColor = COLOR_SUPPLIER.getColor(true, true, true);

		decoratedAnswerCardEspresso.check(cardBackgroundColor(is(expectedColor)));
	}

	/**
	 * Test to ensure that the {@link ColorFadeDecorator#decorate(DecoratedAnswerCard, boolean)}
	 * method functions correctly when the target answer card is marked, selected and correct. This
	 * test examines the case where animations are disabled. The test will only pass if the card
	 * background color is changed to the expected value.
	 */
	@Test
	public void testMarkedSelectedAndCorrect_noAnimation() {
		decoratedAnswerCardEspresso.perform(setAnswer(new PojoAnswer("test", true), false));
		decoratedAnswerCardEspresso.perform(setStatus(true, true, false));

		decoratedAnswerCardEspresso.perform(addDecorator(colorFadeDecorator, false));

		final int expectedColor = COLOR_SUPPLIER.getColor(true, true, true);

		decoratedAnswerCardEspresso.check(cardBackgroundColor(is(expectedColor)));
	}
}