package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziac.aquastpapp.R;

import java.util.Date;
import java.util.Locale;

public class FiltersActivity extends AppCompatActivity {

    ImageView backbtn;

    TextView Displaydate,Displaytime;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        backbtn = findViewById(R.id.back_btn);

        Displaydate = findViewById(R.id.displaydate);
        Displaytime = findViewById(R.id.displaytime);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                handler.postDelayed(this, 1000); // Update every 1000 milliseconds (1 second)
            }
        }, 0);
    }

    private void updateDateTime() {
        Date currentDate = new Date();
        // Update date
        SimpleDateFormat dateFormat = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        }
        String formattedDate = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && dateFormat != null) {
            formattedDate = dateFormat.format(currentDate);
        }
        Displaydate.setText(formattedDate);

        SimpleDateFormat timeFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String formattedTime = timeFormat.format(currentDate);
            formattedTime = formattedTime.replace("am", "AM").replace("pm", "PM");

            Displaytime.setText(formattedTime);
        }

    }



}