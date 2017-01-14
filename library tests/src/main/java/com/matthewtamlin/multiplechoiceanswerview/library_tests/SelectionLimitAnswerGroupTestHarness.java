package com.matthewtamlin.multiplechoiceanswerview.library_tests;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.multiple_choice_answer_view.library.answer.ImmutableAnswer;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.SelectionLimitedAnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AlphaDecorator;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.ColorFadeDecorator;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.ColorFadeDecorator.ColorSupplier;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.DecoratedAnswerCard;

import java.util.Random;

@SuppressLint("SetTextI18n") // Not important during testing
public class SelectionLimitAnswerGroupTestHarness extends AnswerGroupTestHarness
		<DecoratedAnswerCard, SelectionLimitedAnswerGroup<DecoratedAnswerCard>> {
	/**
	 * Decorates the test view by changing the background and text colors.
	 */
	private final ColorSupplier colorSupplier = new ColorSupplier() {
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
	};

	/**
	 * Decorates the test view by changing the transparency.
	 */
	private final AlphaDecorator.AlphaSupplier
			alphaSupplier = new AlphaDecorator.AlphaSupplier() {
		@Override
		public float getAlpha(final boolean marked, final boolean selected,
				final boolean answerIsCorrect) {
			if (marked && !selected && !answerIsCorrect) {
				return 0.5f;
			} else {
				return 1f;
			}
		}
	};

	/**
	 * The view being tested. The view should be initialised before use so that {@link
	 * #getTestView()} never returns null.
	 */
	private SelectionLimitedAnswerGroup<DecoratedAnswerCard> testView;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getControlsContainer().addView(createIncreaseMultipleSelectionLimitButton());
		getControlsContainer().addView(createDecreaseMultipleSelectionLimitButton());
	}

	@Override
	public SelectionLimitedAnswerGroup<DecoratedAnswerCard> getTestView() {
		if (testView == null) {
			testView = new SelectionLimitedAnswerGroup<>(this);
		}

		return testView;
	}

	@Override
	public DecoratedAnswerCard getAnswerView() {
		final DecoratedAnswerCard answerCard = new DecoratedAnswerCard(this);

		answerCard.addDecorator(getNewColorFadeDecorator(), false);
		answerCard.addDecorator(getNewAlphaDecorator(), false);

		if ((new Random()).nextBoolean()) {
			answerCard.setAnswer(new ImmutableAnswer("Correct", true), false);
		} else {
			answerCard.setAnswer(new ImmutableAnswer("Incorrect", false), false);
		}

		answerCard.setStatus(false, false, false);

		return answerCard;
	}

	private Button createIncreaseMultipleSelectionLimitButton() {
		final Button b = new Button(this);
		b.setText("Increase multiple selection limit");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final int currentLimit = getTestView().getMultipleSelectionLimit();
				getTestView().setMultipleSelectionLimit(currentLimit + 1);
			}
		});

		return b;
	}

	private Button createDecreaseMultipleSelectionLimitButton() {
		final Button b = new Button(this);
		b.setText("Decrease multiple selection limit");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final int currentLimit = getTestView().getMultipleSelectionLimit();
				getTestView().setMultipleSelectionLimit(currentLimit - 1);
			}
		});

		return b;
	}

	private ColorFadeDecorator getNewColorFadeDecorator() {
		return new ColorFadeDecorator(colorSupplier);
	}

	private AlphaDecorator getNewAlphaDecorator() {
		return new AlphaDecorator(alphaSupplier);
	}
}