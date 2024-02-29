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

    CardView Resetemail, Resetusername, Resetmobile;
    ImageView back_btn;
    Context context;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password_with);

        context = this;
        Resetemail = findViewById(R.id.useremail);
        Resetusername = findViewById(R.id.usernamer);
        Resetmobile = findViewById(R.id.mobiler);
        back_btn = findViewById(R.id.repair_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Resetemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecoveryPasswordWith.this,ResetPasswordEmail.class));
                finish();
            }
        });

        Resetusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecoveryPasswordWith.this,ResetPasswordUserName.class));
                finish();

            }
        });

        Resetmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecoveryPasswordWith.this,ResetPasswordNumber.class));
                finish();

            }
        });


    }
}