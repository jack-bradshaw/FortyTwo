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
 * 		the type of AnswerViews container
 */
public interface AnswerGroup<V extends AnswerView> extends Listenable<Listener<V>> {
	/**
	 * Adds all views in the supplied collection. The order of the collection determines the order
	 * in which the views are added. The supplied collection must not be null and must not contain
	 * null.
	 *
	 * @param answers
	 * 		the views to add, not null, not containing null
	 */
	public void addAnswers(Collection<V> answers);

	/**
	 * Adds a single answer to the view. The supplied answer must not be null
	 *
	 * @param answer
	 * 		the answer to add, not null
	 */
	public void addAnswer(V answer);

	public void removeAnswer(V answer);

	public void clearAnswers();

	public List<V> getAnswers();

	public void allowSelectionChangesWhenMarked(boolean allow);

	public boolean selectionChangesAreAllowedWhenMarked();

	public void declareExternalViewSelectionChanges();

	public interface Listener<V extends AnswerView> {
		public void onAnswerSelected(final AnswerGroup<V> answerGroup, final V selectedView);

		public void onAnswerDeselected(final AnswerGroup<V> answerGroup, final V deselectedView);
	}
}