package com.example.planmyweek.stage2;

import static org.mockito.Mockito.*;

import com.example.planmyweek.Controller.ActivityController;
import com.example.planmyweek.Manager.ActivityManager;
import com.example.planmyweek.Comman.Activity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ActivityControllerTest {

    private ActivityController activityController;

    @Mock
    private ActivityManager mockActivityManager;

    @Before
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create ActivityController with mocked ActivityManager
        activityController = new ActivityController(mockActivityManager);
    }

    @Test
    public void testHandleAddActivity() {
        Activity activity = new Activity(1, "Test Activity", "High", "Work", "2024-11-15", "10:00",
                "Description", "52.52,13.405", false);

        when(mockActivityManager.addActivity(any(Activity.class))).thenReturn(1L);

        long result = activityController.addActivity(
                activity.getTitle(),
                activity.getPriority(),
                activity.getCategory(),
                activity.getDueDate(),
                activity.getDueTime(),
                activity.getDescription(),
                activity.getLocation(),
                activity.isCompleted()
        );

        verify(mockActivityManager).addActivity(any(Activity.class));
        assertEquals(1L, result);
    }

    @Test
    public void testHandleEditActivity() {
        // Step 1: Simulate retrieving the existing activity
        Activity existingActivity = new Activity(1, "Existing Activity", "High", "Work", "2024-11-15", "10:00",
                "Existing Description", "52.52,13.405", false);

        // Mock the return value of getActivityById
        when(mockActivityManager.getActivityById(1)).thenReturn(existingActivity);

        // Step 2: Prepare the updated activity
        Activity updatedActivity = new Activity(1, "Updated Activity", "Medium", "Personal", "2024-11-16", "11:00",
                "Updated Description", "48.8566,2.3522", true);

        // Mock the void updateActivity method
        doNothing().when(mockActivityManager).updateActivity(any(Activity.class));

        // Step 3: Simulate the process of fetching and updating
        Activity fetchedActivity = activityController.getActivityById(1);
        assertNotNull(fetchedActivity);

        long result = activityController.updateActivity(
                updatedActivity.getId(),
                updatedActivity.getTitle(),
                updatedActivity.getPriority(),
                updatedActivity.getCategory(),
                updatedActivity.getDueDate(),
                updatedActivity.getDueTime(),
                updatedActivity.getDescription(),
                updatedActivity.getLocation(),
                updatedActivity.isCompleted()
        );

        // Step 4: Verify interactions
        verify(mockActivityManager).getActivityById(1); // Ensure the activity was fetched
        verify(mockActivityManager).updateActivity(any(Activity.class)); // Ensure the activity was updated
        assertEquals(1, result); // Ensure the return value is correct
    }

    @Test
    public void testHandleListActivities() {
        List<Activity> mockActivities = Arrays.asList(
                new Activity(1, "Activity 1", "High", "Work", "2024-11-15", "10:00",
                        "Description 1", "52.52,13.405", false),
                new Activity(2, "Activity 2", "Medium", "Personal", "2024-11-16", "11:00",
                        "Description 2", "48.8566,2.3522", true)
        );

        when(mockActivityManager.getAllActivities()).thenReturn(mockActivities);

        List<Activity> activities = activityController.getActivities();

        verify(mockActivityManager).getAllActivities();
        assertNotNull(activities);
        assertEquals(2, activities.size());
        assertEquals("Activity 1", activities.get(0).getTitle());
        assertEquals("Activity 2", activities.get(1).getTitle());
    }
}
