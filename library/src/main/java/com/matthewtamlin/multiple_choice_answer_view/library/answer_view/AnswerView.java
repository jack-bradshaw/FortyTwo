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
 * Displays an answer to the user, along with an identifier which differentiates the answer card
 * from others. Examples of identifiers are "A", "B", "C", "1", "2", "3" etc. Each card has two main
 * properties which influence how it is displayed: selected and marked. Selected is self
 * explanatory, and marked simply means that the card should display an indication of whether or not
 * the answer is correct.
 */
public interface AnswerView {
	/**
	 * Sets the of the answer view and updates the UI to match the change.
	 *
	 * @param marked
	 * 		true to mark the answer, false to unmark it
	 * @param selected
	 * 		true to select the answer, false to deselect it
	 * @param animate
	 * 		true to animate any UI changes, false to perform them instantaneously
	 */
	public void setStatus(boolean marked, boolean selected, boolean animate);

	/**
	 * Sets the marked status of the answer view without changing the selected status. The UI is
	 * updated to match the change.
	 *
	 * @param marked
	 * 		true to mark the answer, false to unmark it
	 * @param animate
	 * 		true to animate any UI changes, false to perform them instantaneously
	 */
	public void setMarkedStatus(boolean marked, boolean animate);

	/**
	 * Sets the selected status of the answer view without changing the marked status. The UI is
	 * updated to match the change.
	 *
	 * @param selected
	 * 		true to select the answer, false to deselect it
	 * @param animate
	 * 		true to animate any UI changes, false to perform them instantaneously
	 */
	public void setSelectedStatus(boolean selected, boolean animate);

	/**
	 * @return true if the view is currently displaying as marked, false otherwise
	 */
	public boolean isMarked();

	/**
	 * @return true if the user has selected the view, false otherwise
	 */
	public boolean isSelected();

	/**
	 * Sets the answer to display in the view. If null is supplied, then no answer is displayed.
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
	 * Sets the identifier to display in the view. If null is supplied, then no identifier is
	 * displayed.
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