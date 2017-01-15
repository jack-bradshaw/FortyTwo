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

package com.matthewtamlin.multiple_choice_answer_view.library.answer;

import com.matthewtamlin.android_utilities.library.testing.Tested;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * An immutable implementation of the Answer interface. The values set at instantiation cannot be
 * changes.
 */
@Tested(testMethod = "automated", requiresInstrumentation = false)
public final class ImmutableAnswer implements Answer {
	/**
	 * The actual text of the answer.
	 */
	private final CharSequence text;

	/**
	 * Whether or not the answer is correct.
	 */
	private final boolean correctness;

	/**
	 * Constructs a new ImmutableAnswer.
	 *
	 * @param text
	 * 		the actual text of this answer, not null
	 * @param correctness
	 * 		whether or not this answer is correct
	 * @throws IllegalArgumentException
	 * 		if {@code text} is null
	 */
	public ImmutableAnswer(final CharSequence text, final boolean correctness) {
		this.text = checkNotNull(text, "test cannot be null.");
		this.correctness = correctness;
	}

	@Override
	public CharSequence getText() {
		return text;
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
		} else if (obj.getClass() == ImmutableAnswer.class) {
			final ImmutableAnswer input = (ImmutableAnswer) obj;
			return text.equals(input.text) && (correctness == input.correctness);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "ImmutableAnswer {" +
				"\n\ttext=" + text +
				"\n\tcorrectness=" + correctness +
				'}';
	}
}