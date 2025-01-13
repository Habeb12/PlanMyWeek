package com.example.planmyweek.Views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planmyweek.modells.Comman.Activity;
import com.example.planmyweek.Controller.ActivityController;
import com.example.planmyweek.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    private TextView selectedDateTextView;
    private TextView selectedTimeTextView;
    private Spinner categorySpinner;
    private Spinner prioritySpinner;
    private EditText descriptionEditText; // Renamed from notesEditText
    private EditText text_view_activity;

    private ActivityController activityController;
    private Calendar calendar;
    private int activityId;
    private Activity activity;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private MapView locationMapView;
    private String selectedLocation = "";
    private static final int LOCATION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activity);

        activityController = new ActivityController(this);

        Intent intent = getIntent();
        activityId = intent.getIntExtra("activityId", -1); // Get the activity id from the intent

        locationMapView = findViewById(R.id.location_map);
        locationMapView.setMultiTouchControls(true);
        locationMapView.getController().setZoom(15.0);

        text_view_activity = findViewById(R.id.text_view_activity);
        selectedDateTextView = findViewById(R.id.selected_date_text_view);
        selectedTimeTextView = findViewById(R.id.selected_time_text_view);
        categorySpinner = findViewById(R.id.category_spinner);
        prioritySpinner = findViewById(R.id.priority_spinner);
        descriptionEditText = findViewById(R.id.notes_edit_text);
        Button selectDateButton = findViewById(R.id.button_select_due_date);
        Button selectTimeButton = findViewById(R.id.button_select_due_time);
        Button editActivityButton = findViewById(R.id.button_edit_activity);

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.categories_array,
                android.R.layout.simple_spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.priorities_array,
                android.R.layout.simple_spinner_item
        );
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);

        loadActivityData();

        selectDateButton.setOnClickListener(v -> showDatePickerDialog());
        selectTimeButton.setOnClickListener(v -> showTimePickerDialog());

        Button selectLocationButton = findViewById(R.id.button_select_location);

        selectLocationButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, MapActivity.class);
            startActivityForResult(intent1, LOCATION_REQUEST_CODE);
        });

        editActivityButton.setOnClickListener(v -> {
            updateActivity();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK) {
            selectedLocation = data.getStringExtra("location");

            if (selectedLocation != null) {
                String[] latLng = selectedLocation.split(",");
                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);

                GeoPoint selectedPoint = new GeoPoint(latitude, longitude);
                locationMapView.getController().setCenter(selectedPoint);

                Marker marker = new Marker(locationMapView);
                marker.setPosition(selectedPoint);
                marker.setTitle("Selected Location");
                locationMapView.getOverlays().clear();
                locationMapView.getOverlays().add(marker);

                locationMapView.invalidate();
            }
        }
    }

    private void loadActivityData() {
        activity = activityController.getActivityById(activityId);
        if (activity != null) {
            selectedDateTextView.setText(activity.getDueDate());
            selectedTimeTextView.setText(activity.getDueTime());
            descriptionEditText.setText(activity.getDescription());
            setSpinnerSelection(categorySpinner, activity.getCategory());
            setSpinnerSelection(prioritySpinner, activity.getPriority());
            text_view_activity.setText(activity.getTitle());

            if (!activity.getLocation().isEmpty()) {
                // location
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

    private void updateActivity() {
        String title = text_view_activity.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String priority = prioritySpinner.getSelectedItem().toString();
        String description = descriptionEditText.getText().toString().trim();
        String dueDate = selectedDateTextView.getText().toString().trim();
        String dueTime = selectedTimeTextView.getText().toString().trim();

        long result = activityController.updateActivity(activityId, title, priority, category, dueDate, dueTime, description, selectedLocation, activity.isCompleted());

        if (result == -2) {
            Toast.makeText(this, "All fields required!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(EditActivity.this, "Activity updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void updateDateAndTimeTextViews() {
        String dateString = String.format(Locale.getDefault(), "%02d/%02d/%d", mDay, mMonth + 1, mYear);
        selectedDateTextView.setText(dateString);

        String timeString = String.format(Locale.getDefault(), "%02d:%02d", mHour, mMinute);
        selectedTimeTextView.setText(timeString);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    mYear = year;
                    mMonth = month;
                    mDay = dayOfMonth;
                    updateDateAndTimeTextViews();
                },
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    mHour = hourOfDay;
                    mMinute = minute;
                    updateDateAndTimeTextViews();
                },
                mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
