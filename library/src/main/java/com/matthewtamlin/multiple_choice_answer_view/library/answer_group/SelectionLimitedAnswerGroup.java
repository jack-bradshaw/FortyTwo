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

package com.matthewtamlin.multiple_choice_answer_view.library.answer_group;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.matthewtamlin.android_utilities.library.testing.Tested;
import com.matthewtamlin.java_utilities.checkers.IntChecker;
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
 * An AnswerGroup which limits the number of view which can be selected at any given time. The limit
 * can be set at any time, and is automatically enforced when views are clicked. If the limit has
 * been reached and a view is clicked, the view which was least recently selected will be deselected
 * to allow for the newly selected view.
 *
 * @param <V>
 * 		the type of AnswerViews contained
 */
@Tested(testMethod = "automated", requiresInstrumentation = true)
public class SelectionLimitedAnswerGroup<V extends AnswerView> extends LinearLayout implements
		AnswerGroup<V> {
	/**
	 * The listeners which have registered for callbacks.
	 */
	private final Set<AnswerGroup.Listener<V>> listeners = new HashSet<>();

	/**
	 * All answers which are currently displayed in this group.
	 */
	private final List<V> allAnswers = new ArrayList<>();

	/**
	 * All answers which are currently displayed and selected. The size of the stack enforces the
	 * selection limit.
	 */
	private EvictingStackSet<V> selectedViews = new EvictingStackSet<>(1);

	/**
	 * Whether or not the selection status of marked views can be changed.
	 */
	private boolean allowSelectionChangesWhenMarked = false;

	/**
	 * Whether or not animations should be shown when selecting and deselecting views.
	 */
	private boolean selectionAnimationsEnabled = true;

	/**
	 * Listens to eviction callbacks from the {@code selectedViews} and selects the evicted view.
	 */
	private EvictionListener<V> evictionListener = new EvictionListener<V>() {
		@Override
		public void onEviction(final EvictingStackSet<V> evictingStackSet, final V evicted) {
			deselectView(evicted);
		}
	};

	/**
	 * Constructs a new SelectionLimitAnswerGroup. The selection limit is initially set to 1.
	 *
	 * @param context
	 * 		the context the view is operating in, not null
	 */
	public SelectionLimitedAnswerGroup(final Context context) {
		super(context);
		init();
	}

	/**
	 * Constructs a new SelectionLimitAnswerGroup. The selection limit is initially set to 1.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public SelectionLimitedAnswerGroup(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Constructs a new SelectionLimitAnswerGroup. The selection limit is initially set to 1.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */
	public SelectionLimitedAnswerGroup(final Context context, final AttributeSet attrs,
			final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	/**
	 * Constructs a new SelectionLimitAnswerGroup. The selection limit is initially set to 1.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 * @param defStyleRes
	 * 		a resource in the current theme which supplied default attributes, pass 0 to ignore
	 */
	@RequiresApi(21) // For caller
	@TargetApi(21) // For lint
	public SelectionLimitedAnswerGroup(final Context context, final AttributeSet attrs,
			final int defStyleAttr, final int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	/**
	 * Sets the number of answer views which can be selected at any time. If the new limit is less
	 * than the current number of selected views, the least recently selected views are deselected
	 * so that the number of selected views equals the limit.
	 *
	 * @param limit
	 * 		the new limit, greater than zero
	 * @throws IllegalArgumentException
	 * 		if {@code limit} is less than 1
	 */
	public void setMultipleSelectionLimit(final int limit) {
		IntChecker.checkGreaterThan(limit, 0, "limit cannot be less than 1.");
		selectedViews.setMaxSize(limit);
	}

	/**
	 * Enables/disables animations when answer views are selected/deselected.
	 *
	 * @param enable
	 * 		true to enable animations, false to disable them
	 */
	public void enableSelectionAnimations(final boolean enable) {
		selectionAnimationsEnabled = enable;
	}

	/**
	 * Whether or not animations are enabled when views are selected/deselected
	 *
	 * @return true if animations are enabled, false otherwise
	 */
	public boolean selectionAnimationsAreEnabled() {
		return selectionAnimationsEnabled;
	}

	/**
	 * @return the current selection limit of this group
	 */
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

	/**
	 * Common initializer method for this view. This method should only be called from a
	 * constructor.
	 */
	private void init() {
		setOrientation(VERTICAL);

		selectedViews.registerListener(evictionListener);
	}

	/**
	 * Handles clicks on answer views contained within this group.
	 *
	 * @param clickedView
	 * 		the answer view which was clicked, not null
	 */
	private void handleClick(final V clickedView) {
		boolean allowSelectionChange = !(clickedView.isMarked()
				&& !allowSelectionChangesWhenMarked);

		if (allowSelectionChange) {
			if (clickedView.isSelected()) {
				deselectView(clickedView);
			} else {
				selectView(clickedView);
			}
		}
	}

	/**
	 * Deselects the supplied view and calls any registered listeners. Calling this method with a
	 * view which is already deselected causes the method to exit immediately.
	 *
	 * @param answerView
	 * 		the view to deselect, not null
	 */
	private void deselectView(final V answerView) {
		if (answerView.isSelected()) {
			answerView.setSelectedStatus(false, selectionAnimationsEnabled);

			selectedViews.remove(answerView);

			for (final AnswerGroup.Listener<V> listener : listeners) {
				listener.onAnswerDeselected(this, answerView);
			}
		}
	}

	/**
	 * Selects the supplied view and calls any registered listeners. Calling this method with a view
	 * which is already selected causes the method to exit immediately.
	 *
	 * @param answerView
	 * 		the view to select, not null
	 */
	private void selectView(final V answerView) {
		if (!answerView.isSelected()) {
			answerView.setSelectedStatus(true, selectionAnimationsEnabled);

			selectedViews.push(answerView);

			for (final AnswerGroup.Listener<V> listener : listeners) {
				listener.onAnswerSelected(this, answerView);
			}
		}
	}
}