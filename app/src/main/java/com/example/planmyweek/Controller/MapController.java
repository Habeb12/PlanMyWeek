package com.example.planmyweek.Controller;

import android.content.Context;
import android.widget.Toast;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.events.MapEventsReceiver;

public class MapController implements MapHandler {

    private final MapView mapView;
    private GeoPoint selectedLocation;

    public MapController(Context context, MapView mapView) {
        this.mapView = mapView;
        initializeMap(context);
    }

    @Override
    public void initializeMap(Context context) {
        Configuration.getInstance().setUserAgentValue(context.getPackageName());
        mapView.setMultiTouchControls(true); // Enable pinch-to-zoom
        mapView.getController().setZoom(15.0); // Default zoom level

        // Set default location to Berlin, Germany
        GeoPoint berlin = new GeoPoint(52.5200, 13.4050);
        mapView.getController().setCenter(berlin);
    }

    @Override
    public void enableClickSelection(Context context) {
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                setMarkerAtLocation(p, "Selected Location");
                selectedLocation = p;

                // Show toast with selected coordinates
                Toast.makeText(context, "Location: " + p.getLatitude() + ", " + p.getLongitude(), Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                // Optional: Handle long press, we not needed now
                return false;
            }
        });

        mapView.getOverlays().add(mapEventsOverlay);
    }

    @Override
    public void setMarkerAtLocation(GeoPoint geoPoint, String title) {
        mapView.getOverlays().clear(); // Clear previous markers
        Marker marker = new Marker(mapView);
        marker.setPosition(geoPoint);
        marker.setTitle(title);
        mapView.getOverlays().add(marker);
        mapView.invalidate(); // Refresh the map
    }

    @Override
    public GeoPoint getSelectedLocation() {
        return selectedLocation;
    }

    @Override
    public void centerMapAt(GeoPoint geoPoint) {
        mapView.getController().setCenter(geoPoint);
    }
}
