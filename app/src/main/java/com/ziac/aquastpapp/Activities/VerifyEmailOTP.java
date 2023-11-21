package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
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

public class VerifyEmailOTP extends AppCompatActivity {
    String username,mobile,otp ,Newpassword;
    TextView Resendotp;
    PinView pinView;
    AppCompatButton EVerify;
    ProgressBar progressBar;
    boolean passwordvisible;
    String email;
    SharedPreferences sharedPreferences;
    private TextInputEditText ENewpwd,Dusrname,Dusrmobile;
    String mobileno;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifiy_email_otp);

        pinView=findViewById(R.id.pinview);
        EVerify=findViewById(R.id.everifyotp);
        progressBar = findViewById(R.id.progressbr);
        ENewpwd = findViewById(R.id.enewpassword);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Resendotp = findViewById(R.id.resendEotp);

        Resendotp.setOnClickListener(v -> startActivity(new Intent(VerifyEmailOTP.this, ResetPasswordEmail.class)));
        ENewpwd.setOnTouchListener((v, event) -> {

            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= ENewpwd.getRight() - ENewpwd.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = ENewpwd.getSelectionEnd();
                    if (passwordvisible) {
                        ENewpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        ENewpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordvisible = false;

                    } else {
                        ENewpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        ENewpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordvisible = true;
                    }
                    ENewpwd.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        EVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting the PinView data
                Newpassword = ENewpwd.getText().toString();
                otp = pinView.getText().toString();

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
              /*  // Global.customtoast(VerifyNumberOTP.this,getLayoutInflater(), error.getMessage());
                if (error instanceof TimeoutError) {
                    Global.customtoast(VerifiyEmailOTP.this, getLayoutInflater(),"Request Time-Out");
                } else if (error instanceof ServerError) {
                    Global.customtoast(VerifiyEmailOTP.this, getLayoutInflater(),"ServerError");
                }  else if (error instanceof ParseError) {
                    Global.customtoast(VerifiyEmailOTP.this, getLayoutInflater(),"Parse Error ");
                }  else if (error instanceof AuthFailureError) {
                    Global.customtoast(VerifiyEmailOTP.this, getLayoutInflater(), "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("MyApp", "ServerError: " + error.getMessage());
                    Global.customtoast(VerifiyEmailOTP.this, getLayoutInflater(), "ServerError");
                } else if (error instanceof ParseError) {
                    Log.e("MyApp", "ParseError: " + error.getMessage());
                    Global.customtoast(VerifiyEmailOTP.this, getLayoutInflater(), "Parse Error");
                } else if (error instanceof AuthFailureError) {
                    Log.e("MyApp", "AuthFailureError: " + error.getMessage());
                    Global.customtoast(VerifiyEmailOTP.this, getLayoutInflater(), "AuthFailureError");
                }
                else {
                    // Log.e("MyApp", "Something else: " + error.getMessage());
                    Global.customtoast(VerifiyEmailOTP.this, getLayoutInflater(), "Something else");
                }*/
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
}