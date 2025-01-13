package com.example.planmyweek.stage3;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.example.planmyweek.modells.Comman.Activity;
import com.example.planmyweek.modells.DB.DatabaseManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class MockActivityDatabaseTest {

    @Mock
    private DatabaseManager mockDatabaseManager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveActivity() {
        // Arrange
        Activity activity = new Activity(1, "Test Activity", "High", "Work", "2024-11-15", "10:00",
                "Description", "52.52,13.405", false);
        when(mockDatabaseManager.addActivity(activity)).thenReturn(1L);

        // Act
        long result = mockDatabaseManager.addActivity(activity);

        // Assert
        verify(mockDatabaseManager).addActivity(activity);
        assertEquals(1L, result);
    }

    @Test
    public void testLoadActivity() {
        // Arrange
        Activity mockActivity = new Activity(1, "Test Activity", "High", "Work", "2024-11-15", "10:00",
                "Description", "52.52,13.405", false);
        when(mockDatabaseManager.getActivityById(1)).thenReturn(mockActivity);

        // Act
        Activity retrievedActivity = mockDatabaseManager.getActivityById(1);

        // Assert
        verify(mockDatabaseManager).getActivityById(1);
        assertNotNull(retrievedActivity);
        assertEquals("Test Activity", retrievedActivity.getTitle());
        assertEquals("High", retrievedActivity.getPriority());
    }

    @Test
    public void testLoadAllActivities() {
        // Arrange
        List<Activity> mockActivities = Arrays.asList(
                new Activity(1, "Activity 1", "High", "Work", "2024-11-15", "10:00",
                        "Description 1", "52.52,13.405", false),
                new Activity(2, "Activity 2", "Medium", "Personal", "2024-11-16", "11:00",
                        "Description 2", "48.8566,2.3522", true)
        );
        when(mockDatabaseManager.getAllActivities()).thenReturn(mockActivities);

        // Act
        List<Activity> retrievedActivities = mockDatabaseManager.getAllActivities();

        // Assert
        verify(mockDatabaseManager).getAllActivities();
        assertNotNull(retrievedActivities);
        assertEquals(2, retrievedActivities.size());
        assertEquals("Activity 1", retrievedActivities.get(0).getTitle());
        assertEquals("Activity 2", retrievedActivities.get(1).getTitle());
    }
}
