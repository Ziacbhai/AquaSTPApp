package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.Lab_Test_Details_Adapter;
import Models.IncidentsClass;

public class Incident_Details_Activity extends AppCompatActivity {
    RecyclerView Incident_details_Rv;
    IncidentsClass incidentsClass;
    Context context;
    private ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_details_design);
        context = this;


        user_topcard();
        if (!Global.isNetworkAvailable(this)) {
            Global.customtoast(this, getLayoutInflater(), "Internet connection lost !!");
        }
        new InternetCheckTask().execute();
        Incident_details_Rv = findViewById(R.id.Incident_details_Recyclerview);
        Incident_details_Rv.setLayoutManager(new LinearLayoutManager(this));
        Incident_details_Rv.setHasFixedSize(true);
        Incident_details_Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getIncidentDetails();
    }

    private void user_topcard() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String personname,useremail,stpname,sitename,siteaddress,processname,usermobile;
        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        siteaddress = sharedPreferences.getString("site_address", "");
        processname = sharedPreferences.getString("process_name", "");
        useremail = sharedPreferences.getString("user_email", "");
        usermobile = sharedPreferences.getString("user_mobile", "");
        personname = sharedPreferences.getString("person_name", "");

        TextView txtsitename,txtstpname,txtsiteaddress,txtuseremail,txtusermobile,txtpersonname;

        txtsitename = findViewById(R.id.sitename);
        txtstpname = findViewById(R.id.stpname);
        txtsiteaddress = findViewById(R.id.siteaddress);
        txtuseremail = findViewById(R.id.useremail);
        txtusermobile = findViewById(R.id.usermobile);
        txtpersonname = findViewById(R.id.personname);

        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " / " + processname);
        txtsiteaddress.setText(siteaddress);
        txtuseremail.setText(useremail);
        txtusermobile.setText(usermobile);
        txtpersonname.setText(personname);
    }

    private void getIncidentDetails() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String Incident_D_detail = Global.Get_Incidents_Details;
        String Incident_Details_API = Incident_D_detail + "incident_code=" + Global.sharedPreferences.getString("incident_code", "0");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Incident_Details_API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Incident_s = new ArrayList<IncidentsClass>();
                incidentsClass = new IncidentsClass();
                JSONArray jarray;
                try {
                    jarray = response.getJSONArray("data");

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                for (int i = 0; i < jarray.length(); i++) {
                    final JSONObject e;
                    try {
                        e = jarray.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    incidentsClass = new IncidentsClass();
                    try {

                        incidentsClass.setInc_Date(e.getString("incident_date"));
                        incidentsClass.setIncidents_Particulars(e.getString("incident_desc"));


                        Log.d("YourTag", "Equipment Name: " + incidentsClass.getInc_Date());
                        Log.d("YourTag", "Equipment ID: " + incidentsClass.getIncidents_Particulars());


                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Incident_s.add(incidentsClass);
                    Lab_Test_Details_Adapter labTestDetailsAdapter = new Lab_Test_Details_Adapter(context, Global.Labtest_s);
                    Incident_details_Rv.setAdapter(labTestDetailsAdapter);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Log.e("Volley Error", "TimeoutError occurred");
                } else if (error instanceof NoConnectionError) {
                    Log.e("Volley Error", "NoConnectionError occurred");

                }

            }
        }) {
            public Map<String, String> getHeaders() {

                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }

        };
        queue.add(jsonObjectRequest);
    }

}