package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

public class VerifyEmailOTP extends AppCompatActivity {
    String otp ,Newpassword;
    TextView Resendotp;
    PinView Enter_pinnumber;
    AppCompatButton EVerify;
    ProgressBar progressBar;
    boolean passwordVisible;
    private TextInputEditText EnterNewpwd;

    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifiy_email_otp);
        Enter_pinnumber=findViewById(R.id.et_pinview);
        new OTP_Receiver().setPinView(Enter_pinnumber);
        requestSMSPermission();
        EVerify=findViewById(R.id.everifyotp);
        progressBar = findViewById(R.id.progressbr);
        EnterNewpwd = findViewById(R.id.enewpassword);
        Resendotp = findViewById(R.id.resendEotp);

        Resendotp.setOnClickListener(v -> startActivity(new Intent(VerifyEmailOTP.this, ResetPasswordEmail.class)));
        EnterNewpwd.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= EnterNewpwd.getRight() - EnterNewpwd.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = EnterNewpwd.getSelectionEnd();
                    if (passwordVisible) {
                        EnterNewpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        EnterNewpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        EnterNewpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        EnterNewpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    EnterNewpwd.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        EVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting the PinView data
                Newpassword = EnterNewpwd.getText().toString();
                otp = Enter_pinnumber.getText().toString();

              /*  if (Newpassword.length() < 6 ){
                    Global.customtoast(VerifiyEmailOTP.this, getLayoutInflater(), "Password should not be less than 6 digits !!");
                    return;
                }else {
                    // Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(), "Passwords doesn't match !!");

                }*/
                //Toast.makeText(OTPActivity.this, otp, Toast.LENGTH_SHORT).show();
                EmailDataUsingVolley(otp);

            }
        });
    }


    private void EmailDataUsingVolley(String otp) {

        String Eurl = Global.validateotpurl;
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue= Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, Eurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(VerifyEmailOTP.this, "Data added to API", Toast.LENGTH_SHORT).show();
                try {

                    JSONObject respObj = new JSONObject(response);
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");
//                    Global.editor = sharedPreferences.edit();
//                    Global.editor.putString("username", username);
//                    Global.editor.putString("mobile", mobileno);
//                    Global.editor.commit();
                    Global.customtoast(VerifyEmailOTP.this, getLayoutInflater(),error);
                    progressBar.setVisibility(View.GONE);
                    if(issuccess.equals("true")){
                        startActivity(new Intent(VerifyEmailOTP.this, LoginSignupActivity.class));

                    }else {
                        showAlertDialog("Wrong OTP", "The entered OTP is incorrect. Please try again.");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Global.customtoast(VerifyEmailOTP.this,getLayoutInflater(), e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", otp);
                params.put("Mobile","");
                params.put("FPType", "E");
                params.put("UserName", "");
                params.put("NewPassword", Newpassword);
                params.put("user_email", Global.sharedPreferences.getString("user_email", ""));
                Log.d("params", params.toString());

                // params.put("NewPassword", "Siva126@Ziac");
                // params.put("UserName", username);


                return params;
            }
        };
        queue.add(request);
    }
    private void requestSMSPermission() {
        if (ContextCompat.checkSelfPermission(VerifyEmailOTP.this,Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(VerifyEmailOTP.this,new String[]{
                    Manifest.permission.RECEIVE_SMS
            },100);
        }

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