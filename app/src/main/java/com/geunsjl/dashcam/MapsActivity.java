package com.geunsjl.dashcam;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;


public class MapsActivity extends FragmentActivity {

    //DBAdapter myDb;
    DatabaseHandler myDb;

    //Update camera bool
    boolean updateCam = true;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private ArrayList<LatLng> arrayPoints = new ArrayList<LatLng>();

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
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getStringExtra("showRoute").equals("showRoute")){
            showRoute();
        }
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


            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

            //get current location
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

            arrayPoints.add(loc);

            //Update camera
            MoveCamera(loc);
            //Write location to database
            myDb.addLocation(loc.latitude, loc.longitude);

            //Map leegmaken
            mMap.clear();
            //Draw route on map
            drawRouteOnMap();
        }
    };

    //Update camera angle
    public void MoveCamera(LatLng loc)
    {
        if(updateCam == true) {
            //update map position
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc, 15);
            mMap.animateCamera(cameraUpdate);
        }
    }

    //display route
    public void showRoute()
    {
        Cursor cursor = myDb.getAllLocations();
        displayRecordSet(cursor);
    }

    private void displayRecordSet(Cursor cursor) {
        mMap.clear();
        arrayPoints.clear();
        updateCam = false;
        if(cursor.moveToFirst())

            //TODO: lees de eerste plaats en ga hiernaartoe met de camera
            //Process data
            do {
                //extract from mdb
                double latitude = cursor.getDouble((int) DBAdapter.COL_LATITUTDE);
                double longtitude = cursor.getDouble((int) DBAdapter.COL_LONGTITUDE);

                LatLng loc = new LatLng(latitude, longtitude);

                arrayPoints.add(loc);

                //draw route on map
                drawRouteOnMap();
            } while (cursor.moveToNext());

        cursor.close();
    }

    private void drawRouteOnMap()
    {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(arrayPoints);
        polylineOptions
                .width(5)
                .color(Color.RED);

        //Nieuwe polyline toevoegen aan map
        mMap.addPolyline(polylineOptions);

    }


}
