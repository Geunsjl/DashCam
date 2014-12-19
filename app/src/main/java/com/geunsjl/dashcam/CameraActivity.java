package com.geunsjl.dashcam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Jan-Lennert on 5/12/2014.
 */
public class CameraActivity extends ActionBarActivity {



    ImageView imageView;
    Button bTakePic;
    VideoView videoView;

    static final int REQUEST_VIDEO_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        videoView = (VideoView) findViewById(R.id.videoView);
        startCamera();

    }


    private void startCamera() {


            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);


            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);

            }
            finish();
    }

    public void startCamera(View v)
    {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.setMediaController(new MediaController(this));
            videoView.requestFocus();
            videoView.start();
        }
    }
}
