package com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.matthewtamlin.android_testing_tools.library.EspressoHelper;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_group.MultipleChoiceAnswerGroup;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.AnswerView;
import com.matthewtamlin.multiple_choice_answer_view.library.answer_view.DecoratedAnswerCard;
import com.matthewtamlin.multiplechoiceanswerview.library_tests.MultipleChoiceAnswerGroupTestHarness;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.MultipleChoiceAnswerGroupViewActions.addAnswer;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.MultipleChoiceAnswerGroupViewActions.addAnswers;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.MultipleChoiceAnswerGroupViewActions.clearAnswers;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.MultipleChoiceAnswerGroupViewActions.removeAnswer;
import static com.matthewtamlin.multiplechoiceanswerview.library_tests.answer_group.MultipleChoiceAnswerGroupViewAssertions.containsView;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class TestMultipleChoiceAnswerGroup {
	@Rule
	public ActivityTestRule<MultipleChoiceAnswerGroupTestHarness> rule = new
			ActivityTestRule<>(MultipleChoiceAnswerGroupTestHarness.class);

	private MultipleChoiceAnswerGroup<DecoratedAnswerCard> testViewDirect;

	public ViewInteraction testViewEspresso;

	@Before
	public void setup() {
		testViewDirect = rule.getActivity().getTestView();
		testViewEspresso = EspressoHelper.viewToViewInteraction(testViewDirect);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddAnswers_nullSupplied() {
		testViewEspresso.perform(addAnswers(null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddAnswers_collectionContainsNull() {
		final List<AnswerView> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(null);

		testViewEspresso.perform(addAnswers(answers));
	}

	@Test
	public void testAddAnswers_emptyCollection() {
		final List<AnswerView> answers = new ArrayList<>();

		testViewEspresso.perform(addAnswers(answers));

		assertThat("Expected list to reflect there being no answer cards.",
				testViewDirect.getAnswers().isEmpty(), is(true));
	}

	@Test
	public void testAddAnswers_validArgument() {
		final List<DecoratedAnswerCard> answers = new ArrayList<>();
		answers.add(getNewAnswerCard());
		answers.add(getNewAnswerCard());

		testViewEspresso.perform(addAnswers(answers));

		for (final DecoratedAnswerCard card : answers) {
			testViewEspresso.check(containsView(card, true));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddAnswer_nullSupplied() {
		testViewEspresso.perform(addAnswer(null));
	}

	@Test
	public void testAddAnswer_validArgument() {
		final DecoratedAnswerCard view = getNewAnswerCard();

		testViewEspresso.perform(addAnswer(view));

		testViewEspresso.check(containsView(view, true));
	}

	@Test
	public void testRemoveAnswer_nullSupplied() {
		testViewEspresso.perform(removeAnswer(null));
	}

	@Test
	public void testRemoveAnswer_viewNotInGroup() {
		final AnswerView view = getNewAnswerCard();

		testViewEspresso.perform(removeAnswer(view));

		testViewEspresso.check(containsView(view, false));
	}

	@Test
	public void testRemoveAnswer_viewContainedInGroup() {
		final AnswerView view = getNewAnswerCard();

		testViewEspresso.perform(addAnswer(view));

		testViewEspresso.check(containsView(view, true));

		testViewEspresso.perform(removeAnswer(view));

		testViewEspresso.check(containsView(view, false));
	}

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
	}

	public void testAnswerViewClickedBehaviour_selectionChangesAllowedAndMarked() {

	}

	public void testAnswerViewClickedBehaviour_selectionChangesAllowedAndNotUnmrked() {

	}

	public void testAnswerViewClickedBehaviour_selectionChangesDisallowedAndMarked() {

	}

	public void testAnswerViewClickedBehaviour_selectionChangesDisallowedAndUnmarked() {

	}

	private DecoratedAnswerCard getNewAnswerCard() {
		final Context context = InstrumentationRegistry.getTargetContext();
		return new DecoratedAnswerCard(context);
	}
}
