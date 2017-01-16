/*
 * Copyright 2017 Matthew Tamlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matthewtamlin.multiplechoiceanswerview.library_tests;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.android_testing_tools.library.ControlsBelowViewTestHarness;
import com.matthewtamlin.multiple_choice_answer_view.library.answer.PojoAnswer;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.SimpleAnswerCard;

/**
 * A test harness for the {@link SimpleAnswerCard} class. This class is abstract because the test
 * view is abstract.
 */
@SuppressLint("SetTextI18n") // Not important during testing
public abstract class SimpleAnswerCardTestHarness
		extends ControlsBelowViewTestHarness<SimpleAnswerCard> {
	private static final int ANIMATION_DURATION_INCREMENT_MS = 50;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getControlsContainer().addView(createIncreaseAnimationDurationButton());
		getControlsContainer().addView(createDecreaseAnimationDurationButton());
		getControlsContainer().addView(createMarkButton());
		getControlsContainer().addView(createUnmarkButton());
		getControlsContainer().addView(createSelectButton());
		getControlsContainer().addView(createDeselectButton());
		getControlsContainer().addView(createMarkAndSelectButton());
		getControlsContainer().addView(createUnmarkAndDeselectButton());
		getControlsContainer().addView(createSetAnswerCorrectButton());
		getControlsContainer().addView(createSetAnswerIncorrectButton());
		getControlsContainer().addView(createSetIdentifierButton());
	}

	/**
	 * Creates a button which increasing the test view animation duration when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createIncreaseAnimationDurationButton() {
		final Button b = new Button(this);
		b.setText("Increase animation duration");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final int newDuration = getTestView().getAnimationDurationMs() +
						ANIMATION_DURATION_INCREMENT_MS;
				getTestView().setAnimationDurationMs(newDuration);
			}
		});

		return b;
	}

	/**
	 * Creates a button which decreases the test view animation duration when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createDecreaseAnimationDurationButton() {
		final Button b = new Button(this);
		b.setText("Decrease animation duration");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final int newDuration = getTestView().getAnimationDurationMs() -
						ANIMATION_DURATION_INCREMENT_MS;
				getTestView().setAnimationDurationMs(newDuration < 0 ? 0 : newDuration);
			}
		});

		return b;
	}

	/**
	 * Creates a button which marks the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createMarkButton() {
		final Button b = new Button(this);
		b.setText("Mark");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setMarkedStatus(true, true);
			}
		});

		return b;
	}

	/**
	 * Creates a button which unmarks the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createUnmarkButton() {
		final Button b = new Button(this);
		b.setText("Unmark");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setMarkedStatus(false, true);
			}
		});

		return b;
	}

	/**
	 * Creates a button which selects the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createSelectButton() {
		final Button b = new Button(this);
		b.setText("Select");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setSelectedStatus(true, true);
			}
		});

		return b;
	}

	/**
	 * Creates a button which deselects the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createDeselectButton() {
		final Button b = new Button(this);
		b.setText("Deselect");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setSelectedStatus(false, true);
			}
		});

		return b;
	}

	/**
	 * Creates a button which both marks and selects the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createMarkAndSelectButton() {
		final Button b = new Button(this);
		b.setText("Mark and select");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setStatus(true, true, true);
			}
		});

		return b;
	}

	/**
	 * Creates a button which both unmarks and deselects the test view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createUnmarkAndDeselectButton() {
		final Button b = new Button(this);
		b.setText("Unmark and deselect");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setStatus(false, false, true);
			}
		});

		return b;
	}

	/**
	 * Creates a button which sets a correct answer in the text view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createSetAnswerCorrectButton() {
		final Button b = new Button(this);
		b.setText("Set answer (correct) button");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setAnswer(new PojoAnswer("answer (correct)", true), true);
			}
		});

		return b;
	}

	/**
	 * Creates a button which sets an incorrect answer in the text view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createSetAnswerIncorrectButton() {
		final Button b = new Button(this);
		b.setText("Set answer (incorrect) button");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setAnswer(new PojoAnswer("answer (incorrect)", false), true);
			}
		});

		return b;
	}

	/**
	 * Creates a button which sets the identifier in the text view when clicked.
	 *
	 * @return the button, not null
	 */
	private Button createSetIdentifierButton() {
		final Button b = new Button(this);
		b.setText("Set identifier");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().setIdentifier("A", true);
			}
		});

		return b;
	}
}