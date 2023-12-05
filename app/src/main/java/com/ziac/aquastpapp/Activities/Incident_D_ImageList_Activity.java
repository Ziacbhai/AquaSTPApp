package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
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

import Adapters.IncidentImageListAdapter;
import Adapters.Lab_Test_Details_Adapter;
import Models.IncidentsClass;

public class Incident_D_ImageList_Activity extends AppCompatActivity {
    RecyclerView Incident_Image_list_Rv;
    IncidentsClass incidentsClass;
    Context context;

    TextView usersiteH, userstpH, usersiteaddressH, Mailid, Mobno, personnameH;
    private String Personname, mail, Stpname, Sitename, SiteAddress, Process, Mobile;

    private ProgressDialog progressDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_image_list);

        context = this;

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!Global.isNetworkAvailable(this)) {
            Global.customtoast(this, getLayoutInflater(), "Internet connection lost !!");
        }
        new InternetCheckTask().execute();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);


    }

    /*private void getIncident_image_list() {

        RequestQueue queue = Volley.newRequestQueue(this);
        //String Incident_Images = Global.Get_Incidents_Details+ "incident_code=" + Global.sharedPreferences.getString("incident_code" ,"0");
        String Incident_Images = Global.Get_Incidents_Details+ "incident_code=" + "0";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Incident_Images, null, new Response.Listener<JSONObject>() {
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
                        incidentsClass.setImageList(e.getString("file_name"));
                       //Log.d("YourTag", "file_name" + incidentsClass.getImageList());
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Incident_s.add(incidentsClass);
                    IncidentImageListAdapter incidentImageListAdapter = new IncidentImageListAdapter(Global.Incident_s, context);
                    Incident_Image_list_Rv.setAdapter(incidentImageListAdapter);
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
    }*/
}