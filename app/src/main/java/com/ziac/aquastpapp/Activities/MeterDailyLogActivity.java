package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import Adapters.MeterDailyLogAdapter;
import Adapters.MeterDailyLogEditAdapter;
import Models.MetersDailyLogModel;

public class MeterDailyLogActivity extends AppCompatActivity {
    ImageView backbtn;
    TextView Displaydate, Displaytime;
    RecyclerView Meters_recyclerview;
    RecyclerView Meters_recyclerview2;
    LinearLayout Meter_header;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter);

        context = this;
        user_topcard();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        backbtn = findViewById(R.id.back_btn);

        Displaydate = findViewById(R.id.displaydate);
        Displaytime = findViewById(R.id.displaytime);
        String usertype = Global.sharedPreferences.getString("user_type", "");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
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

        // First, initialize your views
        Meter_header = findViewById(R.id.meter_header);
        Meters_recyclerview = findViewById(R.id.meter_edit_recyclerview);
        Meters_recyclerview2 = findViewById(R.id.meter2_recyclerview);

// Then, perform visibility manipulation based on the user type
        if (usertype.equals("C")) {
            if (Meter_header != null) {
                Meter_header.setVisibility(View.GONE);
            }
            if (Meters_recyclerview != null) {
                Meters_recyclerview.setVisibility(View.GONE);
            }
        } else {
            if (Meter_header != null) {
                Meter_header.setVisibility(View.VISIBLE);
            }
            if (Meters_recyclerview != null) {
                Meters_recyclerview.setVisibility(View.VISIBLE);
            }
        }

// Continue with the rest of your code...
        DailyLogMetersEdit();
        if (Meters_recyclerview != null) {
            Meters_recyclerview.setLayoutManager(new LinearLayoutManager(this));
            Meters_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
        if (Meters_recyclerview2 != null) {
            Meters_recyclerview2.setLayoutManager(new LinearLayoutManager(this));
            Meters_recyclerview2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
        swipeRefreshLayout.setOnRefreshListener(() -> {
            DailyLogMetersEdit(); // or DailyLogSensors() depending on which one you want to refresh
        });
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
       // txtsiteaddress = findViewById(R.id.siteaddress);
        txtuseremail = findViewById(R.id.useremail);
        txtusermobile = findViewById(R.id.usermobile);
        txtpersonname = findViewById(R.id.personname);

        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " " + processname + " " + stpcapacity);
      // txtsiteaddress.setText(siteaddress);
        txtuseremail.setText(useremail);
        txtusermobile.setText(usermobile);
        txtpersonname.setText(personname);
    }

    private void DailyLogMetersEdit() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String dailylogmeter = Global.GetDailyLogMeter;
        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String dlog_date = Global.sharedPreferences.getString("dlogdate", "0");

        dailylogmeter = dailylogmeter + "comcode=" + com_code + "&sstp1_code=" + sstp1_code + "&dlog_date=" + dlog_date;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, dailylogmeter, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Global.loadmeter1(response);
                    MeterDailyLogEditAdapter meterDailyLogEditAdapter = new MeterDailyLogEditAdapter((List<MetersDailyLogModel>) Global.Meters_Class, context);
                    Meters_recyclerview.setAdapter(meterDailyLogEditAdapter);
                    meterDailyLogEditAdapter.notifyDataSetChanged();

                    // Common code block
                    Global.loadmeter2(response);
                    MeterDailyLogAdapter meterDailyLogAdapter = new MeterDailyLogAdapter((List<MetersDailyLogModel>) Global.Meters_Class, context);
                    Meters_recyclerview2.setAdapter(meterDailyLogAdapter);
                    meterDailyLogAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
// Dismiss the refresh indicator
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

}