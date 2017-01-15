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

import com.matthewtamlin.android_utilities.library.testing.Tested;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.DecoratedAnswerCard.Decorator;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * A {@link Decorator} which changes the transparency of the target answer card. The alpha values
 * are defined by supplying an {@link AlphaDecorator.AlphaSupplier} to the constructor. If
 * animations are enabled, the transparency is transitioned smoothly.
 */
@Tested(testMethod = "automated", requiresInstrumentation = true)
public class AlphaDecorator extends DecoratorAdapter {
	/**
	 * Supplies the alpha values.
	 */
	private final AlphaSupplier alphaSupplier;

	/**
	 * Indicates whether or not a decoration is currently being applied. This could be an
	 * instantaneous update on the UI thread, or an asynchronous update using animators.
	 */
	private boolean updateInProgress = false;

	/**
	 * Indicates whether or not an update needs to be performed when possible.
	 */
	private boolean updatePending = false;

	/**
	 * Indicates whether or not the next update should be animated or instantaneous.
	 */
	private boolean animateNextUpdate = false;

	/**
	 * Constructs a new AlphaDecorator.
	 *
	 * @param alphaSupplier
	 * 		supplies the alpha values to use when decorating, not null
	 * @throws IllegalArgumentException
	 * 		if {@code alphaSupplier} is null
	 */
	public AlphaDecorator(final AlphaSupplier alphaSupplier) {
		this.alphaSupplier = checkNotNull(alphaSupplier, "alphaSupplier cannot be null.");
	}

	@Override
	public void decorate(final DecoratedAnswerCard cardToDecorate, final boolean animate) {
		checkNotNull(cardToDecorate, "cardToDecorate cannot be null.");

		updatePending = true;
		animateNextUpdate = animate;

		if (!updateInProgress) {
			updateAlpha(cardToDecorate);
		}
	}

	/**
	 * Applies the decoration to the supplied card, using animations if necessary. If this method is
	 * called again while animations from a previous invocation are still running, the current
	 * animations will complete before new ones are started.
	 *
	 * @param cardToDecorate
	 * 		the card to apply the decoration to, not null
	 */
	private void updateAlpha(final DecoratedAnswerCard cardToDecorate) {
		updateInProgress = true;
		updatePending = false;

		// Get the target and current alphas now to avoid thread interference when animating
		final float startAlpha = cardToDecorate.getAlpha();
		final float targetAlpha = alphaSupplier.getAlpha(cardToDecorate.isMarked(),
				cardToDecorate.isSelected(), cardToDecorate.answerIsCorrect());
		final float alphaDifference = targetAlpha - startAlpha;

		if (!animateNextUpdate || getAnimationDurationMs() == 0) {
			cardToDecorate.setAlpha(targetAlpha);

			updateInProgress = false;

		} else {
			final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

			animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(final ValueAnimator animation) {
					final float fraction = animation.getAnimatedFraction();
					cardToDecorate.setAlpha(startAlpha + (fraction * alphaDifference));
				}
			});

			animator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(final Animator animation) {
					updateInProgress = false;

					// If a new update was requested while the animation progressed, do it now
					if (updatePending) {
						updateAlpha(cardToDecorate);
					}
				}
			});

			animator.setDuration(getAnimationDurationMs());
			animator.start();
		}
	}

	/**
	 * Supplies the alpha values to use in an AlphaDecorator.
	 */
	public interface AlphaSupplier {
		/**
		 * Supplies the target alpha value for a card with the given properties.
		 *
		 * @param marked
		 * 		whether or not the card is currently marked
		 * @param selected
		 * 		whether or not the card is currently selected
		 * @param answerIsCorrect
		 * 		whether or not the answer displayed in the card is correct
		 * @return the alpha value to use for the card, between 0 and 1 inclusive
		 */
		public float getAlpha(boolean marked, boolean selected, boolean answerIsCorrect);
	}
}