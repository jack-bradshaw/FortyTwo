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

import android.content.Context;
import android.util.AttributeSet;

import com.matthewtamlin.java_utilities.checkers.IntChecker;
import com.matthewtamlin.multiple_choice_answer_view.library.answer.Answer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An implementation of the AnswerCard interface which can be customised by supplying one or more
 * decorators. Any decorator supplied to {@link #addDecorator(Decorator, boolean)} will be called
 * upon whenever the status, answer or identifier changes.
 */
public class DecoratedAnswerCard extends SimpleAnswerCard {
	/**
	 * All decorators currently registered with the card.
	 */
	private final Set<Decorator> decorators = new HashSet<>();

	/**
	 * Constructs a new DecoratedAnswerCard. The marked and selected statuses are both set to false
	 * by default.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 */
	public DecoratedAnswerCard(final Context context) {
		super(context);
	}

	/**
	 * Constructs a new DecoratedAnswerCard. The marked and selected statuses are both set to false
	 * by default.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public DecoratedAnswerCard(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructs a new DecoratedAnswerCard. The marked and selected statuses are both set to false
	 * by default.
	 *
	 * @param context
	 * 		the context this view is operating in, not null
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */
	public DecoratedAnswerCard(final Context context, final AttributeSet attrs,
			final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * Registers a decorator with this view and calls its {@link Decorator#decorate
	 * (DecoratedAnswerCard, boolean)} method immediately. If the decorator is null or has already
	 * been added, then there is no effect and the method returns immediately.
	 *
	 * @param decorator
	 * 		the decorator to add
	 * @param animate
	 * 		whether or not the initial decoration should be animated
	 */
	public void addDecorator(final Decorator decorator, boolean animate) {
		if (decorator != null) {
			decorators.add(decorator);
			decorator.setAnimationDurationMs(getAnimationDurationMs());
			decorator.decorate(this, animate);
		}
	}

	/**
	 * Removes a decorator from this view. If the decorator is null or has not been added, then
	 * there is no effect and the method returns immediately. If the decorator has modified the view
	 * in some way, those modifications are not reversed.
	 *
	 * @param decorator
	 * 		the decorator to remove
	 */
	public void removeDecorator(final Decorator decorator) {
		decorators.remove(decorator);
	}

	/**
	 * Removes all decorators from the view. If the decorators have modified the view in some way,
	 * those modifications are not reversed.
	 */
	public void clearDecorators() {
		decorators.clear();
	}

	/**
	 * Returns an unmodifiable set containing all decorators currently registered with this view.
	 * The set may be empty but it will never be null.
	 *
	 * @return the set of all current decorators
	 */
	public Set<Decorator> getDecorators() {
		return Collections.unmodifiableSet(decorators);
	}

	@Override
	public void setStatus(final boolean marked, final boolean selected, final boolean animate) {
		super.setStatus(marked, selected, animate);

		for (final Decorator decorator : decorators) {
			decorator.decorate(this, animate);
		}
	}

	@Override
	public void setAnswer(final Answer answer, final boolean animate) {
		super.setAnswer(answer, animate);

		for (final Decorator decorator : decorators) {
			decorator.decorate(this, animate);
		}
	}

	@Override
	public void setAnimationDurationMs(int animationDurationMs) {
		IntChecker.checkGreaterThanOrEqualTo(animationDurationMs, 0, "animationDurationMs cannot " +
				"be less than zero.");

		super.setAnimationDurationMs(animationDurationMs);

		for (final Decorator decorator : decorators) {
			decorator.setAnimationDurationMs(animationDurationMs);
		}
	}

	/**
	 * Applies decoration to a single {@link DecoratedAnswerCard} via the {@link
	 * DecoratedAnswerCard#addDecorator(Decorator, boolean)} method. In general, it is not safe to
	 * apply the same decorator to multiple views simultaneously.
	 */
	public interface Decorator {
		/**
		 * Sets the duration to use for any animations this decorator performs when {@link
		 * #decorate(DecoratedAnswerCard, boolean)} is called.
		 *
		 * @param animationDurationMs
		 * 		the duration to use, measured in milliseconds
		 */
		public void setAnimationDurationMs(int animationDurationMs);

		/**
		 * Returns the duration currently used for any animations this decorator performs when
		 * {@link #decorate(DecoratedAnswerCard, boolean)} is called.
		 *
		 * @return the current animation duration, measured in milliseconds
		 */
		public int getAnimationDurationMs();

		/**
		 * Applies the decoration to the supplied card. This method may start animations, and it may
		 * be called again before the animations finish.
		 *
		 * @param cardToDecorate
		 * 		the card to apply the decoration to, not null
		 * @param animate
		 * 		whether or not decoration animations should be enabled
		 */
		public void decorate(DecoratedAnswerCard cardToDecorate, boolean animate);
	}
}