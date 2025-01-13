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

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

class RecyclerViewMatcher {
    private final int recyclerViewId;

    public RecyclerViewMatcher(int recyclerViewId) {
        this.recyclerViewId = recyclerViewId;
    }

    public static RecyclerViewMatcher withRecyclerView(int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public Matcher<View> atPositionOnView(final int position, final int targetViewId) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("RecyclerView with id: " + recyclerViewId +
                        " at position: " + position);
            }

            @Override
            protected boolean matchesSafely(View view) {
                RecyclerView recyclerView = view.getRootView().findViewById(recyclerViewId);
                if (recyclerView != null && recyclerView.getId() == recyclerViewId) {
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                    if (viewHolder != null) {
                        View targetView = viewHolder.itemView.findViewById(targetViewId);
                        return view == targetView;
                    }
                }
                return false;
            }
        };
    }
}

@RunWith(AndroidJUnit4.class)
public class EditActivityTest {

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
    public void testEditActivity() throws InterruptedException {
        // Step 1: Launch Edit Activity screen
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.btn_edit)));

        // Verify navigation to EditActivityActivity
        Espresso.onView(ViewMatchers.withId(R.id.edit_activity_layout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Step 2: Modify activity details
        // Update title
        Espresso.onView(ViewMatchers.withId(R.id.text_view_activity))
                .perform(ViewActions.replaceText("Updated Activity Title"), ViewActions.closeSoftKeyboard());

        // Update priority
        Espresso.onView(ViewMatchers.withId(R.id.priority_spinner)).perform(ViewActions.click());
        Espresso.onData(Matchers.allOf(Matchers.is(Matchers.instanceOf(String.class)), Matchers.is("Medium")))
                .perform(ViewActions.click());

        // Update category
        Espresso.onView(ViewMatchers.withId(R.id.category_spinner)).perform(ViewActions.click());
        Espresso.onData(Matchers.allOf(Matchers.is(Matchers.instanceOf(String.class)), Matchers.is("Personal")))
                .perform(ViewActions.click());

        // Update due date
        Espresso.onView(ViewMatchers.withId(R.id.button_select_due_date)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());

        // Update due time
        Espresso.onView(ViewMatchers.withId(R.id.button_select_due_time)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());

        // Update description
        Espresso.onView(ViewMatchers.withId(R.id.notes_edit_text))
                .perform(ViewActions.replaceText("Updated Activity Description"), ViewActions.closeSoftKeyboard());

        // Step 3: Save changes
        Espresso.onView(ViewMatchers.withId(R.id.button_edit_activity))
                .perform(ViewActions.scrollTo(), ViewActions.click());

        // Verify navigation back to MainActivity
        Espresso.onView(ViewMatchers.withId(R.id.main_activity_layout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Step 4: Verify changes in the RecyclerView
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(RecyclerViewActions.scrollToPosition(0));

        // Check that the updated title is displayed
        Espresso.onView(RecyclerViewMatcher.withRecyclerView(R.id.recyclerview).atPositionOnView(0, R.id.text_title))
                .check(ViewAssertions.matches(ViewMatchers.withText("Updated Activity Title")));
    }
}
