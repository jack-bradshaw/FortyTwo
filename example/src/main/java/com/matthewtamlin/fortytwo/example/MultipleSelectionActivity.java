package com.matthewtamlin.fortytwo.example;

import android.view.ViewGroup;

import com.matthewtamlin.fortytwo.library.answer.Answer;
import com.matthewtamlin.fortytwo.library.answer.ImmutableAnswer;
import com.matthewtamlin.fortytwo.library.answer_view.AlphaDecorator;
import com.matthewtamlin.fortytwo.library.answer_view.AlphaDecorator.AlphaSupplier;
import com.matthewtamlin.fortytwo.library.answer_view.ColorFadeDecorator;
import com.matthewtamlin.fortytwo.library.answer_view.ColorFadeDecorator.ColorSupplier;
import com.matthewtamlin.fortytwo.library.answer_view.DecoratedAnswerCard;

import java.util.LinkedHashMap;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * An activity which tests the user's knowledge of our solar system.
 */
public class MultipleSelectionActivity extends AbstractQuestionActivity{
	/**
	 * The question to display.
	 */
	private static final String QUESTION = "Which of the following are moons of Jupiter? " +
			"Select up to four answers.";

	/**
	 * The answers and the associated identifiers to display.
	 */
	private static final LinkedHashMap<CharSequence, Answer> answerMap = new LinkedHashMap<>();

	static {
		answerMap.put("1.", new ImmutableAnswer("Pheobe", false));
		answerMap.put("2.", new ImmutableAnswer("Ganymede", true));
		answerMap.put("3.", new ImmutableAnswer("Triton", false));
		answerMap.put("4.", new ImmutableAnswer("Lunar", false));
		answerMap.put("5.", new ImmutableAnswer("Kore", true));
		answerMap.put("6.", new ImmutableAnswer("Callisto", true));
		answerMap.put("7.", new ImmutableAnswer("Titan", false));
	}

	@Override
	protected void onStart() {
		super.onStart();
		displayQuestionAndAnswers();
	}

	/**
	 * Adds all answer and identifiers to the view.
	 */
	@SuppressWarnings("unchecked")
	private void displayQuestionAndAnswers() {
		getQuestionContainer().setText(QUESTION);

		for (final CharSequence identifier : answerMap.keySet()) {
			final DecoratedAnswerCard decoratedAnswerCard = new DecoratedAnswerCard(this);

			decoratedAnswerCard.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
			decoratedAnswerCard.setIdentifier(identifier, false);
			decoratedAnswerCard.setAnswer(answerMap.get(identifier), false);
			decoratedAnswerCard.addDecorator(createColorFadeDecorator(), false);
			decoratedAnswerCard.addDecorator(createAlphaDecorator(), false);

			getAnswerGroup().addAnswer(decoratedAnswerCard);
		}
	}

	/**
	 * @return a new ColorFadeDecorator
	 */
	private ColorFadeDecorator createColorFadeDecorator() {
		final ColorSupplier colorSupplier = new ColorSupplier() {
			@Override
			public int getColor(final boolean marked, final boolean selected,
					final boolean answerIsCorrect) {
				if (marked) {
					if (selected) {
						return answerIsCorrect ? 0xFF1565C0 : 0xFFC62828; // Blue, red
					} else {
						return answerIsCorrect ? 0xFFEF6C00 : 0xFFFFFFFF; // Orange, white
					}
				} else {
					return selected ? 0xFFE91E63 : 0xFFFFFFFF; // Pink, white
				}
			}
		};

		return new ColorFadeDecorator(colorSupplier);
	}

	/**
	 * @return a new AlphaDecorator
	 */
	private AlphaDecorator createAlphaDecorator() {
		final AlphaSupplier alphaSupplier = new AlphaSupplier() {
			@Override
			public float getAlpha(final boolean marked, final boolean selected,
					final boolean answerIsCorrect) {
				if (marked && !selected && !answerIsCorrect) {
					return 0.3f;
				} else {
					return 1f;
				}
			}
		};

		return new AlphaDecorator(alphaSupplier);
	}
}