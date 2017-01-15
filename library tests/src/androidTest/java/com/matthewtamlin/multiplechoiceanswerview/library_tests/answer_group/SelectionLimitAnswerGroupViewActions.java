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

package com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.AnswerGroup.Listener;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.SelectionLimitedAnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;

import org.hamcrest.Matcher;

import java.util.List;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

@SuppressWarnings("unchecked")
public class SelectionLimitAnswerGroupViewActions {
	public static ViewAction addAnswers(final List<? extends AnswerView> answers) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(SelectionLimitedAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return "add answers";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				// Might fail
				((SelectionLimitedAnswerGroup) view).addAnswers(answers);
			}
		};
	}

	public static ViewAction addAnswer(final AnswerView answer) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(SelectionLimitedAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return "add answer";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				// Might fail
				((SelectionLimitedAnswerGroup) view).addAnswer(answer);
			}
		};
	}

	public static ViewAction removeAnswer(final AnswerView answer) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(SelectionLimitedAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return "remove answer";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				// Might fail
				((SelectionLimitedAnswerGroup) view).removeAnswer(answer);
			}
		};
	}

	public static ViewAction clearAnswers() {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(SelectionLimitedAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return "clear answers";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				// Might fail
				((SelectionLimitedAnswerGroup) view).clearAnswers();
			}
		};
	}

	public static ViewAction clickViewAtIndex(final int index) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(SelectionLimitedAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return "click view at index " + index;
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				final SelectionLimitedAnswerGroup
						castView = (SelectionLimitedAnswerGroup) view;
				castView.getChildAt(index).performClick();
			}
		};
	}

	public static ViewAction allowSelectionChangesWhenMarked(final boolean allow) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(SelectionLimitedAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return (allow ? "allow" : "disable") + " selection changes when marked";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				((SelectionLimitedAnswerGroup) view).allowSelectionChangesWhenMarked(allow);
			}
		};
	}

	public static ViewAction setMultipleSelectionLimit(final int limit) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(SelectionLimitedAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return "set multiple selection limit to " + limit;
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				((SelectionLimitedAnswerGroup) view).setMultipleSelectionLimit(limit);
			}
		};
	}

	public static ViewAction registerListener(final Listener listener) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(SelectionLimitedAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return "register listener";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				((SelectionLimitedAnswerGroup) view).registerListener(listener);
			}
		};
	}
}