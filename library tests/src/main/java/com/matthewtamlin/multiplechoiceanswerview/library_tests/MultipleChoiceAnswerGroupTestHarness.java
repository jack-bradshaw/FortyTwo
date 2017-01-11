package com.matthewtamlin.multiplechoiceanswerview.library_tests;

import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.AnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.MultipleChoiceAnswerGroup;

public class MultipleChoiceAnswerGroupTestHarness extends AnswerGroupTestHarness {
	private MultipleChoiceAnswerGroup testView;

	@Override
	public AnswerGroup getTestView() {
		if (testView == null) {
			testView = new MultipleChoiceAnswerGroup(this);
		}

		return testView;
	}
}
