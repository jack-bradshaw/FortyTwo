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

package com.matthewtamlin.fortytwo.library_tests.answer_view;

import android.support.test.rule.ActivityTestRule;

import com.matthewtamlin.fortytwo.library.answer_view.DecoratedAnswerCard;
import com.matthewtamlin.fortytwo.library.answer_view.DecoratedAnswerCard.Decorator;
import com.matthewtamlin.fortytwo.library.answer.Answer;
import com.matthewtamlin.fortytwo.library.answer.PojoAnswer;
import com.matthewtamlin.fortytwo.library_tests.DecoratedAnswerCardTestHarness;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.matthewtamlin.fortytwo.library_tests.answer_view.DecoratedAnswerCardViewActions.addDecorator;
import static com.matthewtamlin.fortytwo.library_tests.answer_view.SimpleAnswerCardViewActions.setAnimationDurationMs;
import static com.matthewtamlin.fortytwo.library_tests.answer_view.SimpleAnswerCardViewActions.setAnswer;
import static com.matthewtamlin.fortytwo.library_tests.answer_view.SimpleAnswerCardViewActions.setStatus;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Instrumentation test for the DecoratedAnswerCard view.
 */
public class TestDecoratedAnswerCard extends TestSimpleAnswerCard {
	/**
	 * Rule to always launch the DecoratedAnswerCardTestHarness before running the tests. This rule
	 * allows an actual instance of the view to be tested.
	 */
	@Rule
	public final ActivityTestRule<DecoratedAnswerCardTestHarness> rule = new
			ActivityTestRule<>(DecoratedAnswerCardTestHarness.class);

	/**
	 * The view under test.
	 */
	private DecoratedAnswerCard testView;

	@Override
	public DecoratedAnswerCard getSimpleAnswerCardView() {
		return testView;
	}

	/**
	 * Performs initialisation before each test case is run. A direct reference to the test view is
	 * obtained from the activity rule.
	 */
	@Before
	public void setup() {
		testView = rule.getActivity().getTestView();

		super.setup();
	}

	/**
	 * Test to ensure that the {@link DecoratedAnswerCard#addDecorator(Decorator, boolean)} method
	 * functions correctly when animations are enabled. The test will only pass if the {@link
	 * Decorator#decorate(DecoratedAnswerCard, boolean)} method is called for each decorator. A null
	 * decorator is used to ensure null is safely handled.
	 */
	@Test
	public void testAddDecorator_usingAnimation() {
		final Decorator decorator1 = mock(Decorator.class);
		final Decorator decorator2 = mock(Decorator.class);

		getSimpleAnswerCardViewInteraction().perform(addDecorator(decorator1, true));
		getSimpleAnswerCardViewInteraction().perform(addDecorator(decorator2, true));
		getSimpleAnswerCardViewInteraction().perform(addDecorator(null, true));

		verify(decorator1, times(1)).decorate(testView, true);
		verify(decorator2, times(1)).decorate(testView, true);
	}

	/**
	 * Test to ensure that the {@link DecoratedAnswerCard#addDecorator(Decorator, boolean)} method
	 * functions correctly when animations are disabled. The test will only pass if the {@link
	 * Decorator#decorate(DecoratedAnswerCard, boolean)} method is called for each decorator. A null
	 * decorator is used to ensure null is safely handled.
	 */
	@Test
	public void testAddDecorator_noAnimation() {
		final Decorator decorator1 = mock(Decorator.class);
		final Decorator decorator2 = mock(Decorator.class);

		getSimpleAnswerCardViewInteraction().perform(addDecorator(decorator1, false));
		getSimpleAnswerCardViewInteraction().perform(addDecorator(decorator2, false));
		getSimpleAnswerCardViewInteraction().perform(addDecorator(null, true));

		verify(decorator1, times(1)).decorate(testView, false);
		verify(decorator2, times(1)).decorate(testView, false);
	}

	/**
	 * Test to ensure that the {@link DecoratedAnswerCard#setAnimationDurationMs(int)} method
	 * functions correctly. The test will only pass if animation duration is passes to all
	 * decorators. A null decorator is used to ensure null is safely handled.
	 */
	@Test
	public void testSetAnimationDuration() {
		final Decorator decorator1 = mock(Decorator.class);
		final Decorator decorator2 = mock(Decorator.class);

		getSimpleAnswerCardViewInteraction().perform(addDecorator(decorator1, false));
		getSimpleAnswerCardViewInteraction().perform(addDecorator(decorator2, false));
		getSimpleAnswerCardViewInteraction().perform(addDecorator(null, false));

		getSimpleAnswerCardViewInteraction().perform(setAnimationDurationMs(10));

		verify(decorator1, times(1)).setAnimationDurationMs(10);
		verify(decorator2, times(1)).setAnimationDurationMs(10);
	}

	/**
	 * Test to ensure that the {@link DecoratedAnswerCard#setStatus(boolean, boolean, boolean)}
	 * method functions correctly. The test will only pass if the {@link
	 * Decorator#decorate(DecoratedAnswerCard, boolean)} method is called on all decorators each
	 * time the status is set. Animations are enabled and disabled in different calls to increase
	 * coverage.
	 */
	@Test
	public void testSetStatus_multipleCalls_checkDecoratorsAreCalled() {
		final Decorator decorator1 = mock(Decorator.class);
		final Decorator decorator2 = mock(Decorator.class);

		getSimpleAnswerCardViewInteraction().perform(addDecorator(decorator1, false));
		getSimpleAnswerCardViewInteraction().perform(addDecorator(decorator2, false));
		getSimpleAnswerCardViewInteraction().perform(addDecorator(null, false));

		getSimpleAnswerCardViewInteraction().perform(setStatus(true, true, true));

		verify(decorator1, times(1)).decorate(testView, true);
		verify(decorator2, times(1)).decorate(testView, true);

		getSimpleAnswerCardViewInteraction().perform(setStatus(true, true, false));

		// Expect two calls because one call was performed when decorators were added
		verify(decorator1, times(2)).decorate(testView, false);
		verify(decorator2, times(2)).decorate(testView, false);
	}

	/**
	 * Test to ensure that the {@link DecoratedAnswerCard#setAnswer(Answer, boolean)} method
	 * functions correctly. The test will only pass if the {@link Decorator#decorate(DecoratedAnswerCard,
	 * boolean)} method is called on all decorators each time the answer is set. Animations are
	 * enabled and disabled in different calls to increase coverage.
	 */
	@Test
	public void testSetAnswer_checkDecoratorsAreCalled() {
		final Decorator decorator1 = mock(Decorator.class);
		final Decorator decorator2 = mock(Decorator.class);

		getSimpleAnswerCardViewInteraction().perform(addDecorator(decorator1, false));
		getSimpleAnswerCardViewInteraction().perform(addDecorator(decorator2, false));
		getSimpleAnswerCardViewInteraction().perform(addDecorator(null, false));

		getSimpleAnswerCardViewInteraction().perform(setAnswer(new PojoAnswer("test", false),
				true));

		verify(decorator1, times(1)).decorate(testView, true);
		verify(decorator2, times(1)).decorate(testView, true);

		getSimpleAnswerCardViewInteraction().perform(setAnswer(null, false));

		// Expect two calls because one call was performed when decorators were added
		verify(decorator1, times(2)).decorate(testView, false);
		verify(decorator2, times(2)).decorate(testView, false);
	}
}