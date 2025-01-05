package com.example.planmyweek.Controller;

import android.content.Context;

import com.example.planmyweek.Manager.ActivityManager;
import com.example.planmyweek.Comman.Activity;

import java.util.List;

public class ActivityController implements ActivityHandler {

    private final ActivityManager activityManager;

    // Constructor for production
    public ActivityController(Context context) {
        activityManager = new ActivityManager(context); // Initialize ActivityManager for database operations
    }

    // Constructor for testing (dependency injection)
    public ActivityController(ActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    @Override
    public long addActivity(String title, String priority, String category, String dueDate, String dueTime, String description, String location, boolean completed) {
        if (title.isEmpty() || priority.isEmpty() || category.isEmpty() || dueDate.isEmpty() || dueTime.isEmpty() || description.isEmpty()) {
            return -2;
        } else {
            Activity activity = new Activity(0, title, priority, category, dueDate, dueTime, description, location, completed);
            return activityManager.addActivity(activity);
        }
    }

    @Override
    public List<Activity> getActivities() {
        return activityManager.getAllActivities();
    }

    @Override
    public long updateActivity(int id, String title, String priority, String category, String dueDate, String dueTime, String description, String location, boolean completed) {
        if (title.isEmpty() || priority.isEmpty() || category.isEmpty() || dueDate.isEmpty() || dueTime.isEmpty() || description.isEmpty()) {
            return -2;
        } else {
            Activity activity = new Activity(id, title, priority, category, dueDate, dueTime, description, location, completed);
            activityManager.updateActivity(activity);
            return 1;
        }
    }

    @Override
    public void deleteActivity(int id) {
        activityManager.deleteActivity(id);
    }

    @Override
    public Activity getActivityById(int id) {
        return activityManager.getActivityById(id);
    }

    @Override
    public void markActivityAsComplete(Activity activity) {
        activity.setCompleted(!activity.isCompleted());
        updateActivity(activity.getId(), activity.getTitle(), activity.getPriority(), activity.getCategory(),
                activity.getDueDate(), activity.getDueTime(), activity.getDescription(), activity.getLocation(), activity.isCompleted());
    }
}
