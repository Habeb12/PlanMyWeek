package com.example.planmyweek.stage2;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.example.planmyweek.Comman.Activity;
import com.example.planmyweek.DB.DatabaseManager;
import com.example.planmyweek.Manager.ActivityManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class ActivityManagerTest {

    private ActivityManager activityManager;

    @Mock
    private DatabaseManager mockDbManager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        activityManager = new ActivityManager(mockDbManager);
    }

    @Test
    public void testCreateActivity() {
        Activity activity = new Activity(1, "New Activity", "High", "Work", "2024-11-15", "10:00", "Description", "52.52,13.405", false);

        // Mocking the database addActivity method
        when(mockDbManager.addActivity(activity)).thenReturn(1L);

        // Call the method under test
        long result = activityManager.addActivity(activity);

        // Verify interactions and assert results
        verify(mockDbManager).addActivity(activity);
        assertEquals(1L, result);
    }

    @Test
    public void testEditExistingActivity() {
        Activity updatedActivity = new Activity(1, "Updated Activity", "Medium", "Personal", "2024-11-16", "11:00", "Updated Description", "48.8566,2.3522", true);

        // Simulating the behavior of updateActivity
        doNothing().when(mockDbManager).updateActivity(updatedActivity);

        // Call the method under test
        activityManager.updateActivity(updatedActivity);

        // Verify that updateActivity was called with the correct argument
        verify(mockDbManager).updateActivity(updatedActivity);
    }

    @Test
    public void testRemoveActivity() {
        int activityId = 1;

        // Simulating the behavior of deleteActivity
        doNothing().when(mockDbManager).deleteActivity(activityId);

        // Call the method under test
        activityManager.deleteActivity(activityId);

        // Verify that deleteActivity was called with the correct argument
        verify(mockDbManager).deleteActivity(activityId);
    }

    @Test
    public void testGetActivityById() {
        Activity activity = new Activity(1, "Activity 1", "High", "Work", "2024-11-15", "10:00", "Description", "52.52,13.405", false);

        // Mocking the behavior of getActivityById
        when(mockDbManager.getActivityById(1)).thenReturn(activity);

        // Call the method under test
        Activity result = activityManager.getActivityById(1);

        // Verify interactions and assert results
        verify(mockDbManager).getActivityById(1);
        assertNotNull(result);
        assertEquals("Activity 1", result.getTitle());
    }

    @Test
    public void testGetAllActivities() {
        List<Activity> mockActivities = Arrays.asList(
                new Activity(1, "Activity 1", "High", "Work", "2024-11-15", "10:00", "Description 1", "52.52,13.405", false),
                new Activity(2, "Activity 2", "Medium", "Personal", "2024-11-16", "11:00", "Description 2", "48.8566,2.3522", true)
        );

        // Mocking the behavior of getAllActivities
        when(mockDbManager.getAllActivities()).thenReturn(mockActivities);

        // Call the method under test
        List<Activity> result = activityManager.getAllActivities();

        // Verify interactions and assert results
        verify(mockDbManager).getAllActivities();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Activity 1", result.get(0).getTitle());
        assertEquals("Activity 2", result.get(1).getTitle());
    }
}
