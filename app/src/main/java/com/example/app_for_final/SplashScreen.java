package com.example.app_for_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

public class SplashScreen extends AppCompatActivity {

    private static final String LOG_TAG = SplashScreen.class.getName();

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        mImageView = (ImageView) findViewById(R.id.image_view_logo);

        YoYo.with(Techniques.Bounce).withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                Log.d(LOG_TAG, "Logo animation started");
            }

            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                Log.d(LOG_TAG, "Logo animation ended");
                showMainActivity();
            }

            @Override
            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                Log.d(LOG_TAG, "Logo animation cancelled");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.d(LOG_TAG, "Logo animation repeat");
            }
        }).duration(2500).delay(500).playOn(mImageView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void showMainActivity() {
        Log.d(LOG_TAG, "Will show " + MainActivity.class.getName());
        Intent intent = new Intent(this, neo.class);
        startActivity(intent);
        finish();
    }

}