package com.matthewtamlin.multiple_choice_answer_view.library.answer_group;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.matthewtamlin.java_utilities.checkers.NullChecker;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;
import com.matthewtamlin.multiple_choice_answer_view.library.util.EvictingStackSet;
import com.matthewtamlin.multiple_choice_answer_view.library.util.EvictingStackSet.EvictionListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An AnswerGroup which allows the
 * @param <V>
 */
public class SelectionLimitedAnswerGroup<V extends AnswerView> extends LinearLayout implements
		AnswerGroup<V> {
	private final Set<AnswerGroup.Listener<V>> listeners = new HashSet<>();

	private final List<V> allAnswers = new ArrayList<>();

	private EvictingStackSet<V> selectedViews = new EvictingStackSet<>(1);

	private boolean allowSelectionChangesWhenMarked = false;

	private EvictionListener<V> evictionListener = new EvictionListener<V>() {
		@Override
		public void onEviction(final EvictingStackSet<V> evictingStackSet, final V evicted) {
			deselectView(evicted, true);
		}
	};

	public SelectionLimitedAnswerGroup(final Context context) {
		super(context);
		init();
	}

	public SelectionLimitedAnswerGroup(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SelectionLimitedAnswerGroup(final Context context, final AttributeSet attrs,
			final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@RequiresApi(21) // For caller
	@TargetApi(21) // For lint
	public SelectionLimitedAnswerGroup(final Context context, final AttributeSet attrs,
			final int defStyleAttr, final int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public void setMultipleSelectionLimit(final int limit) {
		selectedViews.setMaxSize(limit);
	}

	public int getMultipleSelectionLimit() {
		return selectedViews.getMaxSize();
	}

	@Override
	public void addAnswers(final Collection<V> answers) {
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

		removeView((View) answer);
		((View) answer).setOnClickListener(null);
	}

	@Override
	public void clearAnswers() {
		// Use a copy to avoid concurrent modification exceptions when removeAnswer is called
		final List<V> allAnswersCopy = new ArrayList<>(allAnswers);

		for (final V answer : allAnswersCopy) {
			removeAnswer(answer);
		}
	}

	@Override
	public List<V> getAnswers() {
		return new ArrayList<>(allAnswers);
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

		selectedViews.registerListener(evictionListener);
	}

	private void handleClick(final V clickedView) {
		boolean allowSelectionChange = !(clickedView.isMarked()
				&& !allowSelectionChangesWhenMarked);

		if (allowSelectionChange) {
			if (clickedView.isSelected()) {
				deselectView(clickedView, true);
			} else {
				selectView(clickedView, true);
			}
		}
	}

	private void deselectView(final V answerView, final boolean animate) {
		if (answerView.isSelected()) {
			answerView.setSelectedStatus(false, animate);

			selectedViews.remove(answerView);

			for (final AnswerGroup.Listener<V> listener : listeners) {
				listener.onAnswerDeselected(this, answerView);
			}
		}
	}

	private void selectView(final V answerView, final boolean animate) {
		if (!answerView.isSelected()) {
			answerView.setSelectedStatus(true, animate);

			selectedViews.push(answerView);

			for (final AnswerGroup.Listener<V> listener : listeners) {
				listener.onAnswerSelected(this, answerView);
			}
		}
	}
}