package com.matthewtamlin.multiplechoiceanswerview.library_tests;

import android.graphics.Color;

import com.matthewtamlin.multiple_choice_answer_view.library.answer.ImmutableAnswer;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.AnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.MultipleChoiceAnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AlphaDecorator;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.ColorFadeDecorator;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.DecoratedAnswerCard;

import java.util.Random;

public class MultipleChoiceAnswerGroupTestHarness extends
		AnswerGroupTestHarness<DecoratedAnswerCard> {
	/**
	 * Decorates the test view by changing the background and text colors.
	 */
	private final ColorFadeDecorator colorFadeDecorator =
			new ColorFadeDecorator(new ColorFadeDecorator.ColorSupplier
					() {
				@Override
				public int getColor(final boolean marked, final boolean selected,
						final boolean answerIsCorrect) {
					if (marked) {
						if (selected) {
							return answerIsCorrect ? Color.GREEN : Color.RED;
						} else {
							return answerIsCorrect ? Color.RED : Color.GREEN;
						}
					} else {
						return selected ? Color.BLUE : Color.WHITE;
					}
				}
			});

	/**
	 * Decorates the test view by changing the transparency.
	 */
	private final AlphaDecorator
			alphaDecorator = new AlphaDecorator(new AlphaDecorator.AlphaSupplier() {
		@Override
		public float getAlpha(final boolean marked, final boolean selected,
				final boolean answerIsCorrect) {
			if (marked && !selected && !answerIsCorrect) {
				return 0.5f;
			} else {
				return 1f;
			}
		}
	});

	/**
	 * The view being tested. The view should be initialised before use so that {@link
	 * #getTestView()} never returns null.
	 */
	private MultipleChoiceAnswerGroup testView;

	@Override
	public AnswerGroup getTestView() {
		if (testView == null) {
			testView = new MultipleChoiceAnswerGroup(this);
		}

		return testView;
	}

	@Override
	public DecoratedAnswerCard getAnswerView() {
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
}