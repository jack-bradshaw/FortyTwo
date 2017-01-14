package com.matthewtamlin.multiple_choice_answer_view.library.answer_group;

import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.AnswerGroup.Listener;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;
import com.matthewtamlin.multiple_choice_answer_view.library.util.Listenable;

import java.util.Collection;
import java.util.List;

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
 *
 * @param <V>
 * 		the type of AnswerViews contained
 */
public interface AnswerGroup<V extends AnswerView> extends Listenable<Listener<V>> {
	/**
	 * Adds a collection of answer views to this group. The order of the collection determines the
	 * order in which the views are added. The supplied collection must not be null and must not
	 * contain null.
	 *
	 * @param answers
	 * 		the answer views to add, not null, not containing null
	 */
	public void addAnswers(Collection<V> answers);

	/**
	 * Adds the supplied answer view to this group. The supplied view must not be null.
	 *
	 * @param answer
	 * 		the answer to add, not null
	 */
	public void addAnswer(V answer);

	/**
	 * Removes the supplied answer view from this group. If the supplied answer is not contained in
	 * the group then the method exists normally.
	 *
	 * @param answer
	 * 		the answer to remove, not null
	 */
	public void removeAnswer(V answer);

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
	public List<V> getAnswers();

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
	 *
	 * @param <V>
	 * 		the type of answer views in the AnswerGroup
	 */
	public interface Listener<V extends AnswerView> {
		/**
		 * Invoked when an AnswerView is selected in the AnswerGroup this listener is registered
		 * to.
		 *
		 * @param answerGroup
		 * 		the AnswerGroup containing the selected view, not null
		 * @param selectedView
		 * 		the view which was selected, not null
		 */
		public void onAnswerSelected(final AnswerGroup<V> answerGroup, final V selectedView);

		/**
		 * Invoked when an AnswerView is deselected in the AnswerGroup this listener is registered
		 * to.
		 *
		 * @param answerGroup
		 * 		the AnswerGroup containing the deselected view, not null
		 * @param deselectedView
		 * 		the view which was deselected, not null
		 */
		public void onAnswerDeselected(final AnswerGroup<V> answerGroup, final V deselectedView);
	}
}