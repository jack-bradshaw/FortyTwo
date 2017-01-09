package com.matthewtamlin.multiple_choice_answer_view.library.answer_group;

import android.util.Pair;

import com.matthewtamlin.multiple_choice_answer_view.library.answer.Answer;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;
import com.matthewtamlin.multiple_choice_answer_view.library.util.Listenable;

import java.util.List;

public interface AnswerGroup extends Listenable<AnswerGroup.SelectionListener> {
	public void setContent(List<AnswerView> content);

	public List<AnswerView> getContent();

	public void setMultipleSelectionLimit(int limit);

	public int getMultipleSelectionLimit();

	public void allowMarkedViewsToBeSelected(boolean allow);

	public boolean markedViewsAreAllowedToBeSelected();

	public interface SelectionListener {
		public void onAnswerSelected();

		public void onAnswerDeselected();
	}
}
