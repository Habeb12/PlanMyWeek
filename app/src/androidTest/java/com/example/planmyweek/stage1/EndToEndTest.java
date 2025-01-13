package com.example.planmyweek.stage1;



import android.view.View;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import com.example.planmyweek.R;
import com.example.planmyweek.Views.MainActivity;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EndToEndTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testEndToEndFlow() {
        // 1. Füge eine neue Aktivität hinzu
        Espresso.onView(ViewMatchers.withId(R.id.fab_add_activity))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.activity_edit_text))
                .perform(ViewActions.typeText("E2E Test Activity"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.priority_spinner)).perform(ViewActions.click());
        Espresso.onData(org.hamcrest.Matchers.allOf(org.hamcrest.Matchers.is(org.hamcrest.Matchers.instanceOf(String.class)), org.hamcrest.Matchers.is("High")))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.category_spinner)).perform(ViewActions.click());
        Espresso.onData(org.hamcrest.Matchers.allOf(org.hamcrest.Matchers.is(org.hamcrest.Matchers.instanceOf(String.class)), org.hamcrest.Matchers.is("Work")))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.button_select_due_date)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.button_select_due_time)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.notes_edit_text))
                .perform(ViewActions.typeText("Test description"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button_add_activity))
                .perform(ViewActions.scrollTo(), ViewActions.click());

        // 2. Überprüfe, ob die Aktivität in der Liste angezeigt wird
        Espresso.onView(ViewMatchers.withText("E2E Test Activity"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // 3. Navigiere zu den Aktivitätsdetails
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        Espresso.onView(ViewMatchers.withId(R.id.details_activity_layout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.text_view_activity_title))
                .check(ViewAssertions.matches(ViewMatchers.withText("E2E Test Activity")));

        // 4. Navigiere zurück zur MainActivity
        Espresso.pressBack();
        Espresso.onView(ViewMatchers.withId(R.id.main_activity_layout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // 5. Bearbeite die Aktivität
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.btn_edit)));

        Espresso.onView(ViewMatchers.withId(R.id.text_view_activity))
                .perform(ViewActions.replaceText("Updated E2E Activity"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.priority_spinner)).perform(ViewActions.click());
        Espresso.onData(org.hamcrest.Matchers.allOf(org.hamcrest.Matchers.is(org.hamcrest.Matchers.instanceOf(String.class)), org.hamcrest.Matchers.is("Medium")))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.category_spinner)).perform(ViewActions.click());
        Espresso.onData(org.hamcrest.Matchers.allOf(org.hamcrest.Matchers.is(org.hamcrest.Matchers.instanceOf(String.class)), org.hamcrest.Matchers.is("Personal")))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.button_select_due_date)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.button_select_due_time)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.notes_edit_text))
                .perform(ViewActions.replaceText("Updated description"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button_edit_activity))
                .perform(ViewActions.scrollTo(), ViewActions.click());

        // 6. Überprüfe die geänderte Aktivität
        Espresso.onView(ViewMatchers.withText("Updated E2E Activity"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // 7. Navigiere zur MapActivity
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.button_confirm_location)));
        Espresso.onView(ViewMatchers.withId(R.id.button_confirm_location))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Bestätige den Standort
        Espresso.onView(ViewMatchers.withId(R.id.button_confirm_location))
                .perform(ViewActions.click());

        // 8. Lösche die Aktivität
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.swipeLeft()));
        Espresso.onView(ViewMatchers.withText("Updated E2E Activity"))
                .check(ViewAssertions.doesNotExist());
    }

    private ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified ID.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View childView = view.findViewById(id);
                if (childView != null) {
                    childView.performClick();
                }
            }
        };
    }
}