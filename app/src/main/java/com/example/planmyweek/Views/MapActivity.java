package com.example.planmyweek.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planmyweek.Controller.MapController;
import com.example.planmyweek.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MapActivity extends AppCompatActivity {

    private MapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapView mapView = findViewById(R.id.map);
        mapController = new MapController(this, mapView);
        mapController.enableClickSelection(this);

        Button confirmButton = findViewById(R.id.button_confirm_location);
        confirmButton.setOnClickListener(v -> {
            GeoPoint selectedLocation = mapController.getSelectedLocation();
            if (selectedLocation != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("location", selectedLocation.getLatitude() + ", " + selectedLocation.getLongitude());
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
