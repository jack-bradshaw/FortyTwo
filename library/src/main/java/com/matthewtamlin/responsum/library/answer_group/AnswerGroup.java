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

package com.matthewtamlin.responsum.library.answer_group;

import com.matthewtamlin.responsum.library.answer_group.AnswerGroup.Listener;
import com.matthewtamlin.responsum.library.answer_view.AnswerView;
import com.matthewtamlin.responsum.library.util.Listenable;

import java.util.Collection;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Contains and coordinates a collection of AnswerViews. The group listens for clicks on the
 * contained views and responds by selecting and deselecting views as necessary. The group provides
 * the option of ignoring selection changes for views which are currently marked as selected.
 * <p>
 * When using this view, two conditions must be satisfied:<iul> <li>The {@link
 * #allowSelectionChangesWhenMarked(boolean)} method must be called immediately after a contained
 * view is selected or deselected externally.</li> <li>The on click listeners of the contained views
 * must not be modified externally.</li>
 * <p>
 * Failure to satisfy both conditions at any time may result in malfunction.
 */
public interface AnswerGroup extends Listenable<Listener> {
	/**
	 * Adds a collection of answer views to this group. The order of the collection determines the
	 * order in which the views are added. The supplied collection must not be null and must not
	 * contain null.
	 *
	 * @param answers
	 * 		the answer views to add, not null, not containing null
	 */
	public void addAnswers(Collection<? extends AnswerView> answers);

	/**
	 * Adds the supplied answer view to this group. The supplied view must not be null.
	 *
	 * @param answer
	 * 		the answer to add, not null
	 */
	public void addAnswer(AnswerView answer);

	/**
	 * Removes the supplied answer view from this group. If the supplied answer is not contained in
	 * the group then the method exists normally.
	 *
	 * @param answer
	 * 		the answer to remove, not null
	 */
	public void removeAnswer(AnswerView answer);

	/**
	 * Removes all answers views currently in this group.
	 */
	public void clearAnswers();

	/**
	 * Returns a collection containing all answer views currently in this group. Implementations may
	 * return an unmodifiable list. The returned list may be empty, but it will never be null.
	 *
	 * @return the collection of views
	 */
	public List<AnswerView> getAnswers();

	/**
	 * Enables/disables selection changes on all answer views which are marked. Clicks on marked
	 * views will not change the selection status if this is enabled.
	 *
	 * @param allow
	 * 		whether or not to allow marked views to have selection changes
	 */
	public void allowSelectionChangesWhenMarked(boolean allow);

	/**
	 * @return true if the group is currently set to allow selection changes on marked views, false
	 * otherwise
	 */
	public boolean selectionChangesAreAllowedWhenMarked();

	/**
	 * Declares to this view that a contained answer view has been selected or deselected
	 * externally. This method must be called whenever such an event occurs. Calling it when no
	 * external selection change has occurred has no effect, however excessive calls will limit
	 * performance.
	 */
	public void declareExternalViewSelectionChanges();

	/**
	 * Callback listener to be called when an AnswerView in an AnswerGroup is selected or
	 * deselected.
	 */
	public interface Listener {
		/**
		 * Invoked when an AnswerView is selected in the AnswerGroup this listener is registered
		 * to.
		 *
		 * @param answerGroup
		 * 		the AnswerGroup containing the selected view, not null
		 * @param selectedView
		 * 		the view which was selected, not null
		 */
		public void onAnswerSelected(AnswerGroup answerGroup, AnswerView selectedView);

		/**
		 * Invoked when an AnswerView is deselected in the AnswerGroup this listener is registered
		 * to.
		 *
		 * @param answerGroup
		 * 		the AnswerGroup containing the deselected view, not null
		 * @param deselectedView
		 * 		the view which was deselected, not null
		 */
		public void onAnswerDeselected(AnswerGroup answerGroup, AnswerView deselectedView);
	}
}