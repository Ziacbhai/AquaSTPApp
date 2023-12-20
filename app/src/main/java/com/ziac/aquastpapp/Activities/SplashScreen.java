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

import com.ziac.aquastpapp.R;

public class SplashScreen extends AppCompatActivity {
    TextView textView;
    ImageView imageView;

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
        } ,3000);
    }


///Login Code Direct
/*    private void handlermethod() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (isUserLoggedIn(getApplicationContext())) {
                startMainActivity();
            } else {
                startActivity(new Intent(getApplicationContext(), SliderScreen.class));
                finish();
            }
        }, 3000);
    }

    private boolean isUserLoggedIn(Context context) {
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String username = Global.sharedPreferences.getString("username", "");
        String password = Global.sharedPreferences.getString("password", "");
        String accessToken = Global.sharedPreferences.getString("access_token", "");

        return username != null && !username.isEmpty() &&
                password != null && !password.isEmpty() &&
                accessToken != null && !accessToken.isEmpty();
    }

    private void startMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }*/
}