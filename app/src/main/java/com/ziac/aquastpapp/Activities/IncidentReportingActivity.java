package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ziac.aquastpapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.IncidentAdapter;
import Models.IncidentsClass;
import de.hdodenhof.circleimageview.CircleImageView;

public class IncidentReportingActivity extends AppCompatActivity{
    public static final int PICK_IMAGE_REQUEST = 0;
    private ImagePickerCallback imagePickerCallback;
    private static final int CAMERA_REQUEST = 1001;
    RecyclerView Incident_recyclerview;
    IncidentsClass incidents;
    // private ImageView mImageView;
    IncidentAdapter incidentAdapter;
    Bitmap imageBitmap;

    EditText uName, uNumber, uEmail;
    TextView Uref_code;
    //private static final int CAMERA_REQUEST = 0;
    Picasso.Builder builder;
    Picasso picasso;
    Toolbar toolbar;
    CircleImageView Profile,circleImageView;
    Context context;

    IncidentsClass incidentsClass;
    private String userimage, mail, Stpname, Sitename, Siteaddress, userref, person_name;
    TextView usersiteH,userstpH,usersiteaddressH ,Mailid,Mobno,personnameH;
    private String Personname,Mail ,SiteAddress,Process,Mobile;
    private ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_reporting);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        context = this;
        incidentsClass = new IncidentsClass();

        Profile = findViewById(R.id.profileIcon);
        if (!Global.isNetworkAvailable(this)) {
            Global.customtoast(this, getLayoutInflater(), "Internet connection lost !!");
        }
        new InternetCheckTask().execute();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        Sitename = sharedPreferences.getString("site_name", "");
        Stpname = sharedPreferences.getString("stp_name", "");
        SiteAddress = sharedPreferences.getString("site_address", "");
        Process = sharedPreferences.getString("process_name", "");
        Mail = sharedPreferences.getString("user_email", "");
        Mobile = sharedPreferences.getString("user_mobile", "");
        Personname = sharedPreferences.getString("person_name", "");

        usersiteH = findViewById(R.id.site_name);
        userstpH = findViewById(R.id.stp_name);
        usersiteaddressH = findViewById(R.id.site_address);
        Mailid = findViewById(R.id.email);
        Mobno = findViewById(R.id._mobile);
        personnameH = findViewById(R.id.person_name);

        usersiteH.setText(Sitename);
        userstpH.setText(Stpname + " / " + Process);
        usersiteaddressH.setText(SiteAddress);
        Mailid.setText(Mail);
        Mobno.setText(Mobile);
        personnameH.setText(Personname);

        userimage = Global.userImageurl + sharedPreferences.getString("user_image", "");
        userref = sharedPreferences.getString("ref_code", "");
        person_name = Global.sharedPreferences.getString("person_name", "");

        mail = Global.sharedPreferences.getString("user_email", "");
        Sitename = sharedPreferences.getString("site_name", "");
        Stpname = sharedPreferences.getString("stp_name", "");
        ////
        Siteaddress = sharedPreferences.getString("site_address", "");
        Siteaddress = sharedPreferences.getString("process_name", "");



        Picasso.Builder builder = new Picasso.Builder(getApplication());
        Picasso picasso = builder.build();
        picasso.load(Uri.parse(userimage))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(Profile);
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(IncidentReportingActivity.this,v);
                popup.getMenuInflater().inflate(R.menu.profile_pop_up, popup.getMenu());

                // Retrieve data from SharedPreferences
                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(IncidentReportingActivity.this);
                String profileName = Global.sharedPreferences.getString("ref_code", "");
                MenuItem profileMenuItem = popup.getMenu().findItem(R.id.refaral_code);
                profileMenuItem.setTitle("Code: " + profileName);

                userimage = Global.userImageurl + sharedPreferences.getString("user_image", "");
                Picasso.get().load(userimage).into(Profile);

                popup.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.my_profile) {
                        startActivity(new Intent(IncidentReportingActivity.this, ProfileActivity.class));
                        return true;
                    }
                    if (itemId == R.id.nav_logout) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(IncidentReportingActivity.this);
                        builder.setTitle("Logout Confirmation");
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked "Yes", perform logout action
                                startActivity(new Intent(IncidentReportingActivity.this, LoginSignupActivity.class));
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked "No", dismiss the dialog
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        return true;

                    }
                    if (itemId == R.id.changepwd) {
                        startActivity(new Intent(IncidentReportingActivity.this, ChangePasswordActivity.class));
                        return true;
                    } else {

                    }
                    return false;
                });
                popup.show();
            }
        });

        Incident_recyclerview = findViewById(R.id.incident_recyclerview);
        Incident_recyclerview.setLayoutManager(new LinearLayoutManager(context));
        Incident_recyclerview.setHasFixedSize(true);
        Incident_recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        getIncidentReport();
    }


    private void getIncidentReport() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String incident = Global.Get_Incidents;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String ayear = Global.sharedPreferences.getString("ayear", "2023");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        incident = incident + "comcode=" + com_code + "&ayear=" +  ayear + "&sstp1_code=" + sstp1_code ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, incident, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Incident_s = new ArrayList<IncidentsClass>();
                incidents = new IncidentsClass();
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
                    incidents = new IncidentsClass();
                    try {
                        incidents.setInc_No(e.getString("incident_code"));
                        incidents.setInc_Date(e.getString("incident_date"));
                        incidents.setIncidents_Particulars(e.getString("incident_desc"));

                        String incident_code= e.getString("incident_code");
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.putString("incident_code",incident_code);
                       // Log.d("YourTag", "incident_code" + incident_code);
                        //Toast.makeText(context, ""+incident_code, Toast.LENGTH_SHORT).show();
                        Global.editor.commit();

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Incident_s.add(incidents);
                    incidentAdapter = new IncidentAdapter(context, Global.Incident_s);
                    Incident_recyclerview.setAdapter(incidentAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the captured image
            imageBitmap = (Bitmap) data.getExtras().get("data");
            // Notify the adapter or do whatever you need with the image
            incidentAdapter.notifyDataSetChanged(); // Assuming you have a notifyDataSetChanged() method
        }
    }

}