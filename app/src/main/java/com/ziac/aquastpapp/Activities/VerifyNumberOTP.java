package com.ziac.aquastpapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.google.android.material.textfield.TextInputEditText;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerifyNumberOTP extends AppCompatActivity {
    boolean passwordVisible;
    String otp ,Newpassword;
    TextView Resendotp;
    PinView pinView;
    AppCompatButton Verify;
    ProgressBar progressBar;

    private TextInputEditText Newpwd;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number_otp);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
       // displayMobno();

        pinView=findViewById(R.id.pinview);
        Verify=findViewById(R.id.verifyotp);
        progressBar = findViewById(R.id.progressbr);
        Newpwd = findViewById(R.id.newpassword);

        Resendotp = findViewById(R.id.resendNotp);
        Resendotp.setOnClickListener(v -> startActivity(new Intent(VerifyNumberOTP.this, ResetPasswordNumber.class)));

        Newpwd.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Newpwd.getRight() - Newpwd.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Newpwd.getSelectionEnd();
                    if (passwordVisible) {
                        Newpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        Newpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        Newpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        Newpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    Newpwd.setSelection(selection);
                    return true;
                }
            }
            return false;
        });
        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting the PinView data
                Newpassword = Newpwd.getText().toString();
                otp = pinView.getText().toString();

                if (Newpassword.length() < 6 ){
                    Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(), "Password should not be less than 6 digits !!");
                    return;
                }else {
                   // Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(), "Passwords doesn't match !!");

                }
                //Toast.makeText(OTPActivity.this, otp, Toast.LENGTH_SHORT).show();
                postDataUsingVolley(otp);

            }
        });

    }
    private void postDataUsingVolley(String otp) {
        String url = Global.validateotpurl;
        progressBar.setVisibility(View.VISIBLE);

        // Check for multiple logins linked to the mobile number
        if (hasMultipleLogins()) {
            showAlertDialog("Multiple Logins", "Multiple logins are linked to this mobile number. Please contact support.");
            progressBar.setVisibility(View.GONE);
            return; // Do not proceed with OTP validation
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");

                    Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(), error);
                    progressBar.setVisibility(View.GONE);

                    if (issuccess.equals("true")) {
                        startActivity(new Intent(VerifyNumberOTP.this, LoginSignupActivity.class));
                    } else {
                        // Show an alert dialog for wrong OTP
                        showAlertDialog("Wrong OTP", "The entered OTP is incorrect. Please try again.");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(), e.getMessage());
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                // Global.customtoast(VerifyNumberOTP.this,getLayoutInflater(), error.getMessage());
                // Show an alert dialog for network or server error
                showAlertDialog("Error", "Network or server error. Please try again.");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", otp);
                params.put("Mobile", Global.sharedPreferences.getString("mobile", ""));
                params.put("FPType", "M");
                params.put("NewPassword", Newpassword);

                Log.d("params", params.toString());

                return params;
            }
        };
        queue.add(request);
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // You can perform any action here or leave it empty
                    }
                })
                .show();
    }

    // Helper method to check if there are multiple logins linked to the mobile number
    private boolean hasMultipleLogins() {

        return false;
    }
}