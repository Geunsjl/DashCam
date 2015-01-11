package com.geunsjl.dashcam;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class ShowRoute extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    Intent intent;
    //DBAdapter myDb;
    DatabaseHandler myDb;

    private ArrayList<LatLng> arrayPoints = new ArrayList<LatLng>();

    PolylineOptions polylineOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IntentData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);
        setUpMapIfNeeded();
        myDb = new DatabaseHandler(this);

    }

    private void IntentData() {
        Intent myIntent = getIntent();
        int intValue = myIntent.getIntExtra("intRoute", 100);
        int x = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void ShowRouteFromList(long id)
    {
        Cursor cursor = myDb.getAllLocations(id);
        displayRecordSet(cursor);
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

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

    }

    private void displayRecordSet(Cursor cursor) {
        arrayPoints.clear();
        LatLng loc = null;
        if(cursor.moveToFirst())


            //TODO: lees de eerste plaats en ga hiernaartoe met de camera
            //Process data
            do {
                //extract fro mdb
                double latitude = cursor.getDouble((int) DatabaseHandler.COL_LATITUTDE);
                double longtitude = cursor.getDouble((int) DatabaseHandler.COL_LONGTITUDE);
                System.out.print(latitude);
                System.out.print(longtitude);
                loc = new LatLng(latitude, longtitude);

                arrayPoints.add(loc);

                //draw route on map
                drawRouteOnMap();
            } while (cursor.moveToNext());
        if(loc != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
        }

        cursor.close();
    }

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
