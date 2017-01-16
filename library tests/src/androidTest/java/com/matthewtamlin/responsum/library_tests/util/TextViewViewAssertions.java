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

package com.matthewtamlin.responsum.library_tests.util;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Custom Espresso view assertions which can be applied to TextViews.
 */
public class TextViewViewAssertions {
	/**
	 * Creates a ViewAssertion which checks that the test in a TextView matches some condition.
	 *
	 * @param text
	 * 		the condition to match, not null
	 * @return the view assertion
	 */
	public static ViewAssertion hasText(final Matcher<String> text) {
		return new ViewAssertion() {
			@Override
			public void check(final View view, final NoMatchingViewException noViewFoundException) {
				if (view == null || !(view instanceof TextView)) {
					throw new AssertionError("view must be a non-null instance of TextView.");
				} else {
					final TextView singleAnswerCard = (TextView) view;

					assertThat("view has incorrect text.",
							charSequenceToString(singleAnswerCard.getText()), is(text));
				}
			}
		};
	}

	/**
	 * Null safe conversion from CharSequence to String. If null is passed then null is returned.
	 *
	 * @param charSequence
	 * 		the value to convert
	 * @return the supplied value as a String
	 */
	private static String charSequenceToString(final CharSequence charSequence) {
		return charSequence == null ? null : charSequence.toString();
	}
}