package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.RepairAdapter;
import Adapters.Repair_details_Adapter;
import Models.RepairsClass;

public class Repair_Details_Design_Activity extends AppCompatActivity {
    RepairsClass repair_s;

    TextView Date_A,STP_A,Remark_A;
    AppCompatButton Update_A,Cancel_A;
    RecyclerView Repair_details_recyclerview;

    TextView usersiteH,userstpH,usersiteaddressH ,Mailid,Mobno,personnameH;
    private String Personname,Mail,Stpname ,Sitename ,SiteAddress,Process,Mobile;

    Context context;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details_design);


        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

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

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDetailsDialog(context);
            }
        });

        Repair_details_recyclerview = findViewById(R.id.repair_details_recyclerview);
        Repair_details_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        Repair_details_recyclerview.setHasFixedSize(true);
        Repair_details_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        get_Details_Repair();

    }
    @SuppressLint("MissingInflatedId")
    private void showAddDetailsDialog (Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_repair_details_layout, null);


        Date_A = dialogView.findViewById(R.id.date_alert_rd);
        Remark_A = dialogView.findViewById(R.id.remark_alert_rd);
        Update_A = dialogView.findViewById(R.id.update_alert_rd);
        Cancel_A = dialogView.findViewById(R.id.cancel_alert_rd);

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
            dialog.getWindow().setAttributes(layoutParams);
        }

        dialog.show();

        Update_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle update button click
                // You can add your logic here
                dialog.dismiss(); // Close the dialog if needed
            }
        });
        Cancel_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle cancel button click
                // You can add your logic here
                dialog.dismiss(); // Close the dialog if needed
            }
        });

    }
    private void get_Details_Repair() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Global.Get_Repairs_Details;

        String Repair_Details_API = url + "repair1_code=" + Global.sharedPreferences.getString("repair1_code", "0");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Repair_Details_API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Repair_s = new ArrayList<RepairsClass>();
                repair_s = new RepairsClass();
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
                    repair_s = new RepairsClass();
                    try {
                        repair_s.setD_Equipment_Name(e.getString("equip_name"));
                        repair_s.setD_Equipment_Number(e.getString("equip_slno"));
                        repair_s.setD_Amount(e.getString("repaired_amt"));
                        repair_s.setD_Repaired(e.getString("repaired_flag"));
                        repair_s.setD_Remark(e.getString("repaired_remarks"));
                        //String repair_code= repair_s.getRepair_code();
                        // System.out.println(repair_code);
                      //  Global.editor = Global.sharedPreferences.edit();
                       // Global.editor.putString("",repair_code);
                       //Global.editor.commit();
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Repair_s.add(repair_s);
                    Repair_details_Adapter repair_details_adapter = new Repair_details_Adapter(Global.Repair_s, context);
                    Repair_details_recyclerview.setAdapter(repair_details_adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            public Map<String, String> getHeaders() {
                // below line we are creating a map for
                // storing our values in key and value pair.
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