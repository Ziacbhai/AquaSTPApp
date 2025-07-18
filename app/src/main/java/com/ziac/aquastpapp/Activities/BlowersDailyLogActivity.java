package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Adapters.BlowersDailyLogStartAdapter;
import Models.PumpMotorBlowerLogModel;

public class BlowersDailyLogActivity extends AppCompatActivity {
    Context context;
    TextView Displaydate, Displaytime;
    ImageView backbtn;
    RecyclerView blowers_started_recyclerview;
    TextView Startheading;
    View blowerview;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blowers);

        context = this;
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        user_topcard();
        backbtn = findViewById(R.id.back_btn);
        // blowerview = findViewById(R.id.blowerview);
        Displaydate = findViewById(R.id.displaydate);
        Displaytime = findViewById(R.id.displaytime);
        // Startheading = findViewById(R.id.startheading);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        String usertype = sharedPreferences.getString("user_type", "");


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                handler.postDelayed(this, 1000); // Update every 1000 milliseconds (1 second)
            }
        }, 0);
        if (usertype.equals("C")) {
            // hideViews();
        } else {
            // showViews();
        }
        DailyLogBlowers();
        blowers_started_recyclerview = findViewById(R.id.blowers_started_recyclerview);
        blowers_started_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        blowers_started_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    private void showViews() {
        if (Startheading != null) {
            Startheading.setVisibility(View.VISIBLE);
            blowerview.setVisibility(View.VISIBLE);
        }
    }

    private void hideViews() {
        if (Startheading != null) {
            Startheading.setVisibility(View.GONE);
            blowerview.setVisibility(View.GONE);
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


        TextView txtsitename, txtstpname, txtsiteaddress, txtuseremail, txtusermobile, txtpersonname;

        txtsitename = findViewById(R.id.sitename);
        txtstpname = findViewById(R.id.stpname);
        // txtsiteaddress = findViewById(R.id.siteaddress);
        txtuseremail = findViewById(R.id.useremail);
        txtusermobile = findViewById(R.id.usermobile);
        txtpersonname = findViewById(R.id.personname);

        txtsitename.setText(sharedPreferences.getString("site_name", ""));
        txtstpname.setText(sharedPreferences.getString("stp_name", "") + " " +
                sharedPreferences.getString("process_name", "") + " " + sharedPreferences.getString("stp_capacity", ""));
        // txtsiteaddress.setText(sharedPreferences.getString("site_address", ""));
        txtuseremail.setText(sharedPreferences.getString("user_email", ""));
        txtusermobile.setText(sharedPreferences.getString("user_mobile", ""));
        txtpersonname.setText(sharedPreferences.getString("user_name", ""));
    }

    private void DailyLogBlowers() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String dailylogblowers = Global.GetDailyLogBlowers;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String dlog_date = Global.sharedPreferences.getString("dlogdate", "0");

        dailylogblowers = dailylogblowers + "comcode=" + com_code + "&sstp1_code=" + sstp1_code + "&dlog_date=" + dlog_date;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, dailylogblowers, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Global.Blower_LogClass = new ArrayList<PumpMotorBlowerLogModel>();
                //blowerClass = new PumpMotorBlower_LogClass();
                JSONArray jarray;

                try {
                    jarray = response.getJSONArray("blowers");
                    for (int i = 0; i < jarray.length(); i++) {
                        final JSONObject e;
                        try {
                            e = jarray.getJSONObject(i);
                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        PumpMotorBlowerLogModel blowerClass = new PumpMotorBlowerLogModel();
                        try {
                            blowerClass.setEquip_name(e.getString("equip_name"));
                            blowerClass.setStart_time(e.getString("starttime"));
                            blowerClass.setEnd_time(e.getString("endtime"));
                            blowerClass.setRunning_time(e.getString("running_time"));
                            blowerClass.setRunning_status(e.getString("running_status"));
                            blowerClass.setTstp5_code(e.getString("tstp5_code"));

                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        Global.Blower_LogClass.add(blowerClass);

                    }

                    RecyclerView blowersStartedRecyclerView = findViewById(R.id.blowers_started_recyclerview);
                    BlowersDailyLogStartAdapter blowerAdapter = new BlowersDailyLogStartAdapter(context, (List<PumpMotorBlowerLogModel>) Global.Blower_LogClass);
                    blowersStartedRecyclerView.setAdapter(blowerAdapter);


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}