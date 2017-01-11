package com.matthewtamlin.multiplechoiceanswerview.library_tests;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.android_testing_tools.library.ControlsBelowViewTestHarness;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.AnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AlphaDecorator;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.ColorFadeDecorator;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetTextI18n") // Not important during testing
public abstract class AnswerGroupTestHarness<V extends AnswerView> extends
		ControlsBelowViewTestHarness<AnswerGroup<V>> {
	public abstract V getAnswerView();

	@Override
	public void onCreate(final Bundle savedInstanceState, final PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);

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
				final List<AnswerView> answers = new ArrayList<>();
				answers.add(createNewAnswerView());
				answers.add(createNewAnswerView());
				answers.add(createNewAnswerView());

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
				getTestView().addAnswer(createNewAnswerView());
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
					final AnswerView firstAnswer = getTestView().getAnswers().get(0);
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
		b.setText("Lock when marked");
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
		b.setText("Unlock when marked");
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
				for (final AnswerView answerView : getTestView().getAnswers()) {
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
				for (final AnswerView answerView : getTestView().getAnswers()) {
					answerView.setMarkedStatus(false, true);
				}
			}
		});

		return b;
	}
}