package com.matthewtamlin.multiplechoiceanswerview.library_tests;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.android_testing_tools.library.ControlsBelowViewTestHarness;
import com.matthewtamlin.multiple_choice_answer_view.library.answer.ImmutableAnswer;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.AnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AlphaDecorator;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.ColorFadeDecorator;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.DecoratedAnswerCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressLint("SetTextI18n") // Not important during testing
public abstract class AnswerGroupTestHarness extends ControlsBelowViewTestHarness<AnswerGroup> {
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

	private AnswerView createNewAnswerView() {
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