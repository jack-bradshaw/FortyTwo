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

package com.matthewtamlin.fortytwo.library_tests.answer_view;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.view.View;

import com.matthewtamlin.fortytwo.library.answer_view.SimpleAnswerCard;

import org.hamcrest.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Custom Espresso view assertions which can be applied to SimpleAnswerCard views.
 */
public class SimpleAnswerCardViewAssertions {
	/**
	 * Creates a ViewAssertion which checks that the animation duration of a SimpleAnswerCard
	 * matches some condition.
	 *
	 * @param animationDurationMs
	 * 		the condition to match
	 * @return the view assertion
	 */
	public static ViewAssertion animationDuration(final Matcher<Integer> animationDurationMs) {
		return new ViewAssertion() {
			@Override
			public void check(final View view, final NoMatchingViewException noViewFoundException) {
				if (view == null || !(view instanceof SimpleAnswerCard)) {
					throw new AssertionError("view must be a non-null instance of " +
							"SimpleAnswerCard.");
				} else {
					final SimpleAnswerCard singleAnswerCard = (SimpleAnswerCard) view;
					assertThat("card animation duration is wrong.", singleAnswerCard
							.getAnimationDurationMs(), animationDurationMs);
				}
			}
		};
	}

	/**
	 * Creates a ViewAssertion which checks that the status of a SimpleAnswerCard matches some
	 * condition.
	 *
	 * @param marked
	 * 		the condition to match on the marked property of the card
	 * @param selected
	 * 		the condition to match on the selected property of the card
	 * @return the view assertion
	 */
	public static ViewAssertion status(final Matcher<Boolean> marked,
			final Matcher<Boolean> selected) {
		return new ViewAssertion() {
			@Override
			public void check(final View view, final NoMatchingViewException noViewFoundException) {
				if (view == null || !(view instanceof SimpleAnswerCard)) {
					throw new AssertionError("view must be a non-null instance of " +
							"SimpleAnswerCard.");
				} else {
					final SimpleAnswerCard singleAnswerCard = (SimpleAnswerCard) view;

					assertThat("card marked status is wrong.", singleAnswerCard.isMarked(),
							marked);
					assertThat("card selected status is wrong.", singleAnswerCard.isSelected(),
							selected);
				}
			}
		};
	}

	/**
	 * Creates a ViewAssertion which checks that the background of a SimpleAnswerCard matches some
	 * condition. The CardView within the SimpleAnswerCard is the component which is actually
	 * queried when examining the background.
	 *
	 * @param color
	 * 		the condition to match
	 * @return the view assertion
	 */
	public static ViewAssertion cardBackgroundColor(final Matcher<Integer> color) {
		return new ViewAssertion() {
			@Override
			public void check(final View view, final NoMatchingViewException noViewFoundException) {
				if (view == null || !(view instanceof SimpleAnswerCard)) {
					throw new AssertionError("view must be a non-null instance of " +
							"SimpleAnswerCard.");
				} else {
					final SimpleAnswerCard singleAnswerCard = (SimpleAnswerCard) view;

					assertThat("card has wrong cardBackgroundColor.", singleAnswerCard.getCard()
							.getCardBackgroundColor().getDefaultColor(), color);
				}
			}
		};
	}

	/**
	 * Creates a ViewAssertion which checks that the alpha value of a SimpleAnswerCard matches some
	 * condition.
	 *
	 * @param alpha
	 * 		the condition to match
	 * @return the view assertion
	 */
	public static ViewAssertion alpha(final Matcher<Float> alpha) {
		return new ViewAssertion() {
			@Override
			public void check(final View view, final NoMatchingViewException noViewFoundException) {
				if (view == null || !(view instanceof SimpleAnswerCard)) {
					throw new AssertionError("view must be a non-null instance of " +
							"SimpleAnswerCard.");
				} else {
					final SimpleAnswerCard singleAnswerCard = (SimpleAnswerCard) view;

					assertThat("card has wrong alpha.", singleAnswerCard.getAlpha(), alpha);
				}
			}
		};
	}
}