package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Fragments.LoginFragment;
import Models.StpModelClass;

public class SplashScreenActivity extends AppCompatActivity {
    TextView textView;
    ImageView imageView;
    Context context;
    StpModelClass stpModelClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = this;
        textView = findViewById(R.id.txt);
        imageView = findViewById(R.id.imageView);
        Animation myanimation = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.animation);
        textView.startAnimation(myanimation);
        Animation animation = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.animationlogo);
        imageView.startAnimation(animation);
       // handlermethod();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    // Check if access_token and refresh_token exist in shared preferences
                    if (Global.sharedPreferences != null &&
                            Global.sharedPreferences.contains("access_token") &&
                            Global.sharedPreferences.contains("refresh_token")) {
                        dorefreshtokenVolley();
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, SliderScreenActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }

                } catch (Exception ex) {
                    // Handle the exception appropriately (e.g., log the error)
                    ex.printStackTrace();
                }
            }
        }, 2000); // Delay of 2000 milliseconds (2 seconds)


    }


    private void dorefreshtokenVolley() {
        String url = Global.tokenurl;
        RequestQueue queue = Volley.newRequestQueue(SplashScreenActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    String access_token = respObj.getString("access_token");

                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("access_token", access_token);
                    Global.editor.commit();

                    if (isLoggedIn()) {
                        if (access_token != null && !access_token.isEmpty()) {
                            getuserprofile();
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, SliderScreenActivity.class));
                        }
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, SliderScreenActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "No Connection Found", Toast.LENGTH_LONG).show();
                } /*else if (error instanceof ServerError) {
                    String errorResponse = new String(error.networkResponse.data);
                    try {
                        JSONObject errorJson = new JSONObject(errorResponse);
                        String errorDescription = errorJson.optString("error_description", "");
                        Global.customtoast(context, getLayoutInflater(), errorDescription);
                    } catch (JSONException e) {
                        Global.customtoast(context, getLayoutInflater(), "An error occurred. Please try again later.");
                    }
                } */else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error", Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(SplashScreenActivity.this, LoginSignupActivity.class));
                finish();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("refresh_token", Global.sharedPreferences.getString("refresh_token", ""));
                params.put("grant_type", "refresh_token");
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        queue.add(request);
    }

    private void getuserprofile() {

        String url = Global.getuserprofileurl;
        RequestQueue queue = Volley.newRequestQueue(this);
        // progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
           // progressDialog.dismiss();

            try {
                JSONObject respObj1 = new JSONObject(response);
                boolean isSuccess = respObj1.getBoolean("isSuccess");
                if (isSuccess) {
                    JSONObject respObj = new JSONObject(respObj1.getString("data"));
                    String ayear = respObj1.getString("ayear");
                    String finstdate = respObj1.getString("fin_stdate");
                    String fineddate = respObj1.getString("fin_eddate");
                    String user_code = respObj.getString("user_code");
                    String person_name = respObj.getString("person_name");
                    String com_code = respObj.getString("com_code");
                    String user_image = respObj.getString("user_image");
                    String user_type = respObj.getString("user_type");
                    String user_mobile = respObj.getString("user_mobile");
                    String user_email = respObj.getString("user_email");
                    String ref_code = respObj.getString("ref_code");
                    String com_name = respObj.getString("com_name");
                    // String stp_capacity = respObj.getString("stp_capacity");


                    Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("user_code", user_code);
                    Global.editor.putString("person_nameu", person_name);
                    Global.editor.putString("com_code", com_code);
                    Global.editor.putString("user_image", user_image);
                    Global.editor.putString("user_type", user_type);
                    Global.editor.putString("user_mobile", user_mobile);
                    Global.editor.putString("user_email", user_email);
                    Global.editor.putString("ref_code", ref_code);
                    Global.editor.putString("user_image", user_image);
                    Global.editor.putString("ayear", ayear);
                    Global.editor.putString("fin_stdate", finstdate);
                    Global.editor.putString("fin_eddate", fineddate);
                    Global.editor.putString("com_name", com_name);
                    // Global.editor.putString("stp_capacity", stp_capacity);
                    // preferences.edit().remove("text").commit();
                    Global.editor.commit();

                    JSONArray liststp = new JSONArray(respObj1.getString("data2"));


                    try {
                        Global.StpList = new ArrayList<>();

                        for (int i = 0; i < liststp.length(); i++) {
                            final JSONObject e;
                            try {
                                e = liststp.getJSONObject(i);
                            } catch (JSONException ex) {
                                throw new RuntimeException(ex);
                            }

                            stpModelClass = new StpModelClass();
                            stpModelClass.setSucode(e.getInt("su_code"));
                            stpModelClass.setComcode(e.getString("com_code"));
                            stpModelClass.setUsercode(e.getString("user_code"));
                            stpModelClass.setPersonname(e.getString("person_name"));
                            stpModelClass.setUsername(e.getString("username"));
                            stpModelClass.setSstp1code(e.getString("sstp1_code"));
                            stpModelClass.setStpname(e.getString("stp_name"));
                            stpModelClass.setSitecode(e.getString("site_code"));
                            stpModelClass.setSitename(e.getString("site_name"));
                            stpModelClass.setStpactive(e.getString("stp_active"));
                            stpModelClass.setSite_address(e.getString("site_address"));
                            stpModelClass.setProcess__type(e.getString("process_name"));
                            stpModelClass.setStp_capacity(e.getString("stp_capacity"));
                            Global.StpList.add(stpModelClass);
                        }

                       // progressDialog.dismiss();

                        /*if (Global.StpList.isEmpty()) {
                            startActivity(new Intent(getActivity(), GenerateSTPdetails.class));
                        } else {*/
                        Intent welcomeIntent = null;

                        if ("O".equals(user_type)) {
                            welcomeIntent = new Intent(this, WelcomeOwnerActivity.class);
                        } else if ("C".equals(user_type)) {
                            welcomeIntent = new Intent(this, WelcomeCustomerActivity.class);
                        } else if ("S".equals(user_type)) {
                            welcomeIntent = new Intent(this, WelcomeSupervisorActivity.class);
                        } else if ("M".equals(user_type)) {
                            welcomeIntent = new Intent(this, WelcomeManagerActivity.class);
                        } else if ("U".equals(user_type)) {
                            welcomeIntent = new Intent(this, WelcomeUserActivity.class);
                        }

                        if (welcomeIntent != null) {
                            welcomeIntent.setType(Settings.ACTION_SYNC_SETTINGS);
                            this.startActivity(welcomeIntent);

                        } else {
                            //startActivity(new Intent(getActivity(), SelectSTPLocationActivity.class));
                            Toast.makeText(context, "Unable to get User Details !!", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //progressDialog.dismiss();
                    Toast.makeText(context, respObj1.getString("error"), Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", Global.sharedPreferences.getString("user_name", ""));
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


/*
    private void handlermethod() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, SliderScreenActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);
            }
        }, 2000);
    }
*/

    private boolean isLoggedIn() {
        return !Global.sharedPreferences.getString("access_token", "").isEmpty();
    }

}