package com.matthewtamlin.fortytwo.example;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.matthewtamlin.fortytwo.library.answer_group.SelectionLimitedAnswerGroup;
import com.matthewtamlin.fortytwo.library.answer_view.AnswerView;

/**
 * Displays a question, an selection or answers, and a submit/reset button.
 */
@SuppressLint("SetTextI18n") // Just an example, not a real app
public class AbstractQuestionActivity extends AppCompatActivity {
	/**
	 * Contains the question.
	 */
	private TextView questionContainer;

	/**
	 * Contains the answers.
	 */
	private SelectionLimitedAnswerGroup answerGroup;

	/**
	 * Button for submitting and resetting the answers.
	 */
	private Button actionButton;

	/**
	 * Whether or not the answers are currently submitted.
	 */
	private boolean currentlyMarked = false;

	@Override
	protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_activity);

		questionContainer = (TextView) findViewById(R.id.main_Activity_question_container);
		answerGroup = (SelectionLimitedAnswerGroup) findViewById(R.id.main_activity_answer_group);
		actionButton = (Button) findViewById(R.id.main_activity_action_button);

		actionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				for (final AnswerView answerView : answerGroup.getAnswers()) {
					answerView.setMarkedStatus(!currentlyMarked, true);
				}

				currentlyMarked = !currentlyMarked;

				if (currentlyMarked) {
					actionButton.setText("Unsubmit");
				} else {
					actionButton.setText("Submit");
				}
			}
		});
	}

	public TextView getQuestionContainer() {
		return questionContainer;
	}

	public SelectionLimitedAnswerGroup getAnswerGroup() {
		return answerGroup;
	}
}
