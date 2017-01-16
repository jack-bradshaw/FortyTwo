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

package com.matthewtamlin.responsum.library.answer;

import com.matthewtamlin.android_utilities.library.testing.Tested;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * A POJO implementation of the Answer interface.
 */
@Tested(testMethod = "automated", requiresInstrumentation = false)
public class PojoAnswer implements Answer {
	/**
	 * The actual text of the answer.
	 */
	private CharSequence text;

	/**
	 * Whether or not the answer is correct.
	 */
	private boolean correctness;

	/**
	 * Constructs a new PojoAnswer.
	 *
	 * @param text
	 * 		the actual text of the answer, not null
	 * @param correctness
	 * 		whether or not the answer is correct
	 * @throws IllegalArgumentException
	 * 		if {@code text} is null
	 */
	public PojoAnswer(final CharSequence text, final boolean correctness) {
		this.text = checkNotNull(text, "text cannot be null.");
		this.correctness = correctness;
	}

	/**
	 * Sets the text of this answer
	 *
	 * @param text
	 * 		the actual text of this answer, not null
	 * @throws IllegalArgumentException
	 * 		if {@code text} is null
	 */
	public void setText(final CharSequence text) {
		this.text = checkNotNull(text, "text cannot be null.");
	}

	@Override
	public CharSequence getText() {
		return text;
	}

	/**
	 * Sets whether or not this answer is correct.
	 *
	 * @param correctness
	 * 		true if this answer is correct, false otherwise
	 */
	public void setCorrectness(final boolean correctness) {
		this.correctness = correctness;
	}

	@Override
	public boolean isCorrect() {
		return correctness;
	}

	@Override
	public int hashCode() {
		return text.hashCode() * (correctness ? 1 : -1);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		} else if (obj == this) {
			return true;
		} else if (obj instanceof Answer) {
			final PojoAnswer input = (PojoAnswer) obj;
			return text.equals(input.text) && (correctness == input.correctness);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return text.toString() + (correctness ? " (correct)" : " (incorrect)");
	}
}