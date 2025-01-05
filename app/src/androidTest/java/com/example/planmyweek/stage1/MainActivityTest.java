package com.example.planmyweek.stage1;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.planmyweek.Comman.Activity;
import com.example.planmyweek.Controller.ActivityController;
import com.example.planmyweek.R;
import com.example.planmyweek.Views.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.view.View;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import org.hamcrest.Matcher;

import java.util.List;

class MyViewAction {
    public static ViewAction clickChildViewWithId(final int id) {
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


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

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

            // Debugging: Log activities
            List<Activity> activities = controller.getActivities();
            System.out.println("Activities after mock data injection: " + activities.size());

            // Refresh MainActivity's RecyclerView data
            MainActivity mainActivity = activityRule.getActivity();
            mainActivity.loadActivities();
            System.out.println("RecyclerView refreshed in MainActivity.");
        });
    }

    @Test
    public void testNavigationToAddActivityScreen() {
        Espresso.onView(ViewMatchers.withId(R.id.fab_add_activity))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.add_activity_layout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testNavigationToEditActivityScreen() {
        // Click the Edit button in the first item of RecyclerView
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewAction.clickChildViewWithId(R.id.btn_edit)));

        // Verify navigation to Edit Activity Screen
        Espresso.onView(ViewMatchers.withId(R.id.edit_activity_layout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testNavigationToActivityDetailsScreen() throws InterruptedException {

        // Perform click on the first activity item in RecyclerView
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

        // Verify navigation to Activity Details Screen
        Espresso.onView(ViewMatchers.withId(R.id.details_activity_layout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
