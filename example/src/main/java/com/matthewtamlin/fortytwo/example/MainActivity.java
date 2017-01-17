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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.matthewtamlin.fortytwo.library.answer.Answer;
import com.matthewtamlin.fortytwo.library.answer.ImmutableAnswer;
import com.matthewtamlin.fortytwo.library.answer_group.AnswerGroup;
import com.matthewtamlin.fortytwo.library.answer_group.SelectionLimitedAnswerGroup;
import com.matthewtamlin.fortytwo.library.answer_view.AlphaDecorator;
import com.matthewtamlin.fortytwo.library.answer_view.AlphaDecorator.AlphaSupplier;
import com.matthewtamlin.fortytwo.library.answer_view.AnswerView;
import com.matthewtamlin.fortytwo.library.answer_view.ColorFadeDecorator;
import com.matthewtamlin.fortytwo.library.answer_view.ColorFadeDecorator.ColorSupplier;
import com.matthewtamlin.fortytwo.library.answer_view.DecoratedAnswerCard;

import java.util.LinkedHashMap;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * An activity which displays a question and a multiple choice answer selector.
 */
public class MainActivity extends AppCompatActivity {
	/**
	 * The wuestion to display.
	 */
	private static final String QUESTION = "What is the answer to the Ultimate Question of Life, " +
			"the Universe, and Everything?";

	/**
	 * Toa answers to display, each mapped to the identifier to display with the answer.
	 */
	private final LinkedHashMap<CharSequence, Answer> answerMap = new LinkedHashMap<>();

	/**
	 * TextView which contains the question.
	 */
	private TextView questionContainer;

	/**
	 * AnswerGroup which contains the answers.
	 */
	private AnswerGroup answerGroup;

	/**
	 * A button for marking and unmarking the answers.
	 */
	private Button actionButton;

	/**
	 * Whether or not the answers are currently marked.
	 */
	private boolean currentlyMarked = false;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		questionContainer = (TextView) findViewById(R.id.main_Activity_question_container);
		answerGroup = (SelectionLimitedAnswerGroup) findViewById(R.id.main_activity_answer_group);
		actionButton = (Button) findViewById(R.id.main_activity_action_button);

		populateAnswerMap();
		displayAnswersAndIdentifiers();
		setupActionButtonBehaviour();
	}

	/**
	 * Populates the answer map with identifiers and answers.
	 */
	private void populateAnswerMap() {
		answerMap.put("A", new ImmutableAnswer("To live long and prosper.", false));
		answerMap.put("B", new ImmutableAnswer("To write really long sentences in a way which " +
				"causes the word count to be raised to an unnecessarily high value without " +
				"adding any additional/supplemental information or providing any value to the " +
				"reader.", false));
		answerMap.put("C", new ImmutableAnswer("To love and be loved.", false));
		answerMap.put("D", new ImmutableAnswer("42.", true));
		answerMap.put("E", new ImmutableAnswer("To value working software over documentation.",
				false));
		answerMap.put("F", new ImmutableAnswer("To propagate one's species.", false));
	}

	/**
	 * Adds all answer and identifiers to the view.
	 */
	@SuppressWarnings("unchecked")
	private void displayAnswersAndIdentifiers() {
		questionContainer.setText(QUESTION);

		for (final CharSequence identifier : answerMap.keySet()) {
			final DecoratedAnswerCard decoratedAnswerCard = new DecoratedAnswerCard(this);

			decoratedAnswerCard.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
			decoratedAnswerCard.setIdentifier(identifier, false);
			decoratedAnswerCard.setAnswer(answerMap.get(identifier), false);
			decoratedAnswerCard.addDecorator(createColorFadeDecorator(), false);
			decoratedAnswerCard.addDecorator(createAlphaDecorator(), false);

			answerGroup.addAnswer(decoratedAnswerCard);
		}
	}

	/**
	 * Toggles the marked status of all answer views.
	 */
	private void setupActionButtonBehaviour() {
		actionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				for (final AnswerView answerView : answerGroup.getAnswers()) {
					answerView.setMarkedStatus(!currentlyMarked, true);
				}

				currentlyMarked = !currentlyMarked;
			}
		});
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
						return answerIsCorrect ? 0xFF8BC34A : 0xFFb71c1c; // Green, red
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