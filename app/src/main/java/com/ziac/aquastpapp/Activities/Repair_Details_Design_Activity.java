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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Adapters.RepairAdapter;
import Adapters.Repair_details_Adapter;
import Models.RepairsClass;

public class Repair_Details_Design_Activity extends AppCompatActivity {
    RepairsClass repair_s;

    TextView Date_A,STP_A,Remark_A;
    AppCompatButton Update_A,Cancel_A;
    RecyclerView Repair_details_recyclerview;
    Context context;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details_design);
        context = this;
        user_topcard();

        if (!Global.isNetworkAvailable(this)) {
            Global.customtoast(this, getLayoutInflater(), "Internet connection lost !!");
        }
        new InternetCheckTask().execute();

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
              // updateRepairdetails();
                dialog.dismiss();
            }
        });
        Cancel_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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

   /* private void updateRepairdetails() {
        String remarks=Remark_A.getText().toString();
        String equipment=Equipment_name_cd.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Global."" + "type=" + "I";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String sresponse) {
                JSONObject response;
                try {
                    response = new JSONObject(sresponse);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    if (response.getBoolean("isSuccess")) {
                        Toast.makeText(Repair_Details_Design_Activity.this, "Updated successfully !!",Toast.LENGTH_SHORT).show();
                        get_Details_Repair();
                    } else {
                        Toast.makeText(Repair_Details_Design_Activity.this, response.getString("error"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "failed to upload", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("", equipment);
                params.put("",remarks);
                params.put("com_code", Global.sharedPreferences.getString("com_code", "0"));
                params.put("ayear", Global.sharedPreferences.getString("ayear", "0"));
                params.put("sstp1_code", Global.sharedPreferences.getString("sstp1_code", "0"));
                params.put("con1_code", "0");
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(2500), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }*/
}