package com.example.planmyweek.Controller;

import com.example.planmyweek.modells.Comman.Activity;

import java.util.List;

public interface ActivityHandler {

    public long addActivity(String title, String priority, String category, String dueDate, String dueTime,
                            String description, String location, boolean completed);
    public List<Activity> getActivities();
    public long updateActivity(int id, String title, String priority, String category, String dueDate,
                               String dueTime, String description, String location, boolean completed);
    public void deleteActivity(int id) ;
    public Activity getActivityById(int id);
    public void markActivityAsComplete(Activity activity) ;
}
