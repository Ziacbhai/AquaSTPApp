package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class ResetPasswordEmail extends AppCompatActivity {

    AppCompatButton E_OTPbtn;
    EditText Femail;

    String email;
    //SharedPreferences sharedPreferences;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_email);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        E_OTPbtn = findViewById(R.id.eotpbtn);
        Femail = findViewById(R.id.Femail);
        progressBar = findViewById(R.id.progressbr);

       Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //String emailId = Global.sharedPreferences.getString("email", "");


       // Femail.setText(emailId);

        E_OTPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = Femail.getText().toString();
               // progressBar.setVisibility(View.VISIBLE);
                if (email.isEmpty()) {
                    Femail.setError("Please Enter Email");
                    Femail.requestFocus();
                    return;
                } else {

                }
                postDataUsingVolley();

            }
        });
    }

    private void postDataUsingVolley() {

        String urlemail = Global.forgotpasswordurl;
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue= Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, urlemail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respObj = new JSONObject(response);

                    String issuccess = respObj.getString("isSuccess");
                    progressBar.setVisibility(View.GONE);
                    String error = respObj.getString("error");

                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("user_email", email);
                    //Global.editor.remove("Email").commit();

                    Global.editor.commit();

                    if (issuccess.equals("true")) {
                        Global.customtoast(ResetPasswordEmail.this, getLayoutInflater(),respObj.getString("error"));
                        startActivity(new Intent(ResetPasswordEmail.this, VerifyEmailOTP.class));
                    } else {
                        progressBar.setVisibility(View.GONE);
                        // Show a toast message for wrong username or password
                        Global.customtoast(ResetPasswordEmail.this, getLayoutInflater(),respObj.getString("error"));

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
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", "");
               // params.put("Email", email);
                params.put("Mobile", "");
                params.put("FPType", "E");
                params.put("user_email", email);
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