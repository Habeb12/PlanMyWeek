package com.example.planmyweek.stage1;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.planmyweek.modells.Comman.Activity;
import com.example.planmyweek.Controller.ActivityController;
import com.example.planmyweek.R;
import com.example.planmyweek.Views.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ActivityDetailsTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUpMockData() {
        activityRule.getActivity().runOnUiThread(() -> {
            ActivityController controller = new ActivityController(activityRule.getActivity());

            for (int i = 1; i <= 5; i++) {
                Activity mockActivity = new Activity(
                        i,
                        "Activity " + i,
                        "High",
                        "Work",
                        "2024-11-15",
                        "10:00",
                        "Description of activity " + i,
                        "52.52,13.405",
                        false
                );
                controller.addActivity(mockActivity.getTitle(), mockActivity.getPriority(),
                        mockActivity.getCategory(), mockActivity.getDueDate(), mockActivity.getDueTime(),
                        mockActivity.getDescription(), mockActivity.getLocation(), mockActivity.isCompleted());
            }

            // Refresh RecyclerView in MainActivity
            MainActivity mainActivity = activityRule.getActivity();
            mainActivity.loadActivities();
        });
    }

    @Test
    public void testDisplayActivityDetails() {
        // Step 1: Click on the first activity in the RecyclerView
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

        // Verify navigation to ActivityDetailsActivity
        Espresso.onView(ViewMatchers.withId(R.id.details_activity_layout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Step 2: Verify the details of the clicked activity
        Espresso.onView(ViewMatchers.withId(R.id.text_view_activity_title))
                .check(ViewAssertions.matches(ViewMatchers.withText("Activity 1")));

        Espresso.onView(ViewMatchers.withId(R.id.text_view_priority_value))
                .check(ViewAssertions.matches(ViewMatchers.withText("High")));

        Espresso.onView(ViewMatchers.withId(R.id.text_view_category_value))
                .check(ViewAssertions.matches(ViewMatchers.withText("Work")));

        Espresso.onView(ViewMatchers.withId(R.id.text_view_due_date_value))
                .check(ViewAssertions.matches(ViewMatchers.withText("2024-11-15")));

        Espresso.onView(ViewMatchers.withId(R.id.text_view_due_time_value))
                .check(ViewAssertions.matches(ViewMatchers.withText("10:00")));

        Espresso.onView(ViewMatchers.withId(R.id.text_view_description_value))
                .check(ViewAssertions.matches(ViewMatchers.withText("Description of activity 1")));
    }
}
