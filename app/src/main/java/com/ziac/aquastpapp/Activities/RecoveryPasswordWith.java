package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ziac.aquastpapp.R;

public class RecoveryPasswordWith extends AppCompatActivity {

    CardView Rcemail, Rcusername, Rcmobile;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password_with);
        Rcemail = findViewById(R.id.emailr);
        Rcusername = findViewById(R.id.usernamer);
        Rcmobile = findViewById(R.id.mobiler);

        Rcemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent re = new Intent(RecoveryPasswordWith.this, ResetPasswordEmail.class);
                startActivity(re);
            }
        });

        Rcusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent run = new Intent(RecoveryPasswordWith.this, ResetPasswordUserName.class);
                startActivity(run);
            }
        });

        Rcmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rnum = new Intent(RecoveryPasswordWith.this, ResetPasswordNumber.class);
                startActivity(rnum);
            }
        });


    }
}