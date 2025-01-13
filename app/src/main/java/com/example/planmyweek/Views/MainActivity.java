package com.example.planmyweek.Views;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.planmyweek.Views.Adapter.ActivityAdapter;
import com.example.planmyweek.modells.Comman.Activity;
import com.example.planmyweek.Controller.ActivityController;
import com.example.planmyweek.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ScrollView activityScrollView;
    private ActivityController activityController;
    private List<Activity> activityData;
    private ActivityAdapter adapter;
    FloatingActionButton fabAddActivity;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityData = new ArrayList<>();
        activityController = new ActivityController(this);
        fabAddActivity = findViewById(R.id.fab_add_activity);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load activities through ActivityController
        loadActivities();

        // Set up event listeners
        adapter.setOnItemClickListener(new ActivityAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("activityId", activityData.get(position).getId()); // Pass activity id to EditActivity
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteActivity(position);
            }

            @Override
            public void onCheckboxClick(int position) {
                markActivityAsComplete(position);
            }

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, ActivityDetailsActivity.class);
                intent.putExtra("activityId", activityData.get(position).getId());
                startActivity(intent);
            }
        });

        fabAddActivity.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddActivityActivity.class);
            startActivity(intent);
        });

        System.out.println("Main Activity RecyclerView items count: " + adapter.getItemCount());
    }

    public void loadActivities() {
        activityData.clear();
        activityData.addAll(activityController.getActivities()); // Load activities via ActivityController
        System.out.println("Activities in MainActivity after loadActivities: " + activityData.size());
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new ActivityAdapter(this, activityData);
            recyclerView.setAdapter(adapter);
        }
    }

    private void markActivityAsComplete(int position) {
        Activity activity = activityData.get(position);

        activityController.markActivityAsComplete(activity);

        adapter.notifyItemChanged(position);
        Toast.makeText(MainActivity.this, activity.isCompleted() ? "Activity Completed" : "Activity Not Completed", Toast.LENGTH_SHORT).show();
    }

    private void deleteActivity(int position) {
        Activity activity = activityData.get(position);
        activityController.deleteActivity(activity.getId()); // Delete via ActivityController
        activityData.remove(position);
        adapter.notifyItemRemoved(position);
        Toast.makeText(MainActivity.this, "Activity Deleted", Toast.LENGTH_SHORT).show();
    }
}
