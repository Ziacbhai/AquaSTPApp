package com.ziac.aquastpapp.Activities;


import static okhttp3.internal.Util.filterList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.SiteLocationAdapter;
import Models.StpModelClass;

public class SelectLocationActivity extends AppCompatActivity {

    LinearLayout Scr;
    SiteLocationAdapter siteLocationAdapter;
    RecyclerView siteLocationRecyclerView;
    private SearchView searchView;
    StpModelClass stpModelClassList;
    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        context =this;
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        stpModelClassList = new StpModelClass();
        searchView = findViewById(R.id.searchView);
        Scr = findViewById(R.id.search);
        searchView.clearFocus();

        Scr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
                searchView.requestFocus();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });

        siteLocationRecyclerView = findViewById(R.id.stp_recyclerview);
        siteLocationRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL , false));
        siteLocationAdapter = new SiteLocationAdapter(Global.StpList, SelectLocationActivity.this);
        siteLocationRecyclerView.setAdapter(siteLocationAdapter);
    }

    private void performSearch(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Global.getSearchSiteOrSTPByName;
        url=url+"name="+query + "&com_code=" + Global.sharedPreferences.getString("com_code", "");
        StringRequest jsonObjectrequest = new StringRequest(Request.Method.POST,url,response -> {
                    JSONObject jobj;
                    try {
                        jobj = new JSONObject(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    JSONArray jarrrecent;
                    try {
                        jarrrecent = jobj.getJSONArray("data2");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Global.StpList = new ArrayList<>();
                    for (int i = 0; i < jarrrecent.length(); i++) {
                        final JSONObject e;
                        try {
                            e = jarrrecent.getJSONObject(i);
                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }

                        stpModelClassList =  new StpModelClass();
                        try {
                            stpModelClassList.setStpname(e.getString("stp_name"));
                            stpModelClassList.setSitename(e.getString("site_name"));
                            stpModelClassList.setSitecode(e.getString("site_code"));
                            stpModelClassList.setComcode(e.getString("com_code"));
                            stpModelClassList.setUsercode(e.getString("user_code"));
                            stpModelClassList.setSstp1code(e.getString("sstp1_code"));
                            stpModelClassList.setSucode(e.getInt("su_code"));

                          /*  String su_code = e.getString("su_code");
                            if (su_code.startsWith("0")) {
                                su_code = su_code.substring(1);
                            }
                            stpModelClassList.setSucode(Integer.parseInt(su_code));*/

                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        Global.StpList.add(stpModelClassList);
                    }
                    siteLocationAdapter = new SiteLocationAdapter(Global.StpList,this);
                    siteLocationRecyclerView.setAdapter(siteLocationAdapter);
                }, error -> {

            if (error instanceof TimeoutError) {

                Global.customtoast(this, getLayoutInflater(),"Request Time-Out !!");
            } else if (error instanceof NoConnectionError) {
                Global.customtoast(this, getLayoutInflater(),"No Connection !!");
            } else if (error instanceof ServerError) {
                Global.customtoast(this, getLayoutInflater(),"Server Error ");
            } else if (error instanceof NetworkError) {
                Global.customtoast(this, getLayoutInflater(),"Network Error ");
            } else if (error instanceof ParseError) {
                Global.customtoast(this, getLayoutInflater(),"Parse Error ");
            }

            // Global.customtoast(getContext(), getLayoutInflater(),"Failed to get latest vehicle .." + error.getMessage());
        })
        {

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                Log.d("token", accesstoken);
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("name",query);
                Log.d("params", params.toString());

                return params;
            }
        };
        queue.add(jsonObjectrequest);
    }
}