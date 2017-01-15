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

/**
 * A test harness for displaying and interacting with an {@link SelectionLimitedAnswerGroup}.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public class SelectionLimitAnswerGroupTestHarness extends AnswerGroupTestHarness
		<DecoratedAnswerCard, SelectionLimitedAnswerGroup<DecoratedAnswerCard>> {
	/**
	 * Supplies colors to ColorDecorators.
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
	 * Supplied alpha values to AlphaDecorators.
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
	 * The view under test.
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

	/**
	 * Creates a button which increases the multiple selection limit of the test view when clicked.
	 *
	 * @return the button
	 */
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

	/**
	 * Creates a button which decreases the multiple selection limit of the test view when clicked.
	 *
	 * @return the button
	 */
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

	/**
	 * @return a new ColorFadeDecorator which uses the {@code colorSupplier}
	 */
	private ColorFadeDecorator getNewColorFadeDecorator() {
		return new ColorFadeDecorator(colorSupplier);
	}

	/**
	 * @return a new AlphaDecorator which uses the {@code alphaSupplier}
	 */
	private AlphaDecorator getNewAlphaDecorator() {
		return new AlphaDecorator(alphaSupplier);
	}
}