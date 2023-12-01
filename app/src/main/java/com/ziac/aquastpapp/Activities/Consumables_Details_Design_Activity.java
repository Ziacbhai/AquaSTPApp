package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

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
import android.util.Log;
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

import Adapters.Consumables_Details_Adapter;
import Adapters.Repair_details_Adapter;
import Models.ConsumablesClass;
import Models.RepairsClass;

public class Consumables_Details_Design_Activity extends AppCompatActivity {
    ConsumablesClass consumables_Class;
    RecyclerView Consumables_D_Rv;
    Context context;

    TextView usersiteH,userstpH,usersiteaddressH ,Mailid,Mobno,personnameH;
    private String Personname,Mail,Stpname ,Sitename ,SiteAddress,Process,Mobile;
    TextView Date_A,STP_A,Remark_A;
    AppCompatButton Update_A,Cancel_A;
    private ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumables_details_design);


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

        Consumables_D_Rv = findViewById(R.id.consumables_details_recyclerview);
        Consumables_D_Rv.setLayoutManager(new LinearLayoutManager(this));
        Consumables_D_Rv.setHasFixedSize(true);
        Consumables_D_Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        getConsumables_Details();

    }

    @SuppressLint("MissingInflatedId")
    private void showAddDetailsDialog (Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_consumable_details_layout, null);

        STP_A = dialogView.findViewById(R.id.stp_alert_cd);
        Date_A = dialogView.findViewById(R.id.date_alert_cd);
        Remark_A = dialogView.findViewById(R.id.remark_alert_cd);
        Update_A = dialogView.findViewById(R.id.update_alert_cd);
        Cancel_A = dialogView.findViewById(R.id.cancel_alert_cd);

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

    private void getConsumables_Details() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String  consumables_d = Global.Get_Consumables_Details;
        String Consumable_Details_API = consumables_d + "con1_code=" + Global.sharedPreferences.getString("con1_code", "0");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Consumable_Details_API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Consumables_s = new ArrayList<ConsumablesClass>();
                consumables_Class = new ConsumablesClass();
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
                    consumables_Class = new ConsumablesClass();
                    try {
                        consumables_Class.setEquipment_Name(e.getString("equip_name"));
                        consumables_Class.setEquipment_id(e.getString("equip_slno"));
                        consumables_Class.setD_Amount(e.getString("prd_amt"));
                        consumables_Class.setD_item(e.getString("part_no"));
                        consumables_Class.setD_item_name(e.getString("prd_name"));
                        consumables_Class.setD_qty(e.getString("qty"));
                        consumables_Class.setD_unit(e.getString("unit_name"));
                        consumables_Class.setD_rate(e.getString("prch_price"));

                      /*  Log.d("YourTag", "Equipment Name: " + consumables_Class.getEquipment_Name());
                        Log.d("YourTag", "Equipment ID: " + consumables_Class.getEquipment_id());
                        Log.d("YourTag", "D Amount: " + consumables_Class.getD_Amount());*/

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Consumables_s.add(consumables_Class);
                    Consumables_Details_Adapter consumablesDetailsAdapter = new Consumables_Details_Adapter(context, Global.Consumables_s);
                    Consumables_D_Rv.setAdapter(consumablesDetailsAdapter);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Global.customtoast(Consumables_Details_Design_Activity.this,getLayoutInflater(), error.getMessage());
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
}