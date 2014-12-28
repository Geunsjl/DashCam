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

import java.util.Calendar;


public class MapsActivity extends FragmentActivity {

    DBAdapter myDb;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        openDB();
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);


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


    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        myDb.close();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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
        mMap.setMyLocationEnabled(true);
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {

            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

            //update map position
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc, 2);
            mMap.animateCamera(cameraUpdate);

            //Write location to database
            myDb.insertRow(mydate, loc.latitude, loc.longitude);

            //Draw route on map
            drawRouteOnMap(loc.latitude, loc.longitude);
        }
    };

    //display route
    public void showRoute()
    {
        Cursor cursor = myDb.getAllRows();
        displayRecordSet(cursor);
    }

    private void displayRecordSet(Cursor cursor) {

        PolylineOptions line = new PolylineOptions();
        //check if there is data
        if(cursor.moveToFirst())
            //Process data
            do {
                double latitude = cursor.getDouble((int) DBAdapter.COL_LATITUTDE);
                double longtitude = cursor.getDouble((int) DBAdapter.COL_LONGTITUDE);

                drawRouteOnMap(latitude, longtitude);
            } while (cursor.moveToNext());

        cursor.close();
    }

    private void drawRouteOnMap(double latitude, double longtitude)
    {
        PolylineOptions rectOptions = new PolylineOptions()
                .add((new LatLng(latitude, longtitude)));

        mMap.addPolyline((rectOptions)
                .color(Color.RED));
    }


}
