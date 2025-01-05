package com.example.planmyweek.stage1;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.planmyweek.R;
import com.example.planmyweek.Views.MapActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MapViewTest {

    @Rule
    public ActivityTestRule<MapActivity> activityRule = new ActivityTestRule<>(MapActivity.class);

    @Test
    public void testSetLocationOnMap() {
        activityRule.getActivity().runOnUiThread(() -> {
            // Get reference to the MapView
            org.osmdroid.views.MapView mapView = activityRule.getActivity().findViewById(R.id.map);

            // Simulate selecting a location on the map
            org.osmdroid.util.GeoPoint testPoint = new org.osmdroid.util.GeoPoint(48.8566, 2.3522); // Paris, France
            mapView.getController().setCenter(testPoint);

            // Confirm the marker is placed
            com.example.planmyweek.Controller.MapController mapController =
                    new com.example.planmyweek.Controller.MapController(activityRule.getActivity(), mapView);
            mapController.setMarkerAtLocation(testPoint, "Test Location");
        });

        // Verify the confirm location button is displayed
        Espresso.onView(ViewMatchers.withId(R.id.button_confirm_location))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Click on confirm location button
        Espresso.onView(ViewMatchers.withId(R.id.button_confirm_location))
                .perform(ViewActions.click());
    }


    @Test
    public void testDisplayLocationOnMap() {
        activityRule.getActivity().runOnUiThread(() -> {
            // Simulate loading a predefined location
            org.osmdroid.util.GeoPoint existingPoint = new org.osmdroid.util.GeoPoint(52.52, 13.405); // Berlin, Germany
            com.example.planmyweek.Controller.MapController mapController =
                    new com.example.planmyweek.Controller.MapController(
                            activityRule.getActivity(),
                            activityRule.getActivity().findViewById(R.id.map)
                    );
            mapController.centerMapAt(existingPoint);
            mapController.setMarkerAtLocation(existingPoint, "Predefined Location");
        });

        // Verify the confirm location button is displayed
        Espresso.onView(ViewMatchers.withId(R.id.button_confirm_location))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

}
