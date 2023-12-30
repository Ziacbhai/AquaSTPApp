package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText Newpwd ,Rpwd;
    AppCompatButton Pwdconfirm;

    ProgressBar progressBar;
    ImageView Backarrowbtn;
    private boolean passwordvisible = false;
    String username,newpassword,repeatmpassword;

    Context context;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        Newpwd = findViewById(R.id.newpwd);
        Rpwd = findViewById(R.id.rpwd);
        Pwdconfirm = findViewById(R.id.pwdconfirm);
        Backarrowbtn = findViewById(R.id.backarrowbtn);
        progressBar = findViewById(R.id.progressbr);


        Newpwd.setOnTouchListener((v, event) -> {

            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Newpwd.getRight() - Newpwd.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Newpwd.getSelectionEnd();
                    if (passwordvisible) {
                        Newpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        Newpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordvisible = false;

                    } else {
                        Newpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        Newpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordvisible = true;


                    }
                    Newpwd.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        Rpwd.setOnTouchListener((v, event) -> {

            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Rpwd.getRight() - Rpwd.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Rpwd.getSelectionEnd();
                    if (passwordvisible) {
                        Rpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        Rpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordvisible = false;

                    } else {
                        Rpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        Rpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordvisible = true;
                    }
                    Rpwd.setSelection(selection);
                    return true;
                }
            }
            return false;
        });
        Backarrowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Pwdconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newpassword = Newpwd.getText().toString();
                repeatmpassword = Rpwd.getText().toString();
                username = Newpwd.getText().toString();
                repeatmpassword = Rpwd.getText().toString();
                if (newpassword.isEmpty()) {
                    Newpwd.setError("Please enter the New Password");
                    Newpwd.requestFocus();
                    return;
                } else if (repeatmpassword.isEmpty()) {
                    Rpwd.setError("Please enter the New Password");
                    Rpwd.requestFocus();
                    return;
                } else if (newpassword.length() < 6) {
                    Toast.makeText(ChangePasswordActivity.this, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!newpassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,15}$")) {
                    Toast toast = Toast.makeText(ChangePasswordActivity.this, "password must contain mix of upper and lower case letters as well as digits and one special charecter !!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    return;
                }

                if (newpassword.equals(repeatmpassword)) {
                    username = Global.sharedPreferences.getString("username", null);
                    updatepassword();
                } else {
                    Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "Passwords do not match!!");
                }            }
        });

    }

    private void updatepassword() {

        String url = Global.changetpasswordurl;
       progressBar.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue= Volley.newRequestQueue(ChangePasswordActivity.this);


        progressBar.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url , new Response.Listener<String>() {
            @Override
            public void onResponse(String sresponse) {
                JSONObject response = null;
                try {
                    response = new JSONObject(sresponse);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                Log.d("Updated", sresponse);

                try {
                    if (response.getBoolean("isSuccess")) {

                        Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "Password changed successfully !!!");
                        finish();

                    } else {
                        if (response.has("error")) {
                            Toast.makeText(ChangePasswordActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                        } else {
                        }
                    }
                } catch (JSONException e) {
                    // Handle JSON parsing exception here
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("UserName", username);
                params.put("NewPassword",newpassword);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    private boolean isNetworkAvailable() {
        Context context = ChangePasswordActivity.this;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}