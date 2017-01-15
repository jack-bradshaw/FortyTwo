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

import com.matthewtamlin.java_utilities.checkers.IntChecker;

/**
 * A partial implementation of the Decorator interface which handles getting and setting the
 * animation duration. The default duration is 300 milliseconds. The implementation of the {@link
 * #decorate(DecoratedAnswerCard, boolean)} method is deferred to subclasses.
 */
public abstract class DecoratorAdapter implements DecoratedAnswerCard.Decorator {
	/**
	 * The duration to use for decoration animations, measured in milliseconds.
	 */
	private int animationDurationMs = 300;

	@Override
	public int getAnimationDurationMs() {
		return animationDurationMs;
	}

	@Override
	public void setAnimationDurationMs(final int animationDurationMs) {
		this.animationDurationMs = IntChecker.checkGreaterThanOrEqualTo(animationDurationMs, 0,
				"animationDurationMs cannot be less than zero.");
	}
}