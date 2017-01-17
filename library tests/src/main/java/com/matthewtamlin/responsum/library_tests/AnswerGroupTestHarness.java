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

package com.matthewtamlin.responsum.library_tests;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.android_testing_tools.library.ControlsOverViewTestHarness;
import com.matthewtamlin.responsum.library.answer_group.AnswerGroup;
import com.matthewtamlin.responsum.library.answer_view.AnswerView;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * An abstract test harness for displaying and interacting with an implementation of the {@link
 * AnswerGroup} interface.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public abstract class AnswerGroupTestHarness extends ControlsOverViewTestHarness<AnswerGroup> {
	/**
	 * @return a new AnswerView which can be displayed in the test answer group, not null
	 */
	public abstract AnswerView getAnswerView();

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getRootView().setBackgroundColor(Color.BLACK);

		getControlsContainer().addView(createAddAnswersButton());
		getControlsContainer().addView(createAddAnswerButton());
		getControlsContainer().addView(createRemoveAnswerButton());
		getControlsContainer().addView(createClearAnswersButton());
		getControlsContainer().addView(createAllowSelectionChangesWhenMarkedButton());
		getControlsContainer().addView(createDisallowSelectionChangesWhenMarkedButton());
		getControlsContainer().addView(createMarkAllButton());
		getControlsContainer().addView(createUnmarkAllButton());
	}

	/**
	 * Creates a button which adds multiple answer views to the test view when clicked.
	 *
	 * @return the button
	 */
	private Button createAddAnswersButton() {
		final Button b = new Button(this);
		b.setText("Add multiple answers");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final List<AnswerView> answers = new ArrayList<>();

				answers.add(getAnswerView());
				answers.add(getAnswerView());
				answers.add(getAnswerView());

				getTestView().addAnswers(answers);
			}
		});

		return b;
	}

	/**
	 * Creates a button which adds a single answer view to the test view when clicked.
	 *
	 * @return the button
	 */
	private Button createAddAnswerButton() {
		final Button b = new Button(this);
		b.setText("Add one answer");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().addAnswer(getAnswerView());
			}
		});

		return b;
	}

	/**
	 * Creates a button which removes the first answer view from the test view when clicked.
	 *
	 * @return the button
	 */
	private Button createRemoveAnswerButton() {
		final Button b = new Button(this);
		b.setText("Remove first answer");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (!getTestView().getAnswers().isEmpty()) {
					final AnswerView firstAnswer = getTestView().getAnswers().get(0);
					getTestView().removeAnswer(firstAnswer);
				}
			}
		});

		return b;
	}

	/**
	 * Creates a button which clears all answer views from the test view when clicked.
	 *
	 * @return the button
	 */
	private Button createClearAnswersButton() {
		final Button b = new Button(this);
		b.setText("Clear answers");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().clearAnswers();
			}
		});

		return b;
	}

	/**
	 * Creates a button which when clicked, allows marked views to be selected and unselected.
	 *
	 * @return the button
	 */
	private Button createAllowSelectionChangesWhenMarkedButton() {
		final Button b = new Button(this);
		b.setText("Unlock when marked");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().allowSelectionChangesWhenMarked(true);
			}
		});

		return b;
	}

	/**
	 * Creates a button which when clicked, prevents marked views from being selected and
	 * unselected.
	 *
	 * @return the button
	 */
	private Button createDisallowSelectionChangesWhenMarkedButton() {
		final Button b = new Button(this);
		b.setText("Lock when marked");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().allowSelectionChangesWhenMarked(false);
			}
		});

		return b;
	}

	/**
	 * Creates a button which when clicked, marks all answer views in the test view.
	 *
	 * @return the button
	 */
	private Button createMarkAllButton() {
		final Button b = new Button(this);
		b.setText("Mark all");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				for (final AnswerView answerView : getTestView().getAnswers()) {
					answerView.setMarkedStatus(true, true);
				}
			}
		});

		return b;
	}

	/**
	 * Creates a button which when clicked, unmarks all answer views in the test view.
	 *
	 * @return the button
	 */
	private Button createUnmarkAllButton() {
		final Button b = new Button(this);
		b.setText("Unmark all");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				for (final AnswerView answerView : getTestView().getAnswers()) {
					answerView.setMarkedStatus(false, true);
				}
			}
		});

		return b;
	}
}