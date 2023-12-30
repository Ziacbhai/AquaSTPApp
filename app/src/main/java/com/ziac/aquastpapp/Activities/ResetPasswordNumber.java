package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
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
    EditText ForgotMobile;
    String usermobile;
    ProgressBar progressBar;
    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_number);

        ForgotMobile = findViewById(R.id.fmobile);
        GetOTPBtn = findViewById(R.id.n_OTPbtn);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //   String mobileno = Global.sharedPreferences.getString("mobile", "");

        // FMobile.setText(mobileno);
        progressBar = findViewById(R.id.progressbr);

        GetOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usermobile = ForgotMobile.getText().toString();
                // progressBar.setVisibility(View.VISIBLE);
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
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, urlnumber, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);

                    String issuccess = respObj.getString("isSuccess");
                    progressBar.setVisibility(View.GONE);
                    String error = respObj.getString("error");

                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("mobile", usermobile);
                    Global.editor.commit();

                    if (issuccess.equals("true")) {
                        Global.customtoast(ResetPasswordNumber.this, getLayoutInflater(), respObj.getString("error"));
                        startActivity(new Intent(ResetPasswordNumber.this, VerifyNumberOTP.class));
                    } else {
                        progressBar.setVisibility(View.GONE);
                        // Show a toast message for wrong username or password
                        Global.customtoast(ResetPasswordNumber.this, getLayoutInflater(), respObj.getString("error"));
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
                params.put("UserName", "");
                params.put("Mobile", usermobile);
                params.put("FPType", "M");
                params.put("user_email", "");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        queue.add(request);
    }
}