package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziac.aquastpapp.R;

public class GenerateSTPdetails extends AppCompatActivity {

    AppCompatButton Back_Login;
    ImageView Back_btn;

    TextView Company_name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_stpdetails);

        Back_Login = findViewById(R.id.back_Login_btn);

        Back_btn = findViewById(R.id.back_btn);

        Company_name = findViewById(R.id.company);

        String com_name  = Global.sharedPreferences.getString("com_name", "");

        Company_name.setText(com_name);
        Back_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    @Override
    public void onBackPressed() {

    }
}