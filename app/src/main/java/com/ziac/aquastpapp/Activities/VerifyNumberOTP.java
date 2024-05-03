package com.ziac.aquastpapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
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
    private boolean passwordvisible = false;
    String otp, Newpassword,show_number;
    PinView pinView;
    TextView NumberVerify,Resendotp,Show_number;
    ProgressBar progressBar;
    private TextInputEditText Newpwd;
    Context context;
    ImageView Repair_back_btn;
    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number_otp);

        context = this;

        pinView = findViewById(R.id.pinview);
        NumberVerify = findViewById(R.id.numberverifyotp);
        progressBar = findViewById(R.id.progressbr);
        Newpwd = findViewById(R.id.newpassword);
        Show_number = findViewById(R.id.shownumber);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        show_number = Global.sharedPreferences.getString("fpmobilenumber", "");
        Show_number.setText(show_number);

        Resendotp = findViewById(R.id.resendNotp);
        Resendotp.setOnClickListener(v -> startActivity(new Intent(VerifyNumberOTP.this, ResetPasswordNumber.class)));

        Newpwd.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Newpwd.getRight() - Newpwd.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Newpwd.getSelectionEnd();
                    if (passwordvisible) {
                        Newpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        Newpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordvisible = false;
                    } else {
                        Newpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        Newpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordvisible = true;
                    }
                    Newpwd.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        Repair_back_btn = findViewById(R.id.repair_back_btn);
        Repair_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        NumberVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Newpassword = Newpwd.getText().toString();
                otp = pinView.getText().toString();

                if (!TextUtils.isEmpty(otp)) {
                    if (!TextUtils.isEmpty(Newpassword)) {
                        postDataUsingVolley(otp);
                    } else {
                        Toast.makeText(VerifyNumberOTP.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyNumberOTP.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }
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
                    progressBar.setVisibility(View.GONE);

                    switch (issuccess) {
                        case "true":
                            startActivity(new Intent(VerifyNumberOTP.this, LoginSignupActivity.class));
                            finish();
                            break;
                        default:
                            if (error.equals("PasswordError")) {
                                showAlertDialog("Wrong Password", "The entered password is incorrect. Please try again.");
                            } else {
                                //showAlertDialog("General Error", "An error occurred. Please try again.");
                                Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(), error);
                            }
                            break;
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
                showAlertDialog("Error", "Network or server error. Please try again.");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", otp);
                params.put("Mobile", Global.sharedPreferences.getString("user_mobile", ""));
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