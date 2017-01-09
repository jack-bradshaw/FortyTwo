package com.matthewtamlin.multiple_choice_answer_view.library.answer_group;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleAnswerGroup extends LinearLayout implements AnswerGroup {
	private final Set<SelectionListener> listeners = new HashSet<>();

	private final List<AnswerView> content = new ArrayList<>();

	private EvictingStack<AnswerView> selectedViews = new EvictingStack<>(1);

	public SimpleAnswerGroup(final Context context) {
		super(context);
		init();
	}

	public SimpleAnswerGroup(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SimpleAnswerGroup(final Context context, final AttributeSet attrs,
			final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@RequiresApi(21) // For caller
	@TargetApi(21) // For lint
	public SimpleAnswerGroup(final Context context, final AttributeSet attrs,
			final int defStyleAttr, final int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	@Override
	public void setContent(List<AnswerView> content) {
		this.content.clear();
		this.content.addAll(content);

		updateUI();
	}

	@Override
	public List<AnswerView> getContent() {
		return null;
	}

	@Override
	public void setMultipleSelectionLimit(final int limit) {
		selectedViews =  new EvictingStack<>(limit, selectedViews);
	}

	@Override
	public int getMultipleSelectionLimit() {
		return sel;
	}

	@Override
	public void registerListener(final SelectionListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	@Override
	public void unregisterListener(final SelectionListener listener) {
		listeners.remove(listener);
	}

	private void init() {
		setOrientation(VERTICAL);
	}

	private void updateUI() {
		removeAllViews();

		for (final AnswerView answerView : content) {
			final View asView = (View) answerView;

			addView((View) answerView);
			((View) answerView).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					handleClick(answerView);
				}
			});
		}
	}

	private void handleClick(final AnswerView clickedAnswerCard) {

	}
}
