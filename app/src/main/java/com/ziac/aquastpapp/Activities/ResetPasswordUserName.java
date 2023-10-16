package com.ziac.aquastpapp.Activities;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class ResetPasswordUserName extends AppCompatActivity {

   // EditText Username ;
    EditText etusername;
    AppCompatButton Getotp1;
    ProgressBar progressBar;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_user_name);

        Getotp1 = findViewById(R.id.submitotp);
        etusername = findViewById(R.id.fgusername);
        progressBar = findViewById(R.id.progressbr);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String usernames = Global.sharedPreferences.getString("username", "");

        etusername.setText(usernames);

        Getotp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etusername.getText().toString();
               if (username.isEmpty()) {
                   etusername.setError("Please Enter User Name");
                   etusername.requestFocus();
                    return;
                }
                postDataUsingVolley();
            }
        });
    }

    private void postDataUsingVolley() {

        String urlUsername = Global.forgotpasswordurl;
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue= Volley.newRequestQueue(ResetPasswordUserName.this);
        StringRequest request = new StringRequest(Request.Method.POST, urlUsername, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);

                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");

                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("username", username);
                    Global.editor.commit();

                    if (issuccess.equals("true")) {
                        startActivity(new Intent(ResetPasswordUserName.this, VerifyUserNameOTP.class));
                    } else {
                        // Show a toast message for wrong username or password
                        Global.customtoast(ResetPasswordUserName.this, getLayoutInflater(),"Provided username is invalid ");
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
                params.put("UserName", username);
                params.put("Mobile", "mobile");
                params.put("FPType", "U");
                params.put("user_email", "ziacbhai1993@gmail.com");
                return params;
            }
        };
        queue.add(request);

    }
}