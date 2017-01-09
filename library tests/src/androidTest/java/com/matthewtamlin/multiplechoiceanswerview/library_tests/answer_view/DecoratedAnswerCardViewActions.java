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

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.DecoratedAnswerCard;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.DecoratedAnswerCard.Decorator;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

/**
 * Custom Espresso view actions which can be applied to DecoratedAnswerCard views.
 */
public class DecoratedAnswerCardViewActions {
	/**
	 * The duration to wait for asynchronous events to finish, measured in milliseconds
	 */
	private static final int WAIT_FOR_ASYNC_EVENTS_TO_FINISH_MS = 1500;

	/**
	 * Creates a ViewAction which can be applied to a DecoratedAnswerCard to add a decorator.
	 *
	 * @param decorator
	 * 		the decorator to add, not null
	 * @param animate
	 * 		whether or not the initial decoration should be animated
	 * @return the view action
	 */
	public static ViewAction addDecorator(final Decorator decorator, final boolean animate) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(DecoratedAnswerCard.class);
			}

			@Override
			public String getDescription() {
				return "add decorator";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				((DecoratedAnswerCard) view).addDecorator(decorator, animate);

				if (animate) {
					uiController.loopMainThreadForAtLeast(WAIT_FOR_ASYNC_EVENTS_TO_FINISH_MS);
				}
			}
		};
	}

	/**
	 * Creates a ViewAction which can be applied to a DecoratedAnswerCard to remove a decorator.
	 *
	 * @param decorator
	 * 		the decorator to remove, not null
	 * @return the view action
	 */
	public static ViewAction removeDecorator(final Decorator decorator) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(DecoratedAnswerCard.class);
			}

			@Override
			public String getDescription() {
				return "remove decorator";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				((DecoratedAnswerCard) view).removeDecorator(decorator);
			}
		};
	}
}