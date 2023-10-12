package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerifyNumberOTP extends AppCompatActivity {

    String otp,mobile;
    TextView Resendotp;
    PinView pinView;
    AppCompatButton Verify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number_otp);

       // displayMobno();
        pinView=findViewById(R.id.pinview);
        Verify=findViewById(R.id.verifyotp);

        //progressBar = findViewById(R.id.progressbr);

        Resendotp = findViewById(R.id.resendotp);
        Resendotp.setOnClickListener(v -> startActivity(new Intent(VerifyNumberOTP.this, ResetPasswordNumber.class)));
        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting the PinView data
                otp = pinView.getText().toString();
                //Toast.makeText(OTPActivity.this, otp, Toast.LENGTH_SHORT).show();
                postDataUsingVolley(otp);

            }
        });

    }
    private void postDataUsingVolley(String otp) {
        String url = Global.validateotpurl;
       //progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue= Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              Toast.makeText(VerifyNumberOTP.this, "Data added to API", Toast.LENGTH_SHORT).show();
                try {

                    JSONObject respObj = new JSONObject(response);
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");
                    Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(),error);

                    if(issuccess.equals("true")){
                        startActivity(new Intent(VerifyNumberOTP.this,ResetPassword.class));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                   // progressBar.setVisibility(View.GONE);
                    Global.customtoast(VerifyNumberOTP.this,getLayoutInflater(), e.getMessage());
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressBar.setVisibility(View.GONE);
               // Global.customtoast(VerifyNumberOTP.this,getLayoutInflater(), error.getMessage());
                if (error instanceof TimeoutError) {
                    Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(),"Request Time-Out");
                } else if (error instanceof ServerError) {
                    Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(),"ServerError");
                }  else if (error instanceof ParseError) {
                    Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(),"Parse Error ");
                }  else if (error instanceof AuthFailureError) {
                    Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(), "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("MyApp", "ServerError: " + error.getMessage());
                    Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(), "ServerError");
                } else if (error instanceof ParseError) {
                    Log.e("MyApp", "ParseError: " + error.getMessage());
                    Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(), "Parse Error");
                } else if (error instanceof AuthFailureError) {
                    Log.e("MyApp", "AuthFailureError: " + error.getMessage());
                    Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(), "AuthFailureError");
                }
                else {
                   // Log.e("MyApp", "Something else: " + error.getMessage());
                     Global.customtoast(VerifyNumberOTP.this, getLayoutInflater(), "Something else");
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("UserName", username);
                params.put("Mobile", mobile);
                params.put("otp", otp);

                return params;
            }
        };
        queue.add(request);
    }

}