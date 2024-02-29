package com.ziac.aquastpapp.Activities;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

public class VerifyUserNameOTP extends AppCompatActivity {
    String otp, Newpassword;
    TextView Resendotp;
    PinView mPinView;
    private boolean passwordvisible;
    TextView UserVerify;
    ProgressBar progressBar;
    Context context;
    ImageView Repair_back_btn;
    private TextInputEditText UNewpwd;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user_name_otp);
        context = this;
        mPinView = findViewById(R.id.pinview);
        UserVerify = findViewById(R.id.userverifyotp);


        progressBar = findViewById(R.id.progressbr);
        UNewpwd = findViewById(R.id.usernewpassword);
        Resendotp = findViewById(R.id.resendUotp);
        Resendotp.setOnClickListener(v -> startActivity(new Intent(VerifyUserNameOTP.this, ResetPasswordUserName.class)));
        UNewpwd.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= UNewpwd.getRight() - UNewpwd.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = UNewpwd.getSelectionEnd();
                    if (passwordvisible) {
                        UNewpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        UNewpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordvisible = false;
                    } else {
                        UNewpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        UNewpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordvisible = true;
                    }
                    UNewpwd.setSelection(selection);
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
        UserVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting the PinView data
                Newpassword = UNewpwd.getText().toString();
                otp = mPinView.getText().toString();
                if (!TextUtils.isEmpty(otp)) {
                    // Check if other required fields are not empty
                    if (!TextUtils.isEmpty(Newpassword)) {
                        UserDataUsingVolley(otp);
                    } else {
                        Toast.makeText(VerifyUserNameOTP.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyUserNameOTP.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void UserDataUsingVolley(String otp) {

        String Uurl = Global.validateotpurl;
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, Uurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");
                    progressBar.setVisibility(View.GONE);

                    switch (issuccess) {
                        case "true":
                            startActivity(new Intent(VerifyUserNameOTP.this, LoginSignupActivity.class));
                            break;
                        default:
                            if (error.equals("PasswordError")) {
                                showAlertDialog("Wrong Password", "The entered password is incorrect. Please try again.");
                            } else {
                                //showAlertDialog("General Error", "An error occurred. Please try again.");
                                Global.customtoast(VerifyUserNameOTP.this, getLayoutInflater(), error);
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Global.customtoast(VerifyUserNameOTP.this, getLayoutInflater(), e.getMessage());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", otp);
                params.put("FPType", "U");
                params.put("NewPassword", Newpassword);
                params.put("username", Global.sharedPreferences.getString("username", ""));
                Log.d("params", params.toString());

                // params.put("NewPassword", "Siva126@Ziac");
                // params.put("UserName", username);
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
}