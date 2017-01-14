package com.matthewtamlin.multiple_choice_answer_view.library.answer;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * An immutable implementation of the Answer interface. The values set at instantiation cannot be
 * changes.
 */
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