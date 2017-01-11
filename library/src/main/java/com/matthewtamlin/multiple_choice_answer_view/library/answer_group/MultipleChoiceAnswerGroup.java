package com.matthewtamlin.multiple_choice_answer_view.library.answer_group;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.matthewtamlin.java_utilities.checkers.NullChecker;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultipleChoiceAnswerGroup<V extends AnswerView> extends LinearLayout implements
		AnswerGroup<V> {
	private final Set<AnswerGroup.Listener<V>> listeners = new HashSet<>();

	private final List<V> allAnswers = new ArrayList<>();

	private Set<V> selectedViews = new HashSet<>();

	private int multipleSelectionLimit = 1;

	private boolean allowSelectionChangesWhenMarked = false;

	public MultipleChoiceAnswerGroup(final Context context) {
		super(context);
		init();
	}

	public MultipleChoiceAnswerGroup(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MultipleChoiceAnswerGroup(final Context context, final AttributeSet attrs,
			final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@RequiresApi(21) // For caller
	@TargetApi(21) // For lint
	public MultipleChoiceAnswerGroup(final Context context, final AttributeSet attrs,
			final int defStyleAttr, final int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public void setMultipleSelectionLimit(final int limit, final boolean animate) {
		multipleSelectionLimit = limit;

		if (limit < selectedViews.size()) {
			for (final V view : allAnswers) {
				deselectView(view);
			}
		}
	}

	public int getMultipleSelectionLimit() {
		return multipleSelectionLimit;
	}

	@Override
	public void addAnswers(final List<V> answers) {
		NullChecker.checkEachElementIsNotNull(answers, "answers cannot be null or contain null.");

		allAnswers.addAll(answers);

		for (final V answer : answers) {
			addView((View) answer);

			((View) answer).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					handleClick(answer);
				}
			});

			if (answer.isSelected()) {
				selectedViews.add(answer);
			}
		}
	}

	@Override
	public void addAnswer(final V answer) {
		NullChecker.checkNotNull(answer, "answer cannot be null.");

		final List<V> answers = new ArrayList<>();
		answers.add(answer);

		addAnswers(answers);
	}

	@Override
	public void removeAnswer(final V answer) {
		NullChecker.checkNotNull(answer, "answer cannot be null.");

		allAnswers.remove(answer);
		selectedViews.remove(answer);

		((View) answer).setOnClickListener(null);
	}

	@Override
	public void clearAnswers() {
		for (final V answer : allAnswers) {
			removeAnswer(answer);
		}
	}

	@Override
	public List<V> getAnswers() {
		return Collections.unmodifiableList(allAnswers);
	}

	@Override
	public void allowSelectionChangesWhenMarked(final boolean allow) {
		allowSelectionChangesWhenMarked = allow;
	}

	@Override
	public boolean selectionChangesAreAllowedWhenMarked() {
		return allowSelectionChangesWhenMarked;
	}

	@Override
	public void declareExternalViewSelectionChanges() {
		selectedViews.clear();

		for (final V answer : allAnswers) {
			if (answer.isSelected()) {
				selectedViews.add(answer);
			}
		}
	}

	@Override
	public void registerListener(final Listener<V> listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	@Override
	public void unregisterListener(final Listener<V> listener) {
		listeners.remove(listener);
	}

	private void init() {
		setOrientation(VERTICAL);
	}

	private void deselectView(final V answerView) {
		if (answerView.isSelected()) {
			answerView.setSelectedStatus(false, true);

			selectedViews.remove(answerView);

			for (final AnswerGroup.Listener<V> listener : listeners) {
				listener.onAnswerDeselected(answerView);
			}
		}
	}

	private void selectView(final V answerView) {
		if (!answerView.isSelected()) {
			answerView.setSelectedStatus(true, true);

			selectedViews.add(answerView);

			for (final AnswerGroup.Listener<V> listener : listeners) {
				listener.onAnswerSelected(answerView);
			}
		}
	}

	private void handleClick(final V clickedView) {
		boolean allowSelectionChange = !(clickedView.isMarked()
				&& !allowSelectionChangesWhenMarked);

		if (allowSelectionChange) {
			if (clickedView.isSelected()) {
				deselectView(clickedView);
			} else if (selectedViews.size() < multipleSelectionLimit) {
				selectView(clickedView);
			}
		}
	}


}
