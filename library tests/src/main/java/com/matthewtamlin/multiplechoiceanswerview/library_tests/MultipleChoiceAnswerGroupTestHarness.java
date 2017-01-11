package com.matthewtamlin.multiplechoiceanswerview.library_tests;

import com.matthewtamlin.multiple_choice_answer_view.library.answer.ImmutableAnswer;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.AnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.MultipleChoiceAnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.DecoratedAnswerCard;

import java.util.Random;

public class MultipleChoiceAnswerGroupTestHarness extends
		AnswerGroupTestHarness<MultipleChoiceAnswerGroup<DecoratedAnswerCard>> {
	private MultipleChoiceAnswerGroup testView;

	@Override
	public MultipleChoiceAnswerGroup<DecoratedAnswerCard> getAnswerView() {
		final DecoratedAnswerCard answerCard = new DecoratedAnswerCard(this);

		answerCard.addDecorator(colorFadeDecorator, false);
		answerCard.addDecorator(alphaDecorator, false);

		if ((new Random()).nextBoolean()) {
			answerCard.setAnswer(new ImmutableAnswer("Correct", true), false);
		} else {
			answerCard.setAnswer(new ImmutableAnswer("Incorrect", false), false);
		}

		answerCard.setStatus(false, false, false);

		return answerCard;
	}

	@Override
	public AnswerGroup getTestView() {
		if (testView == null) {
			testView = new MultipleChoiceAnswerGroup(this);
		}

		return testView;
	}
}