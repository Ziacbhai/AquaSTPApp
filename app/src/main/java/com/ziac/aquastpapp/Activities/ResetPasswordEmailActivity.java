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

public class ResetPasswordEmailActivity extends AppCompatActivity {

    MaterialButton EnterOTPbtn;
    TextInputEditText Forgotemail;
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
                Intent intent = new Intent(context,RecoveryPasswordWithActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
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
                        Global.customtoast(ResetPasswordEmailActivity.this, getLayoutInflater(), respObj.getString("error"));
                        Intent intent = new Intent(ResetPasswordEmailActivity.this,VerifyEmailOTPActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        // Show a toast message for wrong username or password
                        Global.customtoast(ResetPasswordEmailActivity.this, getLayoutInflater(), respObj.getString("error"));

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