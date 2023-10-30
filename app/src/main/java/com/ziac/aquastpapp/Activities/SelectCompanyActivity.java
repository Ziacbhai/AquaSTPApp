package com.ziac.aquastpapp.Activities;

import static com.google.android.material.internal.ContextUtils.getActivity;
import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Adapters.LocationsAdapter;

public class SelectCompanyActivity extends AppCompatActivity {

    RecyclerView CompanyrecyclerView;

    LocationsAdapter locationsAdapter;

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company);
        CompanyrecyclerView = findViewById(R.id.stp_recyclerview);

     //   getlatestcompanies();
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }



}