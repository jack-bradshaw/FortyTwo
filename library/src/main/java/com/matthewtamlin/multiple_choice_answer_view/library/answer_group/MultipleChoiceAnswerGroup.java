package com.matthewtamlin.multiple_choice_answer_view.library.answer_group;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultipleChoiceAnswerGroup extends LinearLayout implements AnswerGroup {
	private final Set<Listener> listeners = new HashSet<>();

	private final List<AnswerView> allViews = new ArrayList<>();

	private Set<AnswerView> selectedViews = new HashSet<>();

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
			for (final AnswerView view : allViews) {
				view.setStatus(false, false, animate);
			}
		}
	}

	public int getMultipleSelectionLimit() {
		return multipleSelectionLimit;
	}

	@Override
	public void addAnswers(final List<AnswerView> answers) {
		if (answers != null) {
			this.allViews.addAll(answers);

			for (final AnswerView answer : answers) {
				if (answer != null) {

					addView((View) answer);
					((View) answer).setOnClickListener(new OnClickListener() {
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
		}
	}


	@Override
	public void addAnswer(final AnswerView answer) {
		if (answer != null) {
			allViews.add(answer);

			final View asView = (View) answer;
			addView(asView);
			asView.setOnClickListener(new OnClickListener() {
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
	public void removeAnswer(final AnswerView answer) {
		if (answer != null) {
			removeView((View) answer);
			allViews.remove(answer);
			selectedViews.remove(answer);
			((View) answer).setOnClickListener(null);
		}
	}

	@Override
	public void clearAnswers() {
		for (final AnswerView view : allViews) {
			((View) view).setOnClickListener(null);
		}

		allViews.clear();
		selectedViews.clear();
	}

	@Override
	public List<AnswerView> getAnswers() {
		return Collections.unmodifiableList(allViews);
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

		for (final AnswerView view : allViews) {
			if (view.isSelected()) {
				selectedViews.add(view);
			}
		}
	}

	@Override
	public void registerListener(final Listener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	@Override
	public void unregisterListener(final Listener listener) {
		listeners.remove(listener);
	}

	private void init() {
		setOrientation(VERTICAL);
	}

	private void handleClick(final AnswerView clickedView) {
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

	private void deselectView(final AnswerView answerView) {
		answerView.setSelectedStatus(false, true);

		selectedViews.remove(answerView);

		for (final Listener listener : listeners) {
			listener.onAnswerDeselected(answerView);
		}
	}

	private void selectView(final AnswerView answerView) {
		answerView.setSelectedStatus(true, true);

		selectedViews.add(answerView);

		for (final Listener listener : listeners) {
			listener.onAnswerSelected(answerView);
		}
	}
}
