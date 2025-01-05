package com.example.planmyweek.Views;

import android.annotation.SuppressLint;
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

import com.example.planmyweek.Controller.ActivityController;
import com.example.planmyweek.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.Calendar;
import java.util.Locale;

public class AddActivityActivity extends AppCompatActivity {

    private static final int LOCATION_REQUEST_CODE = 1;

    private TextView selectedDateTextView;
    private TextView selectedTimeTextView;
    private EditText titleEditText;
    private Spinner categorySpinner;
    private Spinner prioritySpinner;
    private EditText descriptionEditText;
    private TextView selectedLocationTextView;

    private ActivityController activityController;
    private Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private String selectedLocation = "";
    private MapView locationMapView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);

        activityController = new ActivityController(this);

        locationMapView = findViewById(R.id.location_map);
        locationMapView.setMultiTouchControls(true); // Enable pinch-to-zoom
        locationMapView.getController().setZoom(15.0); // Set default zoom level

        selectedDateTextView = findViewById(R.id.selected_date_text_view);
        selectedTimeTextView = findViewById(R.id.selected_time_text_view);
        titleEditText = findViewById(R.id.activity_edit_text);
        categorySpinner = findViewById(R.id.category_spinner);
        prioritySpinner = findViewById(R.id.priority_spinner);
        descriptionEditText = findViewById(R.id.notes_edit_text);
        selectedLocationTextView = findViewById(R.id.text_view_location);

        Button selectDateButton = findViewById(R.id.button_select_due_date);
        Button selectTimeButton = findViewById(R.id.button_select_due_time);
        Button addActivityButton = findViewById(R.id.button_add_activity);
        Button selectLocationButton = findViewById(R.id.button_select_location);

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

        updateDateAndTimeTextViews();

        selectDateButton.setOnClickListener(v -> showDatePickerDialog());
        selectTimeButton.setOnClickListener(v -> showTimePickerDialog());
        selectLocationButton.setOnClickListener(v -> openMapActivity());

        addActivityButton.setOnClickListener(v -> {
            addActivity();
        });
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

    private void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivityForResult(intent, LOCATION_REQUEST_CODE);
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

                // Update MapView
                GeoPoint selectedPoint = new GeoPoint(latitude, longitude);
                locationMapView.getController().setCenter(selectedPoint);

                // Add a marker
                Marker marker = new Marker(locationMapView);
                marker.setPosition(selectedPoint);
                marker.setTitle("Selected Location");
                locationMapView.getOverlays().clear();
                locationMapView.getOverlays().add(marker);

                locationMapView.invalidate(); // Refresh map
            }
        }
    }

    private void addActivity() {
        String title = titleEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String priority = prioritySpinner.getSelectedItem().toString();
        String description = descriptionEditText.getText().toString().trim();
        String dueDate = selectedDateTextView.getText().toString().trim();
        String dueTime = selectedTimeTextView.getText().toString().trim();

        long result = activityController.addActivity(title, priority, category, dueDate, dueTime, description, selectedLocation, false);

        if (result == -2) {
            Toast.makeText(this, "All fields required!!", Toast.LENGTH_SHORT).show();
        } else if (result == -1) {
            Toast.makeText(this, "Failed to add activity", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Activity added successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddActivityActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
