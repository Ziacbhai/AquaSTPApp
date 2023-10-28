package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordNumber extends AppCompatActivity {
    AppCompatButton GetOTPBtn;
    EditText FMobile;
    String mobile;
    //SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_number);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        FMobile = findViewById(R.id.fmobile);
        GetOTPBtn = findViewById(R.id.n_OTPbtn);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mobileno = Global.sharedPreferences.getString("mobile", "");

        FMobile.setText(mobileno);
        progressBar = findViewById(R.id.progressbr);

        GetOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = FMobile.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
//                 if (mobile.isEmpty()) {
//                     ForgotPass.setError("Please Enter Password");
//                     ForgotPass.requestFocus();
//                    return;
//                } else if(mobile.length() < 10 ){
//                    Global.customtoast(ResetPasswordNumber.this, getLayoutInflater(), "Mobile number should not be less than 10 digits !!");
//                    return;}
                postDataUsingVolley();
            }
        });
    }
    private void postDataUsingVolley() {
        String urlnumber = Global.forgotpasswordurl;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, urlnumber, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);

                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");

                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("mobile", mobile);
                    Global.editor.commit();


                    if (issuccess.equals("true")) {
                        Global.customtoast(ResetPasswordNumber.this, getLayoutInflater(),"OTP send successfully ");
                        startActivity(new Intent(ResetPasswordNumber.this, VerifyNumberOTP.class));
                    } else {
                        // Show a toast message for wrong username or password
                        Global.customtoast(ResetPasswordNumber.this, getLayoutInflater(),"Provided number is invalid ");
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    // Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("UserName", username);
                params.put("Mobile", mobile);
                params.put("FPType", "M");
                params.put("user_email", "ziacbhai1993@gmail.com");
                return params;
            }
        };
        queue.add(request);
    }
}