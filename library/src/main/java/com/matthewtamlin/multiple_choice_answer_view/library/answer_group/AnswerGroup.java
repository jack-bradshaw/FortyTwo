package com.matthewtamlin.multiple_choice_answer_view.library.answer_group;

import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;
import com.matthewtamlin.multiple_choice_answer_view.library.util.Listenable;

import java.util.List;

public interface AnswerGroup extends Listenable<AnswerGroup.SelectionListener> {
	public void addAnswers(List<AnswerView> content);

	public List<AnswerView> getContent();

	public void addAnswer(AnswerView answer);

	public void removeAnswer(AnswerView answer);

	public void allowSelectionChangesWhenMarked(boolean allow);

	public boolean selectionChangesAreAllowedWhenMarked();

	public void declareExternalViewSelectionChanges();

	public interface SelectionListener {
		public void onAnswerSelected(final AnswerView selectedView);

		public void onAnswerDeselected(final AnswerView deselectedView);
	}
}
