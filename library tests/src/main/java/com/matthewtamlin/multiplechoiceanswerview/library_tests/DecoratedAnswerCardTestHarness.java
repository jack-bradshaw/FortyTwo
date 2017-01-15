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

import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AlphaDecorator;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AlphaDecorator.AlphaSupplier;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.ColorFadeDecorator;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.ColorFadeDecorator.ColorSupplier;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.DecoratedAnswerCard;

/**
 * A test harness for displaying and interacting with the {@link DecoratedAnswerCard} class.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public class DecoratedAnswerCardTestHarness extends SimpleAnswerCardTestHarness {
	/**
	 * Decorates the test view by changing the background and text colors.
	 */
	private final ColorFadeDecorator colorFadeDecorator = new ColorFadeDecorator(new ColorSupplier
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
	private final AlphaDecorator alphaDecorator = new AlphaDecorator(new AlphaSupplier() {
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
	 * The view under test.
	 */
	private DecoratedAnswerCard testView;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getControlsContainer().addView(createUseColorDecoratorButton());
		getControlsContainer().addView(createRemoveColorDecoratorButton());
		getControlsContainer().addView(createUseAlphaDecoratorButton());
		getControlsContainer().addView(createRemoveAlphaDecoratorButton());
		getControlsContainer().addView(createClearDecoratorsButton());
	}

	@Override
	public DecoratedAnswerCard getTestView() {
		if (testView == null) {
			testView = new DecoratedAnswerCard(this);
		}

		return testView;
	}

	/**
	 * Creates a button which adds the color fade decorator to the test view when clicked.
	 *
	 * @return the button
	 */
	private Button createUseColorDecoratorButton() {
		final Button b = new Button(this);
		b.setText("Use color fade decorator");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().addDecorator(colorFadeDecorator, true);
			}
		});

		return b;
	}

	/**
	 * Creates a button which removes the color fade decorator from the test view when clicked.
	 *
	 * @return the button
	 */
	private Button createRemoveColorDecoratorButton() {
		final Button b = new Button(this);
		b.setText("Remove color fade decorator");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().removeDecorator(colorFadeDecorator);
			}
		});

		return b;
	}

	/**
	 * Creates a button which adds the alpha decorator to the test view when clicked.
	 *
	 * @return the button
	 */
	private Button createUseAlphaDecoratorButton() {
		final Button b = new Button(this);
		b.setText("Use alpha decorator");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().addDecorator(alphaDecorator, true);
			}
		});

		return b;
	}

	/**
	 * Creates a button which removes the alpha decorator from the test view when clicked.
	 *
	 * @return the button
	 */
	private Button createRemoveAlphaDecoratorButton() {
		final Button b = new Button(this);
		b.setText("Remove alpha decorator");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().removeDecorator(alphaDecorator);
			}
		});

		return b;
	}

	/**
	 * Creates a button which removes all decorators from the test view when clicked.
	 *
	 * @return the button
	 */
	private Button createClearDecoratorsButton() {
		final Button b = new Button(this);
		b.setText("Clear decorators");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().clearDecorators();
			}
		});

		return b;
	}
}