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
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordUserNameActivity extends AppCompatActivity {

    EditText Forgotusername;
    TextView UserGetotp;
    ProgressBar progressBar;
    ImageView back_btn;
    String username;
    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_reset_password_user_name);
        UserGetotp = findViewById(R.id.userOTPbtn);
        Forgotusername = findViewById(R.id.forgotusername);
        progressBar = findViewById(R.id.progressbr);
        back_btn = findViewById(R.id.back_btn);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserGetotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = Forgotusername.getText().toString();
                if (username.isEmpty()) {
                    Forgotusername.setError("Please Enter User Name");
                    Forgotusername.requestFocus();
                    return;
                }
                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("user_name", username);
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
        String urlUsername = Global.forgotpasswordurl;
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(ResetPasswordUserNameActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, urlUsername, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);

                    String issuccess = respObj.getString("isSuccess");
                    progressBar.setVisibility(View.GONE);
                    String error = respObj.getString("error");

                    if (issuccess.equals("true")) {
                        Global.customtoast(ResetPasswordUserNameActivity.this, getLayoutInflater(), respObj.getString("error"));
                        startActivity(new Intent(ResetPasswordUserNameActivity.this, VerifyUserNameOTPActivity.class));
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Global.customtoast(ResetPasswordUserNameActivity.this, getLayoutInflater(), respObj.getString("error"));
                        // Show a toast message for wrong username or password
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
                params.put("UserName", username);
                params.put("Mobile", "");
                params.put("FPType", "U");
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