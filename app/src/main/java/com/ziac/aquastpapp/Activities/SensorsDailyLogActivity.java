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

import Adapters.SensorDailyLogAdapter;
import Adapters.SensorDailyLogEditAdapter;
import Models.SensorsModelClass;

public class SensorsDailyLogActivity extends AppCompatActivity {
    ImageView backbtn;
    TextView Displaydate, Displaytime,Total_sensor_header;
    SensorsModelClass sensorsModelClass;
    LinearLayout Sensor_header;
    RecyclerView sensor_recyclerView;
    RecyclerView sensor_recyclerView2;
    Context context;
    View viewhide;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        context = this;
        user_topcard();
        backbtn = findViewById(R.id.back_btn);
        viewhide = findViewById(R.id.viewhide3);

        Displaydate = findViewById(R.id.displaydate);
        Displaytime = findViewById(R.id.displaytime);
        Sensor_header = findViewById(R.id.sensor_header);
        sensor_recyclerView = findViewById(R.id.sensor_edit_recyclerview);
        sensor_recyclerView2 = findViewById(R.id.sensors_recyclerview);
        Total_sensor_header = findViewById(R.id.total_sensor_header);

        String usertype=Global.sharedPreferences.getString("user_type","");
        if (usertype.equals("C")) {
            hideViews();
        } else {
            showViews();
        }

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

        DailyLogSensorsEdit();

        if (sensor_recyclerView != null) {
            sensor_recyclerView.setLayoutManager(new LinearLayoutManager(this));
            sensor_recyclerView.setHasFixedSize(true);
            sensor_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
        DailyLogSensors();
        if (sensor_recyclerView2!= null){
            sensor_recyclerView2.setLayoutManager(new LinearLayoutManager(this));
            sensor_recyclerView2.setHasFixedSize(true);
            sensor_recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }

    private void showViews() {
        if (Sensor_header != null) {
            Sensor_header.setVisibility(View.VISIBLE);
            Total_sensor_header.setVisibility(View.VISIBLE);
            viewhide.setVisibility(View.VISIBLE);
        }
        if (sensor_recyclerView != null) {
            sensor_recyclerView.setVisibility(View.VISIBLE);
            Total_sensor_header.setVisibility(View.VISIBLE);
            viewhide.setVisibility(View.VISIBLE);
        }
    }

    private void hideViews() {
        if (Sensor_header != null) {
            Sensor_header.setVisibility(View.GONE);
            Total_sensor_header.setVisibility(View.GONE);
            viewhide.setVisibility(View.GONE);
        }
        if (sensor_recyclerView != null) {
            sensor_recyclerView.setVisibility(View.GONE);
            Total_sensor_header.setVisibility(View.GONE);
            viewhide.setVisibility(View.GONE);

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
        personname = sharedPreferences.getString("person_nameu", "");
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


    private void DailyLogSensorsEdit() {

        RequestQueue queue = Volley.newRequestQueue(context);
        String dailylogsensors = Global.GetDailyLogSensors;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String dlog_date = Global.sharedPreferences.getString("dlogdate", "0");

        dailylogsensors = dailylogsensors + "comcode=" + com_code + "&sstp1_code=" + sstp1_code + "&dlog_date=" + dlog_date;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, dailylogsensors, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Global.Sensors_Class = new ArrayList<SensorsModelClass>();
                sensorsModelClass = new SensorsModelClass();
                JSONArray jarray;
                try {
                    jarray = response.getJSONArray("sensors1");
                    for (int i = 0; i < jarray.length(); i++) {
                        final JSONObject e;
                        try {
                            e = jarray.getJSONObject(i);
                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        sensorsModelClass = new SensorsModelClass();
                        try {
                            sensorsModelClass.setEquip_name(e.getString("equip_name"));
                            sensorsModelClass.setSensor_tstp6_code(e.getString("tstp6_code"));
                            sensorsModelClass.setSensor_status(e.getString("status"));
                            //sensorsModelClass.setReading(e.getString("starttime"));

                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        Global.Sensors_Class.add(sensorsModelClass);
                        SensorDailyLogEditAdapter sensorDailyLogEditAdapter = new SensorDailyLogEditAdapter((List<SensorsModelClass>) Global.Sensors_Class, context);
                        sensor_recyclerView.setAdapter(sensorDailyLogEditAdapter);
                    }
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

        queue.add(jsonObjectRequest);
    }

    private void DailyLogSensors() {

        RequestQueue queue = Volley.newRequestQueue(context);
        String dailylogsensors = Global.GetDailyLogSensors;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String dlog_date = Global.sharedPreferences.getString("dlogdate", "0");

        dailylogsensors = dailylogsensors + "comcode=" + com_code + "&sstp1_code=" + sstp1_code + "&dlog_date=" + dlog_date;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, dailylogsensors, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Sensors_Class = new ArrayList<SensorsModelClass>();
                sensorsModelClass = new SensorsModelClass();
                JSONArray jarray;
                try {
                    jarray = response.getJSONArray("sensors2");
                    for (int i = 0; i < jarray.length(); i++) {
                        final JSONObject e;
                        try {
                            e = jarray.getJSONObject(i);
                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        sensorsModelClass = new SensorsModelClass();
                        try {
                            sensorsModelClass.setEquip_name(e.getString("equip_name"));
                            sensorsModelClass.setReading(e.getString("reading_value"));
                            sensorsModelClass.setReading_time(e.getString("readingtime"));
                            sensorsModelClass.setSensor_total(e.getString("final_value"));
                            sensorsModelClass.setSensor_image(e.getString("image_path"));
                            sensorsModelClass.setSensor_tstp6_code(e.getString("tstp6_code"));

                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        Global.Sensors_Class.add(sensorsModelClass);
                        SensorDailyLogAdapter sensorDailyLogAdapter = new SensorDailyLogAdapter((List<SensorsModelClass>) Global.Sensors_Class, context);
                        sensor_recyclerView2.setAdapter(sensorDailyLogAdapter);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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