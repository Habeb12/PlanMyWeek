package com.example.planmyweek.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planmyweek.modells.Comman.Activity;
import com.example.planmyweek.Controller.ActivityController;
import com.example.planmyweek.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class ActivityDetailsActivity extends AppCompatActivity {

    private ActivityController activityController;
    private Activity activity;
    private TextView textViewTitle;
    private TextView textViewCategory;
    private TextView textViewPriority;
    private TextView textViewDueDate;
    private TextView textViewDueTime;
    private TextView textViewDescription;
    private MapView locationMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_details);

        // Initialize views
        textViewTitle = findViewById(R.id.text_view_activity_title);
        textViewCategory = findViewById(R.id.text_view_category_value);
        textViewPriority = findViewById(R.id.text_view_priority_value);
        textViewDueDate = findViewById(R.id.text_view_due_date_value);
        textViewDueTime = findViewById(R.id.text_view_due_time_value);
        textViewDescription = findViewById(R.id.text_view_description_value);

        // Get data from intent
        Intent intent = getIntent();
        int activityId = intent.getIntExtra("activityId", -1);

        activityController = new ActivityController(this);
        activity = activityController.getActivityById(activityId);

        // Set data to views
        textViewTitle.setText(activity.getTitle());
        textViewCategory.setText(activity.getCategory());
        textViewPriority.setText(activity.getPriority());
        textViewDueDate.setText(activity.getDueDate());
        textViewDueTime.setText(activity.getDueTime());
        textViewDescription.setText(activity.getDescription());

        locationMapView = findViewById(R.id.location_map_view);

        // Configure map and set location
        locationMapView.setMultiTouchControls(true);
        locationMapView.getController().setZoom(15.0);

        if (!activity.getLocation().isEmpty()) {

            String[] latLng = activity.getLocation().split(",");
            double latitude = Double.parseDouble(latLng[0]);
            double longitude = Double.parseDouble(latLng[1]);

            GeoPoint geoPoint = new GeoPoint(latitude, longitude);
            locationMapView.getController().setCenter(geoPoint);

            Marker marker = new Marker(locationMapView);
            marker.setPosition(geoPoint);
            marker.setTitle("Activity Location");
            locationMapView.getOverlays().add(marker);
        }
    }
}
