package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ziac.aquastpapp.R;

public class SplashScreen extends AppCompatActivity {
    TextView textView;
    ImageView imageView;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        textView = findViewById(R.id.txt);
        imageView = findViewById(R.id.imageView);
        Animation myanimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.animation);
        textView.startAnimation(myanimation);
        Animation animation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.animationlogo);
        imageView.startAnimation(animation);
        handlermethod();

    }
    private void handlermethod() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, SliderScreen.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);
            }
        }, 2000);
    }
}