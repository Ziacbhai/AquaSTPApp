package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

import Adapters.SiteLocationAdapter;
import Models.StpModelClass;

public class SelectLocationActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce;
    String username;
    SiteLocationAdapter siteLocationAdapter;
    RecyclerView siteLocationRecyclerView;
    StpModelClass stpModelClass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        siteLocationRecyclerView = findViewById(R.id.stp_recyclerview);
        //siteLocationRecyclerView.setAdapter(siteLocationAdapter);
        siteLocationRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL , false));


        getuserprofile();

    }

    private void getuserprofile() {

        String url = Global.getuserprofileurl;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respObj1 = new JSONObject(response);

                    ////////////////////data2

                    JSONArray liststp = new JSONArray(respObj1.getString("data2"));
                    int i;
                    Global.StpList = new ArrayList<>();
                    for (i = 0;i<liststp.length();i++){
                        final JSONObject e;
                        try {
                            e = liststp.getJSONObject(i);
                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }

                        //listofstp = new JSONObject(liststp[i]("site_code"));
                        stpModelClass = new StpModelClass();

                        stpModelClass.setSucode(e.getString("su_code"));
                        stpModelClass.setComcode(e.getString("com_code"));
                        stpModelClass.setUsercode(e.getString("user_code"));
                        stpModelClass.setPersonname(e.getString("person_name"));
                        stpModelClass.setUsername(e.getString("username"));
                        stpModelClass.setSstp1code(e.getString("sstp1_code"));
                        stpModelClass.setStpname(e.getString("stp_name"));
                        stpModelClass.setSitecode(e.getString("site_code"));
                        stpModelClass.setSitename(e.getString("site_name"));
                        stpModelClass.setStpactive(e.getString("stp_active"));
                        Global.StpList.add(stpModelClass);

                    }
                    siteLocationAdapter = new SiteLocationAdapter(Global.StpList, SelectLocationActivity.this);
                    siteLocationRecyclerView.setAdapter(siteLocationAdapter);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", Global.sharedPreferences.getString("username",null));
                params.put("su_code", Global.sharedPreferences.getString("sucode", "0"));
                params.put("site_name", Global.sharedPreferences.getString("sitename", "0"));
                params.put("person_name", Global.sharedPreferences.getString("personname", "0"));


                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                8000, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        queue.add(request);
    }

}