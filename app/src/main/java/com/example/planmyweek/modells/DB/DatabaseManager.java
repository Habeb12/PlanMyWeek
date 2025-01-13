package com.example.planmyweek.modells.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.planmyweek.modells.Comman.Activity;
import com.example.planmyweek.modells.Comman.ActivityContract;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper implements DatabaseGateway {

    private static final String DATABASE_NAME = "activitylist.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ACTIVITY_TABLE =
                "CREATE TABLE " + ActivityContract.ActivityEntry.TABLE_NAME + " (" +
                        ActivityContract.ActivityEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ActivityContract.ActivityEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        ActivityContract.ActivityEntry.COLUMN_CATEGORY + " TEXT, " +
                        ActivityContract.ActivityEntry.COLUMN_PRIORITY + " TEXT, " +
                        ActivityContract.ActivityEntry.COLUMN_DESCRIPTION + " TEXT, " +
                        ActivityContract.ActivityEntry.COLUMN_DUE_DATE + " TEXT, " +
                        ActivityContract.ActivityEntry.COLUMN_DUE_TIME + " TEXT, " +
                        ActivityContract.ActivityEntry.COLUMN_COMPLETED + " INTEGER DEFAULT 0, " +
                        ActivityContract.ActivityEntry.COLUMN_LOCATION + " TEXT);";
        db.execSQL(SQL_CREATE_ACTIVITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database upgrade logic here
    }

    @Override
    public long addActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ActivityContract.ActivityEntry.COLUMN_TITLE, activity.getTitle());
        values.put(ActivityContract.ActivityEntry.COLUMN_CATEGORY, activity.getCategory());
        values.put(ActivityContract.ActivityEntry.COLUMN_PRIORITY, activity.getPriority());
        values.put(ActivityContract.ActivityEntry.COLUMN_DESCRIPTION, activity.getDescription());
        values.put(ActivityContract.ActivityEntry.COLUMN_DUE_DATE, activity.getDueDate());
        values.put(ActivityContract.ActivityEntry.COLUMN_DUE_TIME, activity.getDueTime());
        values.put(ActivityContract.ActivityEntry.COLUMN_COMPLETED, activity.isCompleted() ? 1 : 0);
        values.put(ActivityContract.ActivityEntry.COLUMN_LOCATION, activity.getLocation()); // Add location

        long result = db.insert(ActivityContract.ActivityEntry.TABLE_NAME, null, values);
        db.close();
        return result;
    }

    @Override
    public List<Activity> getAllActivities() {
        List<Activity> activities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ActivityContract.ActivityEntry.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(ActivityContract.ActivityEntry._ID));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_TITLE));
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_CATEGORY));
            @SuppressLint("Range") String priority = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_PRIORITY));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_DESCRIPTION));
            @SuppressLint("Range") String dueDate = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_DUE_DATE));
            @SuppressLint("Range") String dueTime = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_DUE_TIME));
            @SuppressLint("Range") boolean completed = cursor.getInt(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_COMPLETED)) == 1;
            @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_LOCATION));

            activities.add(new Activity(id, title, priority, category, dueDate, dueTime, description, location, completed));
        }

        cursor.close();
        db.close();
        return activities;
    }

    @Override
    public void updateActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ActivityContract.ActivityEntry.COLUMN_TITLE, activity.getTitle());
        values.put(ActivityContract.ActivityEntry._ID, activity.getId());
        values.put(ActivityContract.ActivityEntry.COLUMN_CATEGORY, activity.getCategory());
        values.put(ActivityContract.ActivityEntry.COLUMN_PRIORITY, activity.getPriority());
        values.put(ActivityContract.ActivityEntry.COLUMN_DESCRIPTION, activity.getDescription());
        values.put(ActivityContract.ActivityEntry.COLUMN_DUE_DATE, activity.getDueDate());
        values.put(ActivityContract.ActivityEntry.COLUMN_DUE_TIME, activity.getDueTime());
        values.put(ActivityContract.ActivityEntry.COLUMN_COMPLETED, activity.isCompleted() ? 1 : 0);
        values.put(ActivityContract.ActivityEntry.COLUMN_LOCATION, activity.getLocation()); // Update location

        db.update(ActivityContract.ActivityEntry.TABLE_NAME, values, ActivityContract.ActivityEntry._ID + "=?", new String[]{String.valueOf(activity.getId())});
        db.close();
    }

    @Override
    public void deleteActivity(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ActivityContract.ActivityEntry.TABLE_NAME, ActivityContract.ActivityEntry._ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public Activity getActivityById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Activity activity = null;

        Cursor cursor = db.query(
                ActivityContract.ActivityEntry.TABLE_NAME,
                null,
                ActivityContract.ActivityEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_TITLE));
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_CATEGORY));
            @SuppressLint("Range") String priority = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_PRIORITY));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_DESCRIPTION));
            @SuppressLint("Range") String dueDate = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_DUE_DATE));
            @SuppressLint("Range") String dueTime = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_DUE_TIME));
            @SuppressLint("Range") boolean completed = cursor.getInt(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_COMPLETED)) == 1;
            @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex(ActivityContract.ActivityEntry.COLUMN_LOCATION));

            activity = new Activity(id, title, priority, category, dueDate, dueTime, description, location, completed);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return activity;
    }
}
