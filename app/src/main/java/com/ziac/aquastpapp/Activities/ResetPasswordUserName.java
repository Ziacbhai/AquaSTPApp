package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ziac.aquastpapp.R;

public class ResetPasswordUserName extends AppCompatActivity {

    AppCompatButton U_OTPbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_user_name);

        U_OTPbtn = findViewById(R.id.u_OTPbtn);

        U_OTPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent unotp = new Intent(ResetPasswordUserName.this, VerifyUserNameOTP.class);
                startActivity(unotp);
            }
        });
    }
}