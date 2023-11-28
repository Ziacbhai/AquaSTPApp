package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

import Adapters.Consumables_Details_Adapter;
import Adapters.Lab_Test_Details_Adapter;
import Models.ConsumablesClass;
import Models.LabTestClass;

public class Lab_Test_Details_Design_Activity extends AppCompatActivity {
    RecyclerView Labtest_details_Rv;
    LabTestClass labTest_Dclass;
    Context context;
    TextView usersiteH,userstpH,usersiteaddressH ,Mailid,Mobno,personnameH;
    private String Personname,mail,Stpname ,Sitename ,SiteAddress,Process ,Mobile;

    private ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_details_design);


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
        mail = sharedPreferences.getString("user_email", "");
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

        Mailid.setText(mail);
        Mobno.setText(Mobile);
        personnameH.setText(Personname);

        Labtest_details_Rv = findViewById(R.id.labTestRecyclerview);
        Labtest_details_Rv.setLayoutManager(new LinearLayoutManager(this));
        Labtest_details_Rv.setHasFixedSize(true);
        Labtest_details_Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        getLabTestDetails();
    }

    private void getLabTestDetails() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String labTest_detail = Global.Get_Lab_Details;
        String Lab_Details_API = labTest_detail + "test_code=" + Global.sharedPreferences.getString("test_code", "0");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Lab_Details_API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Labtest_s = new ArrayList<LabTestClass>();
                labTest_Dclass = new LabTestClass();
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
                    labTest_Dclass = new LabTestClass();
                    try {
                        labTest_Dclass.setTRno(e.getString(""));

                      /*  Log.d("YourTag", "Equipment Name: " + consumables_Class.getEquipment_Name());
                        Log.d("YourTag", "Equipment ID: " + consumables_Class.getEquipment_id());
                        Log.d("YourTag", "D Amount: " + consumables_Class.getD_Amount());*/

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Labtest_s.add(labTest_Dclass);
                    Lab_Test_Details_Adapter labTestDetailsAdapter = new Lab_Test_Details_Adapter(context, Global.Labtest_s);
                    Labtest_details_Rv.setAdapter(labTestDetailsAdapter);
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
    }
}