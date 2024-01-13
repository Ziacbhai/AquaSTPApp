package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziac.aquastpapp.R;

public class RecoveryPasswordWith extends AppCompatActivity {

    CardView Rcemail, Rcusername, Rcmobile;

    ImageView back_btn;

    Context context;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password_with);
        Rcemail = findViewById(R.id.useremail);
        Rcusername = findViewById(R.id.usernamer);
        Rcmobile = findViewById(R.id.mobiler);


        back_btn = findViewById(R.id.repair_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Rcemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecoveryPasswordWith.this,ResetPasswordEmail.class));
                finish();
            }
        });

        Rcusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecoveryPasswordWith.this,ResetPasswordUserName.class));
                finish();

            }
        });

        Rcmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecoveryPasswordWith.this,ResetPasswordNumber.class));
                finish();

            }
        });


    }
}