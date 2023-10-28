package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ziac.aquastpapp.R;

public class SplashScreen extends AppCompatActivity {

    TextView textView;
    LinearLayout Splashscreen;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        handlermethod();

        textView = findViewById(R.id.txt);
        imageView = findViewById(R.id.imageView);
        Animation myanimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.animation);
        textView.startAnimation(myanimation);
        Animation animation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.animationlogo);
        imageView.startAnimation(animation);
    }

    private void handlermethod() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, SliderScreen.class));
                finish();
            }
        } ,3000);
    }
}