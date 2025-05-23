package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordNumberActivity extends AppCompatActivity {
    MaterialButton GetOTPBtn;
    TextInputEditText ForgotNumber;
    ImageView back_btn;
    String usermobile;
    ProgressBar progressBar;
    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_number);
        context = this;
        ForgotNumber = findViewById(R.id.forgotNumber);
        GetOTPBtn = findViewById(R.id.numberOTPbtn);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //   String mobileno = Global.sharedPreferences.getString("mobile", "");
        // FMobile.setText(mobileno);
        progressBar = findViewById(R.id.progressbr);

        GetOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usermobile = ForgotNumber.getText().toString();
                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("user_mobile", usermobile);
                Global.editor.commit();

                postDataUsingVolley();
            }
        });

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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


                    if (issuccess.equals("true")) {
                        Global.customtoast(ResetPasswordNumberActivity.this, getLayoutInflater(), respObj.getString("error"));
                        startActivity(new Intent(ResetPasswordNumberActivity.this, VerifyNumberOTPActivity.class));
                    } else {
                        progressBar.setVisibility(View.GONE);
                        // Show a toast message for wrong username or password
                        Global.customtoast(ResetPasswordNumberActivity.this, getLayoutInflater(), respObj.getString("error"));
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