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
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
    boolean passwordVisible;
    AppCompatButton UVerify;
    ProgressBar progressBar;
    Context context;

    private TextInputEditText UNewpwd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user_name_otp);

        mPinView = findViewById(R.id.pinview);
        UVerify = findViewById(R.id.uverifyotp);

        progressBar = findViewById(R.id.progressbr);
        UNewpwd = findViewById(R.id.usernewpassword);
        Resendotp = findViewById(R.id.resendUotp);
        Resendotp.setOnClickListener(v -> startActivity(new Intent(VerifyUserNameOTP.this, ResetPasswordUserName.class)));
        UNewpwd.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= UNewpwd.getRight() - UNewpwd.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = UNewpwd.getSelectionEnd();
                    if (passwordVisible) {
                        UNewpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        UNewpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        UNewpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        UNewpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    UNewpwd.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        UVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting the PinView data
                Newpassword = UNewpwd.getText().toString();
                // username = UNewpwd.getText().toString();
                otp = mPinView.getText().toString();

              /*  if (Newpassword.length() < 6 ){
                    Global.customtoast(VerifiyEmailOTP.this, getLayoutInflater(), "Password should not be less than 6 digits !!");
                    return;
                }else {
                    // Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(), "Passwords doesn't match !!");

                }*/
                //Toast.makeText(OTPActivity.this, otp, Toast.LENGTH_SHORT).show();
                UserDataUsingVolley(otp);

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
                // Toast.makeText(VerifyUserNameOTP.this, "Data added to API", Toast.LENGTH_SHORT).show();
                try {

                    JSONObject respObj = new JSONObject(response);
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");
//                    Global.editor = sharedPreferences.edit();
//                    Global.editor.putString("username", username);
//                    Global.editor.putString("mobile", mobileno);
//                    Global.editor.commit();
                    Global.customtoast(VerifyUserNameOTP.this, getLayoutInflater(), error);
                    progressBar.setVisibility(View.GONE);
                    if (issuccess.equals("true")) {
                        startActivity(new Intent(VerifyUserNameOTP.this, LoginSignupActivity.class));

                    } else {
                        showAlertDialog("Wrong OTP", "The entered OTP is incorrect. Please try again.");

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