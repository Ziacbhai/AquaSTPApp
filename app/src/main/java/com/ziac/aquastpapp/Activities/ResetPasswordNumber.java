package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ziac.aquastpapp.R;

public class ResetPasswordNumber extends AppCompatActivity {
    AppCompatButton N_OTPbtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_number);

        N_OTPbtn = findViewById(R.id.n_OTPbtn);

        N_OTPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notp = new Intent(ResetPasswordNumber.this,VerifyNumberOTP.class);
                startActivity(notp);
            }
        });
    }
}