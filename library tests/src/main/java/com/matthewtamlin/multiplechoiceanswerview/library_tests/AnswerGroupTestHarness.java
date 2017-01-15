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

import com.matthewtamlin.android_testing_tools.library.ControlsOverViewTestHarness;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.AnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetTextI18n") // Not important during testing
public abstract class AnswerGroupTestHarness<V extends AnswerView, T extends AnswerGroup<V>> extends
		ControlsOverViewTestHarness<T> {
	public abstract V getAnswerView();

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

	private Button createAddAnswersButton() {
		final Button b = new Button(this);
		b.setText("Add multiple answers");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final List<V> answers = new ArrayList<>();

				answers.add(getAnswerView());
				answers.add(getAnswerView());
				answers.add(getAnswerView());

				getTestView().addAnswers(answers);
			}
		});

		return b;
	}

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

	private Button createRemoveAnswerButton() {
		final Button b = new Button(this);
		b.setText("Remove first answer");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (!getTestView().getAnswers().isEmpty()) {
					final V firstAnswer = getTestView().getAnswers().get(0);
					getTestView().removeAnswer(firstAnswer);
				}
			}
		});

		return b;
	}

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

	private Button createMarkAllButton() {
		final Button b = new Button(this);
		b.setText("Mark all");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				for (final V answerView : getTestView().getAnswers()) {
					answerView.setMarkedStatus(true, true);
				}
			}
		});

		return b;
	}

	private Button createUnmarkAllButton() {
		final Button b = new Button(this);
		b.setText("Unmark all");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				for (final V answerView : getTestView().getAnswers()) {
					answerView.setMarkedStatus(false, true);
				}
			}
		});

		return b;
	}
}