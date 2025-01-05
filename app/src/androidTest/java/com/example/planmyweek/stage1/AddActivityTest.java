package com.example.planmyweek.stage1;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.planmyweek.R;
import com.example.planmyweek.Views.AddActivityActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddActivityTest {

    @Rule
    public ActivityTestRule<AddActivityActivity> activityRule = new ActivityTestRule<>(AddActivityActivity.class);

    @Test
    public void testAddActivityWithValidData() {
        // Fill in the title
        Espresso.onView(ViewMatchers.withId(R.id.activity_edit_text))
                .perform(ViewActions.typeText("Test Activity"), ViewActions.closeSoftKeyboard());

        // Select Priority
        Espresso.onView(ViewMatchers.withId(R.id.priority_spinner)).perform(ViewActions.click());
        Espresso.onData(Matchers.allOf(Matchers.is(Matchers.instanceOf(String.class)), Matchers.is("High")))
                .perform(ViewActions.click());

        // Select Category
        Espresso.onView(ViewMatchers.withId(R.id.category_spinner)).perform(ViewActions.click());
        Espresso.onData(Matchers.allOf(Matchers.is(Matchers.instanceOf(String.class)), Matchers.is("Work")))
                .perform(ViewActions.click());

        // Select Due Date
        Espresso.onView(ViewMatchers.withId(R.id.button_select_due_date)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());

        // Select Due Time
        Espresso.onView(ViewMatchers.withId(R.id.button_select_due_time)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());

        // Add description
        Espresso.onView(ViewMatchers.withId(R.id.notes_edit_text))
                .perform(ViewActions.typeText("Description of the test activity"), ViewActions.closeSoftKeyboard());

        // Submit the activity
        Espresso.onView(ViewMatchers.withId(R.id.button_add_activity))
                .perform(ViewActions.scrollTo(), ViewActions.click());

        // Verify success: Navigation to the main activity occurs
        Espresso.onView(ViewMatchers.withId(R.id.main_activity_layout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testAddActivityWithMissingFields() {
        // Leave title blank and fill other fields

        // Select Priority
        Espresso.onView(ViewMatchers.withId(R.id.priority_spinner)).perform(ViewActions.click());
        Espresso.onData(Matchers.allOf(Matchers.is(Matchers.instanceOf(String.class)), Matchers.is("High")))
                .perform(ViewActions.click());

        // Select Category
        Espresso.onView(ViewMatchers.withId(R.id.category_spinner)).perform(ViewActions.click());
        Espresso.onData(Matchers.allOf(Matchers.is(Matchers.instanceOf(String.class)), Matchers.is("Work")))
                .perform(ViewActions.click());

        // Select Due Date
        Espresso.onView(ViewMatchers.withId(R.id.button_select_due_date)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());

        // Select Due Time
        Espresso.onView(ViewMatchers.withId(R.id.button_select_due_time)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());

        // Add description
        Espresso.onView(ViewMatchers.withId(R.id.notes_edit_text))
                .perform(ViewActions.typeText("Description without a title"), ViewActions.closeSoftKeyboard());

        // Submit the form
        Espresso.onView(ViewMatchers.withId(R.id.button_add_activity))
                .perform(ViewActions.scrollTo(), ViewActions.click());

        // Verify that it does NOT navigate to the main screen
        Espresso.onView(ViewMatchers.withId(R.id.main_activity_layout))
                .check(ViewAssertions.doesNotExist());

        // Optionally verify that we remain on the AddActivityActivity screen
        Espresso.onView(ViewMatchers.withId(R.id.add_activity_layout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
