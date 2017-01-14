package com.matthewtamlin.multiple_choice_answer_view.library.answer;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * A POJO implementation of the Answer interface.
 */
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