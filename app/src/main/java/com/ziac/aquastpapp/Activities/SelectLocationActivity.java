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

        siteLocationAdapter = new SiteLocationAdapter(Global.StpList, SelectLocationActivity.this);
        siteLocationRecyclerView.setAdapter(siteLocationAdapter);
    }
}