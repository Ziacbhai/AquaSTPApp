package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ziac.aquastpapp.R;

public class RecoveryPasswordWith extends AppCompatActivity {

    CardView Remail,Rusername,Rmobile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password_with);

        Remail = findViewById(R.id.remail);
        Rusername = findViewById(R.id.rusername);
        Rmobile = findViewById(R.id.rmobile);


       Remail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent re = new Intent(RecoveryPasswordWith.this,ResetPasswordEmail.class);
               startActivity(re);
           }
       });

        Rusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent run = new Intent(RecoveryPasswordWith.this, ResetPasswordUserName.class);
                startActivity(run);
            }
        });

        Rmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rnum = new Intent(RecoveryPasswordWith.this, ResetPasswordNumber.class);
                startActivity(rnum);
            }
        });


    }
}