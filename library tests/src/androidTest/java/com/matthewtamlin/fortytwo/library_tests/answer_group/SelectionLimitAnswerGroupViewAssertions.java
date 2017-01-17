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

package com.matthewtamlin.fortytwo.library_tests.answer_group;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.view.View;

import com.matthewtamlin.fortytwo.library.answer_group.SelectionLimitedAnswerGroup;
import com.matthewtamlin.fortytwo.library.answer_view.AnswerView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Custom Espresso view assertions which can be applied to SelectionLimitAnswerViews.
 */
public class SelectionLimitAnswerGroupViewAssertions {
	/**
	 * Creates a ViewAssertion which checks whether or not a SelectionLimitAnswerView contains a
	 * particular answer view.
	 *
	 * @param answerView
	 * 		the answer view to check if contained or not contained
	 * @param contained
	 * 		true to check that the answer view is contained, false to check that it is not
	 * @return the view assertion
	 */
	public static ViewAssertion containsView(final AnswerView answerView, final boolean
			contained) {
		return new ViewAssertion() {
			@Override
			public void check(final View view, final NoMatchingViewException noViewFoundException) {
				if (view == null || !(view instanceof SelectionLimitedAnswerGroup)) {
					throw new AssertionError("view must be a non-null instance of " +
							"MultipleChoiceAnswerGroup.");
				} else {
					final SelectionLimitedAnswerGroup
							group = (SelectionLimitedAnswerGroup) view;

					boolean contains = false;

					for (int i = 0; i < group.getChildCount(); i++) {
						if (group.getChildAt(i).equals(answerView)) {
							contains = true;
						}
					}

					assertThat("view was not contained in answer group.", contains, is(contained));
				}
			}
		};
	}

	/**
	 * Creates a ViewAssertion which checks that a SelectionLimitAnswerView contains no answer
	 * views.
	 *
	 * @return the view assertion
	 */
	public static ViewAssertion containsNoAnswers() {
		return new ViewAssertion() {
			@Override
			public void check(final View view, final NoMatchingViewException noViewFoundException) {
				if (view == null || !(view instanceof SelectionLimitedAnswerGroup)) {
					throw new AssertionError("view must be a non-null instance of " +
							"MultipleChoiceAnswerGroup.");
				} else {
					final SelectionLimitedAnswerGroup
							group = (SelectionLimitedAnswerGroup) view;

					assertThat("view was not contained in answer group.", group.getChildCount(),
							is(0));
				}
			}
		};
	}
}