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
import android.animation.ValueAnimator;

import com.matthewtamlin.android_utilities.library.helpers.ColorHelper;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.DecoratedAnswerCard.Decorator;

import static com.matthewtamlin.android_utilities.library.helpers.ColorHelper.calculateBestTextColor;
import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * A {@link Decorator} which changes the colors of an answer card, specifically the card background
 * color and the text colors. The card background colors are defined by supplying a {@link
 * ColorSupplier} to the constructor, and the text colors are automatically chosen to maximise
 * readability against the background. If animations are enabled, the colors are transitioned
 * smoothly.
 */
public class ColorFadeDecorator extends DecoratorAdapter {
	/**
	 * Supplies the background colors to use.
	 */
	private final ColorSupplier colorSupplier;

	/**
	 * Indicates whether or not a decoration is currently being applied to a view. This could be an
	 * instantaneous update on the UI thread, or an asynchronous update using animators.
	 */
	private boolean updateInProgress = false;

	/**
	 * Indicates whether or not an update needs to be performed.
	 */
	private boolean updatePending = false;

	/**
	 * Indicates whether or not the next update should be animated or instantaneous.
	 */
	private boolean animateNextUpdate = false;

	/**
	 * Constructs a new ColorFadeDecorator.
	 *
	 * @param colorSupplier
	 * 		defines the card background colors, not null
	 * @throws IllegalArgumentException
	 * 		if {@code colorSupplier} is null
	 */
	public ColorFadeDecorator(final ColorSupplier colorSupplier) {
		this.colorSupplier = checkNotNull(colorSupplier, "colorSupplier cannot be null.");
	}

	/**
	 * @return the current color supplier
	 */
	public ColorSupplier getColorSupplier() {
		return colorSupplier;
	}

	@Override
	public void decorate(final DecoratedAnswerCard cardToDecorate, final boolean animate) {
		checkNotNull(cardToDecorate, "cardToDecorate cannot be null.");

		updatePending = true;
		animateNextUpdate = animate;

		if (!updateInProgress) {
			updateBackgroundColor(cardToDecorate);
		}
	}

	/**
	 * Applies the decoration to the supplied card. If this method is called again while animations
	 * from a previous invocation are still running, the current animations will complete before new
	 * ones are started.
	 *
	 * @param cardToDecorate
	 * 		the card to apply the decoration to, not null
	 */
	private void updateBackgroundColor(final DecoratedAnswerCard cardToDecorate) {
		updateInProgress = true;
		updatePending = false;

		final int startBackground = cardToDecorate.getCard().getCardBackgroundColor()
				.getDefaultColor();
		final int targetBackground = colorSupplier.getColor(cardToDecorate.isMarked(),
				cardToDecorate.isSelected(), cardToDecorate.answerIsCorrect());

		final int startTextColor = cardToDecorate.getAnswerContainer().getCurrentTextColor();
		final int targetTextColor = calculateBestTextColor(targetBackground);

		if (!animateNextUpdate || getAnimationDurationMs() == 0) {
			cardToDecorate.getCard().setCardBackgroundColor(targetBackground);
			cardToDecorate.getAnswerContainer().setTextColor(targetTextColor);
			cardToDecorate.getIdentifierContainer().setTextColor(targetTextColor);

			updateInProgress = false;

		} else {
			final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

			animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(final ValueAnimator animation) {
					// Set background color by proportionally blending start and target colors
					final int backgroundColor = ColorHelper.blendColors(startBackground,
							targetBackground, animation.getAnimatedFraction());
					cardToDecorate.getCard().setCardBackgroundColor(backgroundColor);

					// Set text color by proportionally blending start and target colors
					final int textColor = ColorHelper.blendColors(startTextColor,
							targetTextColor, animation.getAnimatedFraction());
					cardToDecorate.getAnswerContainer().setTextColor(textColor);
					cardToDecorate.getIdentifierContainer().setTextColor(textColor);

				}
			});

			animator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(final Animator animation) {
					updateInProgress = false;

					// If a new update was requested while the animation progressed, do it now
					if (updatePending) {
						updateBackgroundColor(cardToDecorate);
					}
				}
			});

			animator.setDuration(getAnimationDurationMs());
			animator.start();
		}
	}

	/**
	 * Supplies the colors to use in a ColorFadeDecorator.
	 */
	public static interface ColorSupplier {
		/**
		 * Supplies the target background color for a card with the given properties.
		 *
		 * @param marked
		 * 		whether or not the card is currently marked
		 * @param selected
		 * 		whether or not the card is currently selected
		 * @param answerIsCorrect
		 * 		whether or not the answer displayed in the card is correct
		 * @return the color to use for the background, as an ARGB hex code
		 */
		public int getColor(boolean marked, boolean selected, boolean answerIsCorrect);
	}
}