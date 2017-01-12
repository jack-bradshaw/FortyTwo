package com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.view.View;

import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.MultipleChoiceAnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MultipleChoiceAnswerGroupViewAssertions {
	public static ViewAssertion containsView(final AnswerView view) {
		return new ViewAssertion() {
			@Override
			public void check(final View view, final NoMatchingViewException noViewFoundException) {
				if (view == null || !(view instanceof MultipleChoiceAnswerGroup)) {
					throw new AssertionError("view must be a non-null instance of " +
							"MultipleChoiceAnswerGroup.");
				} else {
					final MultipleChoiceAnswerGroup group = (MultipleChoiceAnswerGroup) view;

					boolean contains = false;

					for (int i = 0; i < group.getChildCount(); i++) {
						if (group.getChildAt(i).equals(view)) {
							contains = true;
						}
					}

					assertThat("view was not contained in answer group.", contains, is(true));
				}
			}
		};
	}
}