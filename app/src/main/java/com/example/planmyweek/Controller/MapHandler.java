package com.example.planmyweek.Controller;

import android.content.Context;
import org.osmdroid.util.GeoPoint;

public interface MapHandler {
    public void initializeMap(Context context) ;
    public void enableClickSelection(Context context);
    public void setMarkerAtLocation(GeoPoint geoPoint, String title);
    public GeoPoint getSelectedLocation();
    public void centerMapAt(GeoPoint geoPoint);
}
