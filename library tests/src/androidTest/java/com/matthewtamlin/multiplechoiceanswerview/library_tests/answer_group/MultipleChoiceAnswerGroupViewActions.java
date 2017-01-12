package com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.MultipleChoiceAnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;

import org.hamcrest.Matcher;

import java.util.List;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

@SuppressWarnings("unchecked")
public class MultipleChoiceAnswerGroupViewActions {
	public static ViewAction addAnswers(final List<? extends AnswerView> answers) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(MultipleChoiceAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return "add answers";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				// Might fail
				((MultipleChoiceAnswerGroup) view).addAnswers(answers);
			}
		};
	}

	public static ViewAction addAnswer(final AnswerView answer) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(MultipleChoiceAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return "add answer";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				// Might fail
				((MultipleChoiceAnswerGroup) view).addAnswer(answer);
			}
		};
	}

	public static ViewAction removeAnswer(final AnswerView answer) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(MultipleChoiceAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return "remove answer";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				// Might fail
				((MultipleChoiceAnswerGroup) view).removeAnswer(answer);
			}
		};
	}

	public static ViewAction clearAnswers() {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(MultipleChoiceAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return "clear answers";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				// Might fail
				((MultipleChoiceAnswerGroup) view).clearAnswers();
			}
		};
	}

	public static ViewAction clickViewAtIndex(final int index) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(MultipleChoiceAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return "click view at index " + index;
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				final MultipleChoiceAnswerGroup castView = (MultipleChoiceAnswerGroup) view;
				castView.getChildAt(index).performClick();
			}
		};
	}

	public static ViewAction allowSelectionChangesWhenMarked(final boolean allow) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(MultipleChoiceAnswerGroup.class);
			}

			@Override
			public String getDescription() {
				return (allow ? "allow" : "disable") + " selection changes when marked";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				((MultipleChoiceAnswerGroup) view).allowSelectionChangesWhenMarked(allow);
			}
		};
	}
}