package com.matthewtamlin.fortytwo.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.matthewtamlin.fortytwo.library.answer_group.AnswerGroup;
import com.matthewtamlin.fortytwo.library.answer_group.SelectionLimitedAnswerGroup;

public class AbstractQuestionActivity extends AppCompatActivity {
	/**
	 * TextView which contains the question.
	 */
	private TextView questionContainer;

	/**
	 * AnswerGroup which contains the answers.
	 */
	private AnswerGroup answerGroup;

	/**
	 * A button for marking and unmarking the answers.
	 */
	private Button actionButton;

	/**
	 * Whether or not the answers are currently marked.
	 */
	private boolean currentlyMarked = false;

	@Override
	protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_activity);

		questionContainer = (TextView) findViewById(R.id.main_Activity_question_container);
		answerGroup = (SelectionLimitedAnswerGroup) findViewById(R.id.main_activity_answer_group);
		actionButton = (Button) findViewById(R.id.main_activity_action_button);
	}
}
