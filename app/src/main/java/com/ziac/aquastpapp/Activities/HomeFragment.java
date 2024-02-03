package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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
import java.util.List;
import java.util.Map;

import Adapters.FiltersDailyLogAdapter;
import Models.DailyLogClass;
import Models.FiltersClass;


public class HomeFragment extends Fragment {

    ProgressDialog progressDialog;
    FloatingActionButton Fab;

    DailyLogClass dailyLog;
    Context context;



    RelativeLayout layoutpump, layoutblower, layoutmeter, layoutsensor, layoutfilter, layouthandover_remark;

    @Override
    public void onResume() {
        super.onResume();
        DailyLogIndex();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        user_topcard(view);

        DailyLogIndex();

        layoutpump = view.findViewById(R.id.pumpmotor);
        layoutblower = view.findViewById(R.id.blower);
        layoutmeter = view.findViewById(R.id.meter);
        layoutsensor = view.findViewById(R.id.sensor);
        layoutfilter = view.findViewById(R.id.filter);
        layouthandover_remark = view.findViewById(R.id.handover_remarks);

        layoutpump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pump = new Intent(getActivity(), PumpMotorDailyLogActivity.class);
                startActivity(pump);
            }
        });
        layoutblower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blower = new Intent(getActivity(), BlowersDailyLogActivity.class);
                startActivity(blower);
            }
        });
        layoutmeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent meter = new Intent(getActivity(), MeterDailyLogActivity.class);
                startActivity(meter);
            }
        });
        layoutsensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sensors = new Intent(getActivity(), SensorsDailyLogActivity.class);
                startActivity(sensors);
            }
        });
        layoutfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filter = new Intent(getActivity(), FiltersDailyLogActivity.class);
                startActivity(filter);
            }
        });

        layouthandover_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filter = new Intent(getActivity(), Handover_Remarks_Activity.class);
                startActivity(filter);
            }
        });


        return view;
    }

    private void user_topcard(View view) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        String personname, useremail, stpname, sitename, siteaddress, processname, usermobile, stpcapacity;
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        siteaddress = sharedPreferences.getString("site_address", "");
        processname = sharedPreferences.getString("process_name", "");
        useremail = sharedPreferences.getString("user_email", "");
        usermobile = sharedPreferences.getString("user_mobile", "");
        personname = sharedPreferences.getString("person_nameu", "");
        stpcapacity = sharedPreferences.getString("stp_capacity", "");

        TextView txtsitename, txtstpname, txtsiteaddress, txtuseremail, txtusermobile, txtpersonname;

        txtsitename = view.findViewById(R.id.sitename);
        txtstpname = view.findViewById(R.id.stpname);
        txtsiteaddress = view.findViewById(R.id.siteaddress);
        txtuseremail = view.findViewById(R.id.useremail);
        txtusermobile = view.findViewById(R.id.usermobile);
        txtpersonname = view.findViewById(R.id.personname);

        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " / " + processname + " / " + stpcapacity);
        txtsiteaddress.setText(siteaddress);
        txtuseremail.setText(useremail);
        txtusermobile.setText(usermobile);
        txtpersonname.setText(personname);
    }

    private void DailyLogIndex() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String dailylog = Global.GetDailyLogIndex;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String ayear = Global.sharedPreferences.getString("ayear", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");

        dailylog = dailylog + "comcode=" + com_code + "&ayear=" + ayear + "&sstp1_code=" + sstp1_code;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, dailylog, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.dailyLogClassArrayList = new ArrayList<DailyLogClass>();
                JSONArray jarray;

                try {
                    // Check if the response was successful
                    boolean isSuccess = response.getBoolean("isSuccess");

                    if (isSuccess) {
                        // The "dlogdate" is present in the response
                        String dlogdate = response.getString("dlogdate");
                        Log.d("YourTag", "Daily Log Date: " + dlogdate);


                        DailyLogClass dailyLog = new DailyLogClass();
                        dailyLog.setDailylog(dlogdate);
                        Global.dailyLogClassArrayList.add(dailyLog);
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.putString("dlogdate", dlogdate);
                        Global.editor.commit();

                    } else {
                        String error = response.getString("error");
                        Toast.makeText(requireActivity(), error, Toast.LENGTH_LONG).show();
                    }

                    jarray = response.getJSONArray("data");
                    for (int i = 0; i < jarray.length(); i++) {
                        final JSONObject e;
                        try {
                            e = jarray.getJSONObject(i);
                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }

                        try {
                            DailyLogClass dailyLogClass = new DailyLogClass();
                            dailyLogClass.setTstp1_code(e.getString("tstp1_code"));
                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }

                    }


                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle Volley errors
                if (error instanceof TimeoutError) {
                    Toast.makeText(requireActivity(), "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(requireActivity(), "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(requireActivity(), "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(requireActivity(), "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(requireActivity(), "Parse Error", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                // Set the Authorization header with the access token
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // If you have any parameters to send in the request body, you can set them here
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        queue.add(jsonObjectRequest);
    }

}