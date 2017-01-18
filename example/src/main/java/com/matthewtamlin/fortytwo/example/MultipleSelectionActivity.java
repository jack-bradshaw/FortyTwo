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

package com.matthewtamlin.fortytwo.example;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.matthewtamlin.fortytwo.library.answer.Answer;
import com.matthewtamlin.fortytwo.library.answer.ImmutableAnswer;
import com.matthewtamlin.fortytwo.library.answer_view.AlphaDecorator;
import com.matthewtamlin.fortytwo.library.answer_view.AlphaDecorator.AlphaSupplier;
import com.matthewtamlin.fortytwo.library.answer_view.ColorFadeDecorator;
import com.matthewtamlin.fortytwo.library.answer_view.ColorFadeDecorator.ColorSupplier;
import com.matthewtamlin.fortytwo.library.answer_view.DecoratedAnswerCard;

import java.util.LinkedHashMap;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * An activity which tests the user's knowledge of our solar system.
 */
public class MultipleSelectionActivity extends AbstractQuestionActivity {
	private static final int MULTIPLE_SELECTION_LIMIT = 4;

	/**
	 * The question to display.
	 */
	private static final String QUESTION = "Which of the following are moons of Jupiter? " +
			"You may select up to four answers.";

	/**
	 * The answers and the associated identifiers to display.
	 */
	private static final LinkedHashMap<CharSequence, Answer> answerMap = new LinkedHashMap<>();

	static {
		answerMap.put("1.", new ImmutableAnswer("Pheobe", false));
		answerMap.put("2.", new ImmutableAnswer("Ganymede", true));
		answerMap.put("3.", new ImmutableAnswer("Triton", false));
		answerMap.put("4.", new ImmutableAnswer("Lunar", false));
		answerMap.put("5.", new ImmutableAnswer("Kore", true));
		answerMap.put("6.", new ImmutableAnswer("Callisto", true));
		answerMap.put("7.", new ImmutableAnswer("Titan", false));
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		displayQuestionAndAnswers();
		getAnswerGroup().setMultipleSelectionLimit(MULTIPLE_SELECTION_LIMIT);
	}

	/**
	 * Adds all answer and identifiers to the view.
	 */
	@SuppressWarnings("unchecked")
	private void displayQuestionAndAnswers() {
		getQuestionContainer().setText(QUESTION);

		for (final CharSequence identifier : answerMap.keySet()) {
			final DecoratedAnswerCard decoratedAnswerCard = new DecoratedAnswerCard(this);

			decoratedAnswerCard.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
			decoratedAnswerCard.setIdentifier(identifier, false);
			decoratedAnswerCard.setAnswer(answerMap.get(identifier), false);
			decoratedAnswerCard.addDecorator(createColorFadeDecorator(), false);
			decoratedAnswerCard.addDecorator(createAlphaDecorator(), false);

			getAnswerGroup().addAnswer(decoratedAnswerCard);
		}
	}

	/**
	 * @return a new ColorFadeDecorator
	 */
	private ColorFadeDecorator createColorFadeDecorator() {
		final ColorSupplier colorSupplier = new ColorSupplier() {
			@Override
			public int getColor(final boolean marked, final boolean selected,
					final boolean answerIsCorrect) {
				if (marked) {
					if (selected) {
						return answerIsCorrect ? 0xFF2E7D32 : 0xFFb71c1c; // Green, red
					} else {
						return answerIsCorrect ? 0xFF673AB7 : 0xFFFFFFFF; // Purple, white
					}
				} else {
					return selected ? 0xFFFF9800 : 0xFFFFFFFF; // Orange, white
				}
			}
		};

		return new ColorFadeDecorator(colorSupplier);
	}

	/**
	 * @return a new AlphaDecorator
	 */
	private AlphaDecorator createAlphaDecorator() {
		final AlphaSupplier alphaSupplier = new AlphaSupplier() {
			@Override
			public float getAlpha(final boolean marked, final boolean selected,
					final boolean answerIsCorrect) {
				if (marked && !selected && !answerIsCorrect) {
					return 0.3f;
				} else {
					return 1f;
				}
			}
		};

		return new AlphaDecorator(alphaSupplier);
	}
}