package com.example.planmyweek.stage2;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.planmyweek.Comman.Activity;
import com.example.planmyweek.Comman.ActivityContract;
import com.example.planmyweek.DB.DatabaseManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseManagerTest {

    private DatabaseManager dbManager;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbManager = new DatabaseManager(context);
        dbManager.getWritableDatabase().execSQL("DELETE FROM " + ActivityContract.ActivityEntry.TABLE_NAME); // Clear the database
    }

    @After
    public void tearDown() {
        dbManager.close();
    }

    @Test
    public void testAddActivity() {
        Activity activity = new Activity(0, "New Activity", "High", "Work", "2024-11-15", "10:00", "Description", "52.52,13.405", false);

        long id = dbManager.addActivity(activity);

        assertTrue(id > 0); // Verify the activity was added successfully

        Activity retrievedActivity = dbManager.getActivityById((int) id);

        assertNotNull(retrievedActivity);
        assertEquals("New Activity", retrievedActivity.getTitle());
        assertEquals("High", retrievedActivity.getPriority());
        assertEquals("Work", retrievedActivity.getCategory());
        assertEquals("2024-11-15", retrievedActivity.getDueDate());
        assertEquals("10:00", retrievedActivity.getDueTime());
        assertEquals("Description", retrievedActivity.getDescription());
        assertEquals("52.52,13.405", retrievedActivity.getLocation());
        assertFalse(retrievedActivity.isCompleted());
    }

    @Test
    public void testUpdateActivity() {
        Activity activity = new Activity(0, "New Activity", "High", "Work", "2024-11-15", "10:00", "Description", "52.52,13.405", false);

        long id = dbManager.addActivity(activity);

        Activity updatedActivity = new Activity((int) id, "Updated Activity", "Medium", "Personal", "2024-11-16", "11:00", "Updated Description", "48.8566,2.3522", true);

        dbManager.updateActivity(updatedActivity);

        Activity retrievedActivity = dbManager.getActivityById((int) id);

        assertNotNull(retrievedActivity);
        assertEquals("Updated Activity", retrievedActivity.getTitle());
        assertEquals("Medium", retrievedActivity.getPriority());
        assertEquals("Personal", retrievedActivity.getCategory());
        assertEquals("2024-11-16", retrievedActivity.getDueDate());
        assertEquals("11:00", retrievedActivity.getDueTime());
        assertEquals("Updated Description", retrievedActivity.getDescription());
        assertEquals("48.8566,2.3522", retrievedActivity.getLocation());
        assertTrue(retrievedActivity.isCompleted());
    }

    @Test
    public void testDeleteActivity() {
        Activity activity = new Activity(0, "New Activity", "High", "Work", "2024-11-15", "10:00", "Description", "52.52,13.405", false);

        long id = dbManager.addActivity(activity);

        dbManager.deleteActivity((int) id);

        Activity retrievedActivity = dbManager.getActivityById((int) id);

        assertNull(retrievedActivity); // Verify the activity was deleted
    }
}
