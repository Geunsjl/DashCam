package com.geunsjl.dashcam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToMap(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void openMediaGalerij(View v){
        final int RESULT_GALLERY = 0;

        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent , RESULT_GALLERY );
    }

    public void goToAbout(View v){
        Intent myIntent = new Intent(this, AboutActivity.class);
        startActivity(myIntent);
    }

    public void goToDB(View v){
        Intent myIntent = new Intent(this, AndroidDatabaseManager.class);
        startActivity(myIntent);
    }

    public void goToCamera(View v){
        Intent myIntent = new Intent(this, CameraActivity.class);
        startActivity(myIntent);
    }

    public void onClick_ShowRoute(View v)
    {
        Intent ShowRoute = new Intent(this, MapsActivity.class);
        ShowRoute.putExtra("showRoute","showRoute");
        startActivity(ShowRoute);
    }


}
