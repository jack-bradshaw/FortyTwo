package com.matthewtamlin.multiple_choice_answer_view.library.answer;

/**
 * An answer to a question. The answer is made of two parts: <ul><li>The actual text of
 * the answer.</li><li>Whether or not the answer is correct.</li></ul>
 */
public interface Answer {
	/**
	 * @return the actual text of the answer, not null
	 */
	public CharSequence getText();

	/**
	 * @return whether or not this answer is correct
	 */
	public boolean isCorrect();
}