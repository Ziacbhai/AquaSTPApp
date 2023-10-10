package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ziac.aquastpapp.R;

public class ResetPasswordEmail extends AppCompatActivity {

    AppCompatButton E_OTPbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_email);

        E_OTPbtn = findViewById(R.id.eotpbtn);

        E_OTPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eotp = new Intent(ResetPasswordEmail.this,VerifiyEmailOTP.class);
                startActivity(eotp);
            }
        });
    }
}