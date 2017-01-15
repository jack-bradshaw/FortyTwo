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

package com.matthewtamlin.multiple_choice_answer_view.library.answer_view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.matthewtamlin.android_utilities.library.helpers.ColorHelper;
import com.matthewtamlin.java_utilities.checkers.IntChecker;
import com.matthewtamlin.multiple_choice_answer_view.library.R;
import com.matthewtamlin.multiple_choice_answer_view.library.answer.Answer;

/**
 * A implementation of the AnswerView interface which uses a CardView for the UI. Although all
 * interface methods are implemented, the class is declared abstract because the UI is never updated
 * to reflect the selected status and the marked status. Despite this, all getters work as expected.
 * The accessibility content description of the view is automatically set based on the current
 * status and answer, however custom content descriptions can be set by passing false to {@link
 * #enableAutomaticContentDescriptions(boolean)} and setting the content description as usual.
 */
public abstract class SimpleAnswerCard extends FrameLayout implements AnswerView {
	/**
	 * The main UI component, containing the answer container and identifier container.
	 */
	private CardView card;

	/**
	 * Displays the answer to the user.
	 */
	private TextView answerContainer;

	/**
	 * Displays the identifier to the user.
	 */
	private TextView identifierContainer;

	/**
	 * Indicates whether or not the containers are currently being updated. This could be an
	 * instantaneous update on the UI thread, or an asynchronous update using animators.
	 */
	private boolean textUpdateInProgress = false;

	/**
	 * Indicates whether or not an update needs to be performed when possible.
	 */
	private boolean textUpdatePending = false;

	/**
	 * Indicates whether or not the next update should be animated or instantaneous.
	 */
	private boolean animateNextTextUpdate = false;

	/**
	 * The duration to use for animated updates, measured in milliseconds.
	 */
	private int animationDurationMs = 300;

	/**
	 * Whether or not this card is currently marked. This value is not reflected in the UI, as this
	 * responsibility is delegated to subclasses.
	 */
	private boolean marked = false;

	/**
	 * Whether or not this card is currently selected. This value is not reflected in the UI, as
	 * this responsibility is delegated to subclasses.
	 */
	private boolean selected = false;

	/**
	 * The current answer.
	 */
	private Answer answer = null;

	/**
	 * The current identifier.
	 */
	private CharSequence identifier = null;

	/**
	 * Whether or not the content description should automatically be updated to reflect the status
	 * and answer of the view.
	 */
	private boolean enableAutomaticContentDescriptions = true;

	/**
	 * Constructs a new SimpleAnswerCard. The marked and selected statuses are both set to false by
	 * default.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 */
	public SimpleAnswerCard(final Context context) {
		super(context);
		init();
	}

	/**
	 * Constructs a new SimpleAnswerCard. The marked and selected statuses are both set to false by
	 * default.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public SimpleAnswerCard(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Constructs a new SimpleAnswerCard. The marked and selected statuses are both set to false by
	 * default.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */
	public SimpleAnswerCard(final Context context, final AttributeSet attrs,
			final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	/**
	 * @return the CardView used for the main body of this view
	 */
	public CardView getCard() {
		return card;
	}

	/**
	 * @return the TextView used to display the answer
	 */
	public TextView getAnswerContainer() {
		return answerContainer;
	}

	/**
	 * @return the TextView used to display the identifier
	 */
	public TextView getIdentifierContainer() {
		return identifierContainer;
	}

	/**
	 * Sets the animation duration to use when updating the UI. The default is 300 milliseconds.
	 *
	 * @param animationDurationMs
	 * 		the duration to use, measured in milliseconds, at least 0
	 */
	public void setAnimationDurationMs(final int animationDurationMs) {
		this.animationDurationMs = IntChecker.checkGreaterThanOrEqualTo(animationDurationMs, 0,
				"animationDurationMs cannot be less than zero.");
	}

	/**
	 * @return the current animation duration, measured in milliseconds
	 */
	public int getAnimationDurationMs() {
		return animationDurationMs;
	}

	/**
	 * Enables/disables automatic content descriptions. If automatic descriptions are enabled, the
	 * content description is automatically set to reflect the current status and answer. If this
	 * option is disabled, the content description can be set externally.
	 *
	 * @param enable
	 * 		true to enable automatic content descriptions, false to disable them
	 */
	public void enableAutomaticContentDescriptions(final boolean enable) {
		enableAutomaticContentDescriptions = enable;

		// May have been false previously, so ensure content descriptions reflects new setting
		if (enable) {
			updateAccessibility();
		}
	}

	/**
	 * Returns whether or not the current answer is correct. If there is currently no answer, false
	 * is returned.
	 */
	public boolean answerIsCorrect() {
		return answer == null ? false : answer.isCorrect();
	}

	@Override
	public void setStatus(final boolean marked, final boolean selected, final boolean animate) {
		this.marked = marked;
		this.selected = selected;

		updateAccessibility();
	}

	@Override
	public void setMarkedStatus(final boolean marked, final boolean animate) {
		setStatus(marked, isSelected(), animate);
	}

	@Override
	public void setSelectedStatus(final boolean selected, final boolean animate) {
		setStatus(isMarked(), selected, animate);
	}

	@Override
	public boolean isMarked() {
		return marked;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setAnswer(final Answer answer, final boolean animate) {
		this.answer = answer;

		updateAccessibility();
		updateText(animate);
	}

	@Override
	public Answer getAnswer() {
		return answer;
	}

	@Override
	public void setIdentifier(final CharSequence identifier, final boolean animate) {
		this.identifier = identifier;

		updateAccessibility();
		updateText(animate);
	}

	@Override
	public CharSequence getIdentifier() {
		return identifier;
	}

	/**
	 * Initialises this view. The UI is inflated and default values are displayed.
	 */
	private void init() {
		inflate(getContext(), R.layout.single_answer_card, this);

		card = (CardView) findViewById(R.id.single_answer_card_root);
		identifierContainer = (TextView) findViewById(R.id.single_answer_card_identifier);
		answerContainer = (TextView) findViewById(R.id.single_answer_card_label);

		final int backgroundColor = card.getCardBackgroundColor().getDefaultColor();
		identifierContainer.setTextColor(ColorHelper.calculateBestTextColor(backgroundColor));
		answerContainer.setTextColor(ColorHelper.calculateBestTextColor(backgroundColor));

		updateAccessibility();
		updateText(false);
	}

	/**
	 * Updates the accessibility properties of this view.
	 */
	private void updateAccessibility() {
		if (enableAutomaticContentDescriptions) {
			if (answer == null) {
				setContentDescription(getResources().getString(R.string
						.single_answer_view_contdesc_desc_blank));
			} else {
				final String selectedText = getContext().getString(R.string
						.single_answer_view_contdesc_general_selected);
				final String unselectedText = getContext().getString(R.string
						.single_answer_view_contdesc_general_unselected);
				final String markedCorrectText = getContext().getString(R.string
						.single_answer_view_contdesc_general_marked_correct);
				final String markedIncorrectText = getContext().getString(R.string
						.single_answer_view_contdesc_general_marked_incorrect);
				final String unmarkedText = getContext().getString(R.string
						.single_answer_view_contdesc_general_unmarked);

				final String selectedVariable = selected ? selectedText : unselectedText;
				final String markedVariable = marked ?
						(answer.isCorrect() ? markedCorrectText : markedIncorrectText) :
						unmarkedText;

				setContentDescription(String.format(getContext().getString(
						R.string.single_answer_view_contdesc_general),
						selectedVariable,
						markedVariable));
			}
		}
	}

	/**
	 * Updates the UI to display the current answer and identifier. If this method is called again
	 * while animations from a previous invocation are still running, the current animations will
	 * complete before new ones are started.
	 *
	 * @param animate
	 * 		whether or not the UI update should be animated
	 */
	private void updateText(final boolean animate) {
		textUpdatePending = true;
		animateNextTextUpdate = animate;

		if (!textUpdateInProgress) {
			textUpdateInProgress = true;
			textUpdatePending = false;

			final CharSequence answerText = answer == null ? null : answer.getText();
			final boolean updateAnswer = !answerContainer.getText().equals(answerText);
			final boolean updateIdentifier = !identifierContainer.getText().equals(identifier);

			if (!animateNextTextUpdate || animationDurationMs == 0) {
				// Reset then set new values
				answerContainer.setText(null);
				identifierContainer.setText(null);
				answerContainer.setText(answerText);
				identifierContainer.setText(identifier);

				textUpdateInProgress = false;
			} else {
				final ValueAnimator fadeOutCurrent = ValueAnimator.ofFloat(0, 1);

				fadeOutCurrent.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(final ValueAnimator animation) {
						if (updateAnswer) {
							answerContainer.setAlpha(1 - animation.getAnimatedFraction());
						}

						if (updateIdentifier) {
							identifierContainer.setAlpha(1 - animation.getAnimatedFraction());
						}
					}
				});

				final ValueAnimator fadeInNew = ValueAnimator.ofFloat(0, 1);

				fadeInNew.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(final ValueAnimator animation) {
						if (updateAnswer) {
							answerContainer.setAlpha(animation.getAnimatedFraction());
						}

						if (updateIdentifier) {
							identifierContainer.setAlpha(animation.getAnimatedFraction());
						}
					}
				});

				fadeInNew.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationStart(final Animator animation) {
						// Reset then set new values
						answerContainer.setText(null);
						identifierContainer.setText(null);
						answerContainer.setText(answerText);
						identifierContainer.setText(identifier);
					}

					@Override
					public void onAnimationEnd(final Animator animation) {
						textUpdateInProgress = false;

						// If a new update was requested while the animation progressed, do it now
						if (textUpdatePending) {
							updateText(animateNextTextUpdate);
						}
					}
				});

				final AnimatorSet update = new AnimatorSet();
				update.play(fadeOutCurrent).before(fadeInNew);
				update.setDuration(animationDurationMs / 2);
				update.start();
			}
		}
	}
}