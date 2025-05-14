package Fragments;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ziac.aquastpapp.Activities.BlowersDailyLogActivity;
import com.ziac.aquastpapp.Activities.FiltersDailyLogActivity;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.Activities.HandoverRemarksActivity;
import com.ziac.aquastpapp.Activities.MeterDailyLogActivity;
import com.ziac.aquastpapp.Activities.PumpMotorDailyLogActivity;
import com.ziac.aquastpapp.Activities.SelectSTPLocationActivity;
import com.ziac.aquastpapp.Activities.SensorsDailyLogActivity;
import com.ziac.aquastpapp.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import Models.DailyLogModel;



public class HomeFragment extends Fragment {
    ProgressDialog progressDialog;
    FloatingActionButton Fab;
    DailyLogModel dailyLog;
    Context context;
    CardView layoutpump, CardselectSTP,layoutblower, layoutmeter, layoutsensor, layoutfilter, layouthandover_remark;

   /* @Override
    public void onResume() {
        super.onResume();
        DailyLogIndex();
    }*/

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        user_topcard(view);
        context = getActivity();
        DailyLogIndex();
        CardselectSTP = view.findViewById(R.id.selectSTP);
        layoutpump = view.findViewById(R.id.pumpmotor);
        layoutblower = view.findViewById(R.id.blower);
        layoutmeter = view.findViewById(R.id.meter);
        layoutsensor = view.findViewById(R.id.sensor);
        layoutfilter = view.findViewById(R.id.filter);
        layouthandover_remark = view.findViewById(R.id.handover_remarks);

        String usertype = Global.sharedPreferences.getString("user_type", "");

        if (usertype.equals("C")) {
            hideViews();
        } else {
            showViews();
        }

        CardselectSTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), SelectSTPLocationActivity.class);
                startActivity(intent);
            }
        });


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
                Intent remark = new Intent(getActivity(), HandoverRemarksActivity.class);
                startActivity(remark);
            }
        });
        return view;
    }

    private void showViews() {
        if (layouthandover_remark != null) {
            layouthandover_remark.setVisibility(View.VISIBLE);
        }
    }

    private void hideViews() {
        if (layouthandover_remark != null) {
            layouthandover_remark.setVisibility(View.GONE);
        }
    }

    private void user_topcard(View view) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        String personname, useremail, stpname, sitename, siteaddress, processname, usermobile, stpcapacity;

        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        siteaddress = sharedPreferences.getString("site_address", "");
        processname = sharedPreferences.getString("process_name", "");
        useremail = sharedPreferences.getString("user_email", "");
        usermobile = sharedPreferences.getString("user_mobile", "");
        personname = sharedPreferences.getString("user_name", "");
        stpcapacity = sharedPreferences.getString("stp_capacity", "");

        TextView txtsitename, txtstpname, txtsiteaddress, txtuseremail, txtusermobile, txtpersonname;

        txtsitename = view.findViewById(R.id.sitename);
        txtstpname = view.findViewById(R.id.stpname);
       // txtsiteaddress = view.findViewById(R.id.siteaddress);
        txtuseremail = view.findViewById(R.id.useremail);
        txtusermobile = view.findViewById(R.id.usermobile);
        txtpersonname = view.findViewById(R.id.personname);

        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " " + processname + " " + stpcapacity);
/*
        txtsiteaddress.setText(siteaddress);
*/
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
                Global.dailyLogModelArrayList = new ArrayList<DailyLogModel>();
                JSONArray jarray;

                try {
                    // Check if the response was successful
                    boolean isSuccess = response.getBoolean("isSuccess");

                    if (isSuccess) {
                        // The "dlogdate" is present in the response
                        String dlogdate = response.getString("dlogdate");
                        Log.d("YourTag", "Daily Log Date: " + dlogdate);

                        DailyLogModel dailyLog = new DailyLogModel();
                        dailyLog.setDailylog(dlogdate);
                        Global.dailyLogModelArrayList.add(dailyLog);

                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.putString("dlogdate", dlogdate);
                        Global.editor.commit();

                        // Extracting and displaying "tstp1_code"
                        JSONObject dataObject = response.getJSONObject("data");
                        String tstp1_code = dataObject.getString("tstp1_code");
                        Global.editor.putString("tstp1_code", tstp1_code);
                        Global.editor.commit();
                    } else {
                       /* String error = response.getString("error");
                        Toast.makeText(requireActivity(), error, Toast.LENGTH_LONG).show();*/
                        Global.customtoast(context, getLayoutInflater(), response.getString("error"));

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }
}