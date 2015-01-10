package com.geunsjl.dashcam;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity {

    // LIstview
    private ListView lv;
    ArrayAdapter<LatLng> arrayAdapter;

    //DBAdapter myDb;
    DatabaseHandler myDb;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private ArrayList<LatLng> arrayPoints = new ArrayList<LatLng>();

    //Laatste route
    int lastRoute = 1;

    PolylineOptions polylineOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        myDb = new DatabaseHandler(this);
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        mMap.setMyLocationEnabled(true);
        arrayPoints.clear();
        checkRouteNumber();
    }

    private void checkRouteNumber() {
        lastRoute = myDb.getLatestRouteNumber();
        lastRoute++;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {


            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

            //loc in arraylist
            arrayPoints.add(loc);

            //Write location to database
            myDb.addLocation(loc.latitude, loc.longitude, lastRoute);

            //UPdate camera
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

            //Draw route on map
            drawRouteOnMap();
        }
    };

    private void drawRouteOnMap()
    {
        //Map leegmaken

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(arrayPoints);
        polylineOptions
                .width(5)
                .color(Color.RED);

        //Nieuwe polyline toevoegen aan map
        mMap.addPolyline(polylineOptions);

    }


}
