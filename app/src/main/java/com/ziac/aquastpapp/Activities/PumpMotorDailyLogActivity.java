package com.ziac.aquastpapp.Activities;


import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Adapters.StoppedPumpMotorAdapter;
import Adapters.RunningPumpMotorAdapter;
import Models.PumpMotorBlower_LogClass;

public class PumpMotorDailyLogActivity extends AppCompatActivity {
    Context context;
    String currentDatevalue, currentDateValue2;
    ImageView backbtn,Pump_reload;

    //PumpMotorBlower_LogClass pumpMotorClass;
    TextView Displaydate, Displaytime;
    RecyclerView pump_motor_started_recyclerview;
    RecyclerView pump_motor_stopped_recyclerview;
    LinearLayout Pump_start_header;
    public RunningPumpMotorAdapter dailyLogAdapter;
    public StoppedPumpMotorAdapter dailyLogAdapter1;
    View viewpump;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pump_details);
        String usertype=Global.sharedPreferences.getString("user_type","");
        context = this;
        user_topcard();
        backbtn = findViewById(R.id.back_btn);
        Pump_start_header = findViewById(R.id.pump_start_header);
        pump_motor_started_recyclerview = findViewById(R.id.pump_motor_start_recyclerview);
        pump_motor_stopped_recyclerview = findViewById(R.id.pump_motor_stop_recyclerview);
        Pump_reload = findViewById(R.id.pump_reload);
        viewpump = findViewById(R.id.viewpump);
       // content();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                handler.postDelayed(this, 1000); // Update every 1000 milliseconds (1 second)
            }
        }, 0);
        Displaydate = findViewById(R.id.displaydate);
        Displaytime = findViewById(R.id.displaytime);

        if (usertype.equals("C")) {
            hideViews();
        } else {
            showViews();
        }
        PumpsMotors();
        pump_motor_started_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        pump_motor_started_recyclerview.setHasFixedSize(true);
        pump_motor_started_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        pump_motor_stopped_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        pump_motor_stopped_recyclerview.setHasFixedSize(true);
        pump_motor_stopped_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }
    private void showViews() {
        if (Pump_start_header != null) {
            Pump_start_header.setVisibility(View.VISIBLE);
            Pump_reload.setVisibility(View.VISIBLE);
            viewpump.setVisibility(View.VISIBLE);
        }
        if (pump_motor_stopped_recyclerview != null) {
            pump_motor_stopped_recyclerview.setVisibility(View.VISIBLE);
            Pump_reload.setVisibility(View.VISIBLE);
            viewpump.setVisibility(View.VISIBLE);
        }
    }
    private void hideViews() {

        if (Pump_start_header != null) {
            Pump_start_header.setVisibility(View.GONE);
            Pump_reload.setVisibility(View.GONE);
            viewpump.setVisibility(View.GONE);
        }
        if (pump_motor_stopped_recyclerview != null) {
            pump_motor_stopped_recyclerview.setVisibility(View.GONE);
            Pump_reload.setVisibility(View.GONE);
            viewpump.setVisibility(View.GONE);
        }
    }
    private void updateDateTime() {
        Date currentDate = new Date();
        // Update date
        SimpleDateFormat dateFormat = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        }
        String formattedDate = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && dateFormat != null) {
            formattedDate = dateFormat.format(currentDate);
        }
        Displaydate.setText(formattedDate);

        SimpleDateFormat timeFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String formattedTime = timeFormat.format(currentDate);
            formattedTime = formattedTime.replace("am", "AM").replace("pm", "PM");

            Displaytime.setText(formattedTime);
        }
    }
    private void user_topcard() {
        String personname, useremail, stpname, sitename, siteaddress, processname, usermobile, stpcapacity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        siteaddress = sharedPreferences.getString("site_address", "");
        processname = sharedPreferences.getString("process_name", "");
        useremail = sharedPreferences.getString("user_email", "");
        usermobile = sharedPreferences.getString("user_mobile", "");
        personname = sharedPreferences.getString("user_name", "");
        stpcapacity = sharedPreferences.getString("stp_capacity", "");

        TextView txtsitename, txtstpname, txtsiteaddress, txtuseremail, txtusermobile, txtpersonname;

        txtsitename = findViewById(R.id.sitename);
        txtstpname = findViewById(R.id.stpname);
        txtsiteaddress = findViewById(R.id.siteaddress);
        txtuseremail = findViewById(R.id.useremail);
        txtusermobile = findViewById(R.id.usermobile);
        txtpersonname = findViewById(R.id.personname);

        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " / " + processname + " / " + stpcapacity);
        txtsiteaddress.setText(siteaddress);
        txtuseremail.setText(useremail);
        txtusermobile.setText(usermobile);
        txtpersonname.setText(personname);
    }

    private void PumpsMotors() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String dailylogpump = Global.GetDailyLogPumpMotor;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String dlog_date = Global.sharedPreferences.getString("dlogdate", "0");

        dailylogpump = dailylogpump + "comcode=" + com_code + "&sstp1_code=" + sstp1_code + "&dlog_date=" + dlog_date;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, dailylogpump, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Global.loadrunningpumps(response);
                    dailyLogAdapter = new RunningPumpMotorAdapter((List<PumpMotorBlower_LogClass>) Global.RunningPumpsMotors_LogClass, context);
                    pump_motor_started_recyclerview.setAdapter(dailyLogAdapter);

                    Global.loadstoppedpumps(response);
                    dailyLogAdapter1 = new StoppedPumpMotorAdapter((List<PumpMotorBlower_LogClass>) Global.StoppedPumpsMotors_LogClass, context);
                    pump_motor_stopped_recyclerview.setAdapter(dailyLogAdapter1);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error", Toast.LENGTH_LONG).show();
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);

    }

}