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

import com.matthewtamlin.multiple_choice_answer_view.library.answer.Answer;

/**
 * Displays an answer and an identifier to the user. An identifier is a CharSequence which
 * differentiates the answer amongst a series of others (e.g. "A", "B", "C", "1", "2", "3" etc.).
 * <p>
 * Each answer view has two main properties: selected and marked. Selected simply means the user has
 * selected this view. Marked means that the view is displaying whether or not the user's selection
 * is correct.
 */
public interface AnswerView {
	/**
	 * Sets the status of the answer view. The UI is updated to reflect the status.
	 *
	 * @param marked
	 * 		true if the answer is marked, false otherwise
	 * @param selected
	 * 		true if the user has selected this answer as correct, false otherwise
	 * @param animate
	 * 		true to animate any UI changes, false to perform them instantaneously
	 */
	public void setStatus(boolean marked, boolean selected, boolean animate);

	/**
	 * @return true if the view is currently displaying as marked, false otherwise
	 */
	public boolean isMarked();

	/**
	 * @return true if the user has selected the view, false otherwise
	 */
	public boolean isSelected();

	/**
	 * Sets the answer to display in the view.
	 *
	 * @param answer
	 * 		the answer to display, may be null
	 * @param animate
	 * 		true to animate any UI changes, false to perform them instantaneously
	 */
	public void setAnswer(Answer answer, boolean animate);

	/**
	 * @return the answer currently displayed in the view, null if there is none
	 */
	public Answer getAnswer();

	/**
	 * Sets the identifier to display in the view.
	 *
	 * @param identifier
	 * 		the identifier to display, may be null
	 * @param animate
	 * 		true to animate any UI changes, false to perform them instantaneously
	 */
	public void setIdentifier(CharSequence identifier, boolean animate);

	/**
	 * @return the identifier currently displayed in the view
	 */
	public CharSequence getIdentifier();
}