package com.matthewtamlin.fortytwo.example;

import com.matthewtamlin.fortytwo.library.answer.Answer;
import com.matthewtamlin.fortytwo.library.answer.ImmutableAnswer;

import java.util.LinkedHashMap;

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
		answerMap.put("4..", new ImmutableAnswer("Lunar", false));
		answerMap.put("5.", new ImmutableAnswer("Kore", true));
		answerMap.put("6.", new ImmutableAnswer("Callisto", true));
		answerMap.put("", new ImmutableAnswer("Titan", false));
	}


}