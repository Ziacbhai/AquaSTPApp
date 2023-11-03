package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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
    boolean passwordVisible;
    String username ,newpassword;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Newpwd = findViewById(R.id.newpwd);
        Rpwd = findViewById(R.id.rpwd);
        Pwdconfirm = findViewById(R.id.pwdconfirm);


        Newpwd.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Newpwd.getRight() - Newpwd.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Newpwd.getSelectionEnd();
                    if (passwordVisible) {
                        Newpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        Newpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        Newpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        Newpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
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
                    if (passwordVisible) {
                        Rpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        Rpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        Rpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        Rpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    Rpwd.setSelection(selection);
                    return true;
                }
            }
            return false;
        });
        Pwdconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatepassword();
            }
        });
    }

    private void updatepassword() {
        String newpwd,rpassword;

        newpwd = Newpwd.getText().toString();
        rpassword = Rpwd.getText().toString();

        if (newpwd.isEmpty() || rpassword.isEmpty()) {
            Toast.makeText(ChangePasswordActivity.this, "Complete the information and try again !!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Rpwd.length() < 6) {
            Toast.makeText(ChangePasswordActivity.this, "Password  should not be less than 6 digits !!", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(ChangePasswordActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.changetpasswordurl, new Response.Listener<String>() {
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
                            // Toast.makeText(requireActivity(), "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    // Handle JSON parsing exception here
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof NoConnectionError) {
                    Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "No Connection Found");
                } else if (error instanceof ServerError) {
                    Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "Server Error");
                } else if (error instanceof NetworkError) {
                    Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "Network Error");
                } else if (error instanceof ParseError) {
                    Global.customtoast(ChangePasswordActivity.this, getLayoutInflater(), "Parse Error");
                }
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

                //  String user_image = respObj.getString("user_image");

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(2500), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }
}