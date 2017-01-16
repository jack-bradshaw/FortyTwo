package com.matthewtamlin.responsum.example;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matthewtamlin.responsum.library.answer.Answer;
import com.matthewtamlin.responsum.library.answer.ImmutableAnswer;
import com.matthewtamlin.responsum.library.answer_group.AnswerGroup;
import com.matthewtamlin.responsum.library.answer_group.SelectionLimitedAnswerGroup;
import com.matthewtamlin.responsum.library.answer_view.AlphaDecorator;
import com.matthewtamlin.responsum.library.answer_view.AlphaDecorator.AlphaSupplier;
import com.matthewtamlin.responsum.library.answer_view.AnswerView;
import com.matthewtamlin.responsum.library.answer_view.ColorFadeDecorator;
import com.matthewtamlin.responsum.library.answer_view.ColorFadeDecorator.ColorSupplier;
import com.matthewtamlin.responsum.library.answer_view.DecoratedAnswerCard;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity {
	private static final String QUESTION = "What is the square root of -1?";

	private static final Answer[] ANSWERS = {
			new ImmutableAnswer("The imaginary unit, i", true),
			new ImmutableAnswer("NaN", false),
			new ImmutableAnswer("1", false),
			new ImmutableAnswer("-1", false),
			new ImmutableAnswer("Pi", false)};

	private TextView questionContainer;

	private AnswerGroup<DecoratedAnswerCard> answerGroup;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createUi();

		questionContainer.setText(QUESTION);

		for (final Answer a : ANSWERS) {
			final DecoratedAnswerCard decoratedAnswerCard = new DecoratedAnswerCard(this);
			decoratedAnswerCard.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));

			decoratedAnswerCard.setAnswer(a, false);
			decoratedAnswerCard.addDecorator(createColorFadeDecorator(), false);
			decoratedAnswerCard.addDecorator(createAlphaFadeDecorator(), false);

			answerGroup.addAnswer(decoratedAnswerCard);
		}
	}

	private void createUi() {
		questionContainer = new TextView(this);
		answerGroup = new SelectionLimitedAnswerGroup<>(this);

		final LinearLayout rootViewGroup = new LinearLayout(this);
		rootViewGroup.setOrientation(LinearLayout.VERTICAL);
		rootViewGroup.addView(questionContainer);
		rootViewGroup.addView((View) answerGroup);

		setContentView(rootViewGroup, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
	}

	private ColorFadeDecorator createColorFadeDecorator() {
		final ColorSupplier colorSupplier = new ColorSupplier() {
			@Override
			public int getColor(final boolean marked, final boolean selected,
					final boolean answerIsCorrect) {
				if (marked) {
					if (selected) {
						return answerIsCorrect ? Color.GREEN : Color.RED;
					} else {
						return answerIsCorrect ? Color.RED : Color.GREEN;
					}
				} else {
					return selected ? Color.BLUE : Color.WHITE;
				}
			}
		};

		return new ColorFadeDecorator(colorSupplier);
	}

	private AlphaDecorator createAlphaFadeDecorator() {
		final AlphaSupplier alphaSupplier = new AlphaSupplier() {
			@Override
			public float getAlpha(final boolean marked, final boolean selected,
					final boolean answerIsCorrect) {
				if (marked && !selected && !answerIsCorrect) {
					return 0.5f;
				} else {
					return 1f;
				}
			}
		};

		return new AlphaDecorator(alphaSupplier);
	}
}