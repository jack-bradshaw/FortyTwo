package com.matthewtamlin.multiple_choice_answer_view.library.answer_group;

import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;
import com.matthewtamlin.multiple_choice_answer_view.library.util.Listenable;

import java.util.List;

public interface AnswerGroup extends Listenable<AnswerGroup.SelectionListener> {
	public void setContent(List<AnswerView> content);

	public List<AnswerView> getContent();

	public void setMultipleSelectionLimit(int limit, boolean animate);

	public int getMultipleSelectionLimit();

	public void allowSelectionChangesWhenMarked(boolean allow);

	public boolean selectionChangesAreAllowedWhenMarked();

	public void markAll(boolean animate);

	public void unmarkAll(boolean animate);

	public void declareExternalViewSelectionChanges();

	public interface SelectionListener {
		public void onAnswerSelected(final AnswerView selectedView);

		public void onAnswerDeselected(final AnswerView deselectedView);
	}
}
