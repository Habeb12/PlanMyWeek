package com.example.planmyweek.DB;


import com.example.planmyweek.Comman.Activity;
import java.util.List;

public interface DatabaseGateway {

    public long addActivity(Activity activity);
    public List<Activity> getAllActivities();
    public void updateActivity(Activity activity);
    public void deleteActivity(int id);
    public Activity getActivityById(int id);
}
