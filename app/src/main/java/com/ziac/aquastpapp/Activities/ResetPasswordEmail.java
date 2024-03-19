package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordEmail extends AppCompatActivity {

    TextView EnterOTPbtn;
    EditText Forgotemail;
    String useremail;
    ImageView back_btn;
    ProgressBar progressBar;
    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_email);
        context  = this;
        EnterOTPbtn = findViewById(R.id.emailgetotpbtn);
        Forgotemail = findViewById(R.id.resetemail);
        progressBar = findViewById(R.id.progressbr);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        EnterOTPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useremail = Forgotemail.getText().toString();
                // progressBar.setVisibility(View.VISIBLE);
                if (useremail.isEmpty()) {
                    Forgotemail.setError("Please Enter Email");
                    Forgotemail.requestFocus();
                    return;
                }

                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("user_email", useremail);
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

        String urlemail = Global.forgotpasswordurl;
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, urlemail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respObj = new JSONObject(response);

                    String issuccess = respObj.getString("isSuccess");
                    progressBar.setVisibility(View.GONE);
                    String error = respObj.getString("error");


                    if (issuccess.equals("true")) {
                        Global.customtoast(ResetPasswordEmail.this, getLayoutInflater(), respObj.getString("error"));
                        startActivity(new Intent(ResetPasswordEmail.this, VerifyEmailOTP.class));
                    } else {
                        progressBar.setVisibility(View.GONE);
                        // Show a toast message for wrong username or password
                        Global.customtoast(ResetPasswordEmail.this, getLayoutInflater(), respObj.getString("error"));

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
                params.put("Mobile", "");
                params.put("FPType", "E");
                params.put("user_email", useremail);
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