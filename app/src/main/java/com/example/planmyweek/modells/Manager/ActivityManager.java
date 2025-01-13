package com.example.planmyweek.Manager;

import android.content.Context;

import com.example.planmyweek.DB.DatabaseManager;
import com.example.planmyweek.Comman.Activity;

import java.util.List;

public class ActivityManager implements ActivityCoordinator {

    private DatabaseManager dbManager;

    public ActivityManager(Context context) {
        dbManager = new DatabaseManager(context);
    }

    // Constructor for testing (dependency injection)
    public ActivityManager(DatabaseManager databaseManager) {
        this.dbManager = databaseManager;
    }

    @Override
    public long addActivity(Activity activity) {
        return dbManager.addActivity(activity);
    }

    @Override
    public List<Activity> getAllActivities() {
        return dbManager.getAllActivities();
    }

    @Override
    public void updateActivity(Activity activity) {
        dbManager.updateActivity(activity);
    }

    @Override
    public void deleteActivity(int id) {
        dbManager.deleteActivity(id);
    }

    @Override
    public Activity getActivityById(int id) {
        return dbManager.getActivityById(id);
    }
}
