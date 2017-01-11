package com.matthewtamlin.multiple_choice_answer_view.library.answer_group;

import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.AnswerGroup.Listener;
import com.matthewtamlin.multiple_choice_answer_view.library.util.Listenable;

import java.util.List;

public interface AnswerGroup<V extends AnswerView> extends Listenable<Listener<V>> {
	public void addAnswers(List<V> answers);

	public void addAnswer(V answer);

	public void removeAnswer(V answer);

	public void clearAnswers();

	public List<V> getAnswers();

	public void allowSelectionChangesWhenMarked(boolean allow);

	public boolean selectionChangesAreAllowedWhenMarked();

	public void declareExternalViewSelectionChanges();

	public interface Listener<V extends AnswerView> {
		public void onAnswerSelected(final V selectedView);

		public void onAnswerDeselected(final V deselectedView);
	}
}