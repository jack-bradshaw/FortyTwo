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

package com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.matthewtamlin.android_testing_tools.library.EspressoHelper;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.AnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.AnswerGroup.Listener;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.SelectionLimitedAnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.DecoratedAnswerCard;
import com.matthewtamlin.multiplechoiceanswerview.library_tests.SelectionLimitAnswerGroupTestHarness;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.SelectionLimitAnswerGroupViewActions.addAnswer;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.SelectionLimitAnswerGroupViewActions.addAnswers;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.SelectionLimitAnswerGroupViewActions.allowSelectionChangesWhenMarked;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.SelectionLimitAnswerGroupViewActions.clearAnswers;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.SelectionLimitAnswerGroupViewActions.clickViewAtIndex;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.SelectionLimitAnswerGroupViewActions.registerListener;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.SelectionLimitAnswerGroupViewActions.removeAnswer;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.SelectionLimitAnswerGroupViewActions.setMultipleSelectionLimit;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.SelectionLimitAnswerGroupViewAssertions.containsNoAnswers;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.SelectionLimitAnswerGroupViewAssertions.containsView;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the {@link SelectionLimitedAnswerGroup} class.
 */
@RunWith(AndroidJUnit4.class)
public class TestSelectionLimitAnswerGroup {
	/**
	 * Rule to always launch the SelectionLimitAnswerGroupTestHarness before running the tests. This
	 * rule allows an actual instance of the view to be tested.
	 */
	@Rule
	public ActivityTestRule<SelectionLimitAnswerGroupTestHarness> rule = new
			ActivityTestRule<>(SelectionLimitAnswerGroupTestHarness.class);

	/**
	 * A direct reference to the view under test.
	 */
	private SelectionLimitedAnswerGroup<DecoratedAnswerCard> testViewDirect;

	/**
	 * The view under test, as an Espresso ViewInteraction.
	 */
	public ViewInteraction testViewEspresso;

	/**
	 * A mock listener.
	 */
	private Listener<DecoratedAnswerCard> listener1;

	/**
	 * Another mock listener.
	 */
	private Listener<DecoratedAnswerCard> listener2;

	/**
	 * Performs initialisation before the tests run. The direct and espresso view references are
	 * obtained, and the listeners are registered for callbacks.
	 */
	@SuppressWarnings("unchecked") // Not relevant to mocks
	@Before
	public void setup() {
		testViewDirect = rule.getActivity().getTestView();
		testViewEspresso = EspressoHelper.viewToViewInteraction(testViewDirect);

		listener1 = mock(Listener.class);
		listener2 = mock(Listener.class);

		testViewEspresso.perform(registerListener(listener1));
		testViewEspresso.perform(registerListener(listener2));
		testViewEspresso.perform(registerListener(null)); // Check null safety
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#addAnswers(Collection)} method
	 * functions correctly when provided with a null collection. The test will only pass if the
	 * expected exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddAnswers_nullSupplied() {
		testViewEspresso.perform(addAnswers(null));
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#addAnswers(Collection)} method
	 * functions correctly when provided with a collection containing null. The test will only pass
	 * if the expected exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddAnswers_collectionContainsNull() {
		final List<AnswerView> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(null);

		testViewEspresso.perform(addAnswers(answers));
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#addAnswers(Collection)} method
	 * functions correctly when provided with an empty collection. The test will only pass if the
	 * group contains no answers.
	 */
	@Test
	public void testAddAnswers_emptyCollection() {
		final List<AnswerView> answers = new ArrayList<>();

		testViewEspresso.perform(addAnswers(answers));

		testViewEspresso.check(containsNoAnswers());

		assertThat("Expected list to reflect there being no answer cards.",
				testViewDirect.getAnswers().isEmpty(), is(true));
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#addAnswers(Collection)} method
	 * functions correctly when provided with a non-empty collection which contains no null
	 * elements. The test will only pass if the group contains all expected answers.
	 */
	@Test
	public void testAddAnswers_validArgument() {
		final List<DecoratedAnswerCard> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());

		testViewEspresso.perform(addAnswers(answers));

		for (final DecoratedAnswerCard card : answers) {
			testViewEspresso.check(containsView(card, true));
		}

		assertThat("Expected list to contain only the supplied answers.",
				testViewDirect.getAnswers(), is(answers));
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#addAnswer(AnswerView)} method functions
	 * correctly when provided with a null answer view. The test will only pass if the expected
	 * exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddAnswer_nullSupplied() {
		testViewEspresso.perform(addAnswer(null));
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#addAnswer(AnswerView)} method functions
	 * correctly when provided with a non-null answer view. The test will only pass if the answer is
	 * added to the group.
	 */
	@Test
	public void testAddAnswer_validArgument() {
		final DecoratedAnswerCard view = getNewAnswerCard();

		testViewEspresso.perform(addAnswer(view));

		testViewEspresso.check(containsView(view, true));

		assertThat("Expected list to contain only only one item.",
				testViewDirect.getAnswers().size(), is(1));
		assertThat("Expected list to contain only the supplied answer.",
				testViewDirect.getAnswers().get(0), is(view));
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#removeAnswer(AnswerView)} method
	 * functions correctly when provided with a null answer view. The test will only pass if the
	 * expected exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveAnswer_nullSupplied() {
		testViewEspresso.perform(removeAnswer(null));
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#removeAnswer(AnswerView)} method
	 * functions correctly when provided with a non-null answer view which is not contained in the
	 * group. The test wil only pass if an exception is not thrown.
	 */
	@Test
	public void testRemoveAnswer_viewNotInGroup() {
		final AnswerView view = getNewAnswerCard();

		testViewEspresso.perform(removeAnswer(view));
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#removeAnswer(AnswerView)} method
	 * functions correctly when provided with a non-null answer view which is contained in the
	 * group. The test will only pass if the answer is removed from the group.
	 */
	@Test
	public void testRemoveAnswer_viewContainedInGroup() {
		final AnswerView view = getNewAnswerCard();

		testViewEspresso.perform(addAnswer(view));

		testViewEspresso.check(containsView(view, true));

		testViewEspresso.perform(removeAnswer(view));

		testViewEspresso.check(containsView(view, false));

		assertThat("Expected list to contain no items.", testViewDirect.getAnswers().isEmpty(),
				is(true));
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#clearAnswers()} method functions
	 * correctly. The test will only pass if all answers rae removed from the group.
	 */
	@Test
	public void testClearAnswers() {
		final List<DecoratedAnswerCard> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());

		testViewEspresso.perform(addAnswers(answers));

		for (final DecoratedAnswerCard card : answers) {
			testViewEspresso.check(containsView(card, true));
		}

		testViewEspresso.perform(clearAnswers());

		for (final DecoratedAnswerCard view : answers) {
			testViewEspresso.check(containsView(view, false));
		}

		assertThat("Expected list to contain no items.", testViewDirect.getAnswers().isEmpty(),
				is(true));
	}

	/**
	 * Test to ensure the group functions correctly when an answer is clicked while selection
	 * changes are allowed and the clicked view is marked. The test will only pass if the selection
	 * status of the clicked view changes.
	 */
	@Test
	public void testClickAnswer_selectionChangesAllowedAndMarked() {
		final List<DecoratedAnswerCard> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());

		answers.get(0).setStatus(true, false, false);
		answers.get(1).setStatus(false, false, false);

		testViewEspresso.perform(addAnswers(answers));
		testViewEspresso.perform(allowSelectionChangesWhenMarked(true));
		testViewEspresso.perform(clickViewAtIndex(0));

		assertThat("Answer 0 should be selected.", answers.get(0).isSelected(), is(true));
		assertThat("Answer 1 should not be selected.", answers.get(1).isSelected(), is(false));

		verifySelectedCallbackInvocations(answers.get(0), 1);
		verifySelectedCallbackInvocations(answers.get(1), 0);
		verifyDeselectedCallbackInvocations(answers.get(0), 0);
		verifyDeselectedCallbackInvocations(answers.get(1), 0);
	}

	/**
	 * Test to ensure the group functions correctly when an answer is clicked while selection
	 * changes are allowed and the clicked view is unmarked. The test will only pass if the
	 * selection status of the clicked view changes.
	 */
	@Test
	public void testClickAnswer_selectionChangesAllowedAndUnmarked() {
		final List<DecoratedAnswerCard> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());

		answers.get(0).setStatus(false, false, false);
		answers.get(1).setStatus(false, false, false);

		testViewEspresso.perform(addAnswers(answers));
		testViewEspresso.perform(allowSelectionChangesWhenMarked(true));
		testViewEspresso.perform(clickViewAtIndex(0));

		assertThat("Answer 0 should be selected.", answers.get(0).isSelected(), is(true));
		assertThat("Answer 1 should not be selected.", answers.get(1).isSelected(), is(false));

		verifySelectedCallbackInvocations(answers.get(0), 1);
		verifySelectedCallbackInvocations(answers.get(1), 0);
		verifyDeselectedCallbackInvocations(answers.get(0), 0);
		verifyDeselectedCallbackInvocations(answers.get(1), 0);
	}

	/**
	 * Test to ensure the group functions correctly when an answer is clicked while selection
	 * changes are disallowed and the view is marked. The test will only pass if the selection
	 * status of the clicked view does not change.
	 */
	@Test
	public void testClickAnswer_selectionChangesDisallowedAndMarked() {
		final List<DecoratedAnswerCard> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());

		answers.get(0).setStatus(true, false, false);
		answers.get(1).setStatus(false, false, false);

		testViewEspresso.perform(addAnswers(answers));
		testViewEspresso.perform(allowSelectionChangesWhenMarked(false));
		testViewEspresso.perform(clickViewAtIndex(0));

		assertThat("Answer 0 should not be selected.", answers.get(0).isSelected(), is(false));
		assertThat("Answer 1 should not be selected.", answers.get(1).isSelected(), is(false));

		verifySelectedCallbackInvocations(answers.get(0), 0);
		verifySelectedCallbackInvocations(answers.get(1), 0);
		verifyDeselectedCallbackInvocations(answers.get(0), 0);
		verifyDeselectedCallbackInvocations(answers.get(1), 0);
	}

	/**
	 * Test to ensure the group functions correctly when an answer is clicked while selection
	 * changes are disallowed and the view is unmarked. The test will only pass if the selection
	 * status of the clicked view changes.
	 */
	@Test
	public void testClickAnswer_selectionChangesDisallowedAnUnmarked() {
		final List<DecoratedAnswerCard> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());

		answers.get(0).setStatus(false, false, false);
		answers.get(1).setStatus(false, false, false);

		testViewEspresso.perform(addAnswers(answers));
		testViewEspresso.perform(allowSelectionChangesWhenMarked(false));
		testViewEspresso.perform(clickViewAtIndex(0));

		assertThat("Answer 0 should be selected.", answers.get(0).isSelected(), is(true));
		assertThat("Answer 1 should not be selected.", answers.get(1).isSelected(), is(false));

		verifySelectedCallbackInvocations(answers.get(0), 1);
		verifySelectedCallbackInvocations(answers.get(1), 0);
		verifyDeselectedCallbackInvocations(answers.get(0), 0);
		verifyDeselectedCallbackInvocations(answers.get(1), 0);
	}

	/**
	 * Test to ensure the group functions correctly when an answer is clicked and the selection
	 * capacity has been exceeded. The test will only pass if the views are selected and deselected
	 * to maintain the limit.
	 */
	@Test
	public void testSelectAnswer_selectionCapacityExceeded() {
		final List<DecoratedAnswerCard> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());

		testViewEspresso.perform(addAnswers(answers));
		testViewEspresso.perform(allowSelectionChangesWhenMarked(true));
		testViewEspresso.perform(setMultipleSelectionLimit(1));

		testViewEspresso.perform(clickViewAtIndex(0));

		assertThat("Answer 0 should be selected.", answers.get(0).isSelected(), is(true));
		assertThat("Answer 1 should not be selected.", answers.get(1).isSelected(), is(false));
		assertThat("Answer 2 should not be selected.", answers.get(2).isSelected(), is(false));

		verifySelectedCallbackInvocations(answers.get(0), 1);
		verifySelectedCallbackInvocations(answers.get(1), 0);
		verifySelectedCallbackInvocations(answers.get(2), 0);
		verifyDeselectedCallbackInvocations(answers.get(0), 0);
		verifyDeselectedCallbackInvocations(answers.get(1), 0);
		verifyDeselectedCallbackInvocations(answers.get(2), 0);

		testViewEspresso.perform(clickViewAtIndex(1));

		assertThat("Answer 0 should not be selected.", answers.get(0).isSelected(), is(false));
		assertThat("Answer 1 should be selected.", answers.get(1).isSelected(), is(true));
		assertThat("Answer 2 should not be selected.", answers.get(2).isSelected(), is(false));

		verifySelectedCallbackInvocations(answers.get(0), 1);
		verifySelectedCallbackInvocations(answers.get(1), 1);
		verifySelectedCallbackInvocations(answers.get(2), 0);
		verifyDeselectedCallbackInvocations(answers.get(0), 1);
		verifyDeselectedCallbackInvocations(answers.get(1), 0);
		verifyDeselectedCallbackInvocations(answers.get(2), 0);

		testViewEspresso.perform(clickViewAtIndex(2));

		assertThat("Answer 0 should not be selected.", answers.get(0).isSelected(), is(false));
		assertThat("Answer 1 should not be selected.", answers.get(1).isSelected(), is(false));
		assertThat("Answer 2 should be selected.", answers.get(2).isSelected(), is(true));

		verifySelectedCallbackInvocations(answers.get(0), 1);
		verifySelectedCallbackInvocations(answers.get(1), 1);
		verifySelectedCallbackInvocations(answers.get(2), 1);
		verifyDeselectedCallbackInvocations(answers.get(0), 1);
		verifyDeselectedCallbackInvocations(answers.get(1), 1);
		verifyDeselectedCallbackInvocations(answers.get(2), 0);

		testViewEspresso.perform(clickViewAtIndex(0));

		assertThat("Answer 0 should be selected.", answers.get(0).isSelected(), is(true));
		assertThat("Answer 1 should not be selected.", answers.get(1).isSelected(), is(false));
		assertThat("Answer 2 should not be selected.", answers.get(2).isSelected(), is(false));

		verifySelectedCallbackInvocations(answers.get(0), 2);
		verifySelectedCallbackInvocations(answers.get(1), 1);
		verifySelectedCallbackInvocations(answers.get(2), 1);
		verifyDeselectedCallbackInvocations(answers.get(0), 1);
		verifyDeselectedCallbackInvocations(answers.get(1), 1);
		verifyDeselectedCallbackInvocations(answers.get(2), 1);
	}

	/**
	 * Test to ensure the group functions correctly when an answer is clicked and the selection
	 * capacity has not been reached but not exceeded. The test will only pass if all views are
	 * selected and none are deselected.
	 */
	@Test
	public void testSelectAnswer_selectionLimitReachedButNotExceeded() {
		final List<DecoratedAnswerCard> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());

		testViewEspresso.perform(addAnswers(answers));
		testViewEspresso.perform(allowSelectionChangesWhenMarked(true));
		testViewEspresso.perform(setMultipleSelectionLimit(3));

		testViewEspresso.perform(clickViewAtIndex(0));

		assertThat("Answer 0 should be selected.", answers.get(0).isSelected(), is(true));
		assertThat("Answer 1 should not be selected.", answers.get(1).isSelected(), is(false));
		assertThat("Answer 2 should not be selected.", answers.get(2).isSelected(), is(false));

		verifySelectedCallbackInvocations(answers.get(0), 1);
		verifySelectedCallbackInvocations(answers.get(1), 0);
		verifySelectedCallbackInvocations(answers.get(2), 0);
		verifyDeselectedCallbackInvocations(answers.get(0), 0);
		verifyDeselectedCallbackInvocations(answers.get(1), 0);
		verifyDeselectedCallbackInvocations(answers.get(2), 0);

		testViewEspresso.perform(clickViewAtIndex(1));

		assertThat("Answer 0 should be selected.", answers.get(0).isSelected(), is(true));
		assertThat("Answer 1 should be selected.", answers.get(1).isSelected(), is(true));
		assertThat("Answer 2 should not be selected.", answers.get(2).isSelected(), is(false));

		verifySelectedCallbackInvocations(answers.get(0), 1);
		verifySelectedCallbackInvocations(answers.get(1), 1);
		verifySelectedCallbackInvocations(answers.get(2), 0);
		verifyDeselectedCallbackInvocations(answers.get(0), 0);
		verifyDeselectedCallbackInvocations(answers.get(1), 0);
		verifyDeselectedCallbackInvocations(answers.get(2), 0);

		testViewEspresso.perform(clickViewAtIndex(2));

		assertThat("Answer 0 should be selected.", answers.get(0).isSelected(), is(true));
		assertThat("Answer 1 should be selected.", answers.get(1).isSelected(), is(true));
		assertThat("Answer 2 should be selected.", answers.get(2).isSelected(), is(true));

		verifySelectedCallbackInvocations(answers.get(0), 1);
		verifySelectedCallbackInvocations(answers.get(1), 1);
		verifySelectedCallbackInvocations(answers.get(2), 1);
		verifyDeselectedCallbackInvocations(answers.get(0), 0);
		verifyDeselectedCallbackInvocations(answers.get(1), 0);
		verifyDeselectedCallbackInvocations(answers.get(2), 0);
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#setMultipleSelectionLimit(int)} method
	 * functions correctly when provided with a negative limit. The test will only pass if the
	 * correct exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetSelectionLimit_limitIsNegative() {
		testViewEspresso.perform(setMultipleSelectionLimit(-1));
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#setMultipleSelectionLimit(int)} method
	 * functions correctly when provided with a limit of zero. The test will only pass if the
	 * correct exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetSelectionLimit_limitIsZero() {
		testViewEspresso.perform(setMultipleSelectionLimit(0));
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#setMultipleSelectionLimit(int)} method
	 * functions correctly when provided with a limit which is less than the number of views
	 * currently selected. The test will only pass if the least recently clicked views are
	 * deselected.
	 */
	@Test
	public void testSetSelectionLimit_limitLessThanCurrentSelectionCount() {
		final List<DecoratedAnswerCard> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());

		testViewEspresso.perform(addAnswers(answers));
		testViewEspresso.perform(allowSelectionChangesWhenMarked(true));
		testViewEspresso.perform(setMultipleSelectionLimit(3));

		testViewEspresso.perform(clickViewAtIndex(0));
		testViewEspresso.perform(clickViewAtIndex(1));
		testViewEspresso.perform(clickViewAtIndex(2));

		testViewEspresso.perform(setMultipleSelectionLimit(1));

		assertThat("Answer 0 should not be selected.", answers.get(0).isSelected(), is(false));
		assertThat("Answer 1 should not be selected.", answers.get(1).isSelected(), is(false));
		assertThat("Answer 2 should be selected.", answers.get(2).isSelected(), is(true));

		verifySelectedCallbackInvocations(answers.get(0), 1);
		verifySelectedCallbackInvocations(answers.get(1), 1);
		verifySelectedCallbackInvocations(answers.get(2), 1);
		verifyDeselectedCallbackInvocations(answers.get(0), 1);
		verifyDeselectedCallbackInvocations(answers.get(1), 1);
		verifyDeselectedCallbackInvocations(answers.get(2), 0);
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#setMultipleSelectionLimit(int)} method
	 * functions correctly when provided with a limit which is equal to the number of views
	 * currently selected. The test will only pass if no views are deselected.
	 */
	@Test
	public void testSetSelectionLimit_limitEqualToCurrentSelectionCount() {
		final List<DecoratedAnswerCard> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());

		testViewEspresso.perform(addAnswers(answers));
		testViewEspresso.perform(allowSelectionChangesWhenMarked(true));
		testViewEspresso.perform(setMultipleSelectionLimit(3));

		testViewEspresso.perform(clickViewAtIndex(0));
		testViewEspresso.perform(clickViewAtIndex(1));
		testViewEspresso.perform(clickViewAtIndex(2));

		testViewEspresso.perform(setMultipleSelectionLimit(3));

		assertThat("Answer 0 should be selected.", answers.get(0).isSelected(), is(true));
		assertThat("Answer 1 should be selected.", answers.get(1).isSelected(), is(true));
		assertThat("Answer 2 should be selected.", answers.get(2).isSelected(), is(true));

		verifySelectedCallbackInvocations(answers.get(0), 1);
		verifySelectedCallbackInvocations(answers.get(1), 1);
		verifySelectedCallbackInvocations(answers.get(2), 1);
		verifyDeselectedCallbackInvocations(answers.get(0), 0);
		verifyDeselectedCallbackInvocations(answers.get(1), 0);
		verifyDeselectedCallbackInvocations(answers.get(2), 0);
	}

	/**
	 * Test to ensure the {@link SelectionLimitedAnswerGroup#setMultipleSelectionLimit(int)} method
	 * functions correctly when provided with a limit which is greater than the number of views
	 * currently selected. The test will only pass if no views are deselected.
	 */
	@Test
	public void testSetSelectionLimit_limitGreaterThanCurrentSelectionCount() {
		final List<DecoratedAnswerCard> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());

		testViewEspresso.perform(addAnswers(answers));
		testViewEspresso.perform(allowSelectionChangesWhenMarked(true));
		testViewEspresso.perform(setMultipleSelectionLimit(3));

		testViewEspresso.perform(clickViewAtIndex(0));
		testViewEspresso.perform(clickViewAtIndex(1));
		testViewEspresso.perform(clickViewAtIndex(2));

		testViewEspresso.perform(setMultipleSelectionLimit(4));

		assertThat("Answer 0 should be selected.", answers.get(0).isSelected(), is(true));
		assertThat("Answer 1 should be selected.", answers.get(1).isSelected(), is(true));
		assertThat("Answer 2 should be selected.", answers.get(2).isSelected(), is(true));

		verifySelectedCallbackInvocations(answers.get(0), 1);
		verifySelectedCallbackInvocations(answers.get(1), 1);
		verifySelectedCallbackInvocations(answers.get(2), 1);
		verifyDeselectedCallbackInvocations(answers.get(0), 0);
		verifyDeselectedCallbackInvocations(answers.get(1), 0);
		verifyDeselectedCallbackInvocations(answers.get(2), 0);
	}

	/**
	 * @return a new answer card which is neither selected nor marked
	 */
	private DecoratedAnswerCard getNewAnswerCard() {
		final Context context = InstrumentationRegistry.getTargetContext();
		return new DecoratedAnswerCard(context);
	}

	/**
	 * Verifies that the {@link Listener#onAnswerSelected(AnswerGroup, AnswerView)} method of both
	 * listeners has been invoked with the correct arguments, the correct number of times.
	 *
	 * @param selectedView
	 * 		the expected {@code selectedView} argument of the callbacks
	 * @param times
	 * 		the expected number of callback invocations
	 */
	private void verifySelectedCallbackInvocations(final DecoratedAnswerCard selectedView,
			final int times) {
		verify(listener1, times(times)).onAnswerSelected(testViewDirect, selectedView);
		verify(listener2, times(times)).onAnswerSelected(testViewDirect, selectedView);
	}

	/**
	 * Verifies that the {@link Listener#onAnswerDeselected(AnswerGroup, AnswerView)} method of both
	 * listeners has been invoked with the correct arguments, the correct number of times.
	 *
	 * @param deselectedView
	 * 		the expected {@code deselectedView} argument of the callbacks
	 * @param times
	 * 		the expected number of callback invocations
	 */
	private void verifyDeselectedCallbackInvocations(final DecoratedAnswerCard deselectedView,
			final int times) {
		verify(listener1, times(times)).onAnswerDeselected(testViewDirect, deselectedView);
		verify(listener2, times(times)).onAnswerDeselected(testViewDirect, deselectedView);
	}
}