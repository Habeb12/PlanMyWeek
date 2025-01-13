package com.example.planmyweek.modells.Manager;

import com.example.planmyweek.modells.Comman.Activity;

import java.util.List;

public interface ActivityCoordinator {

    public long addActivity(Activity activity) ;
    public List<Activity> getAllActivities();
    public void updateActivity(Activity activity);
    public void deleteActivity(int id) ;
    public Activity getActivityById(int id) ;
}
