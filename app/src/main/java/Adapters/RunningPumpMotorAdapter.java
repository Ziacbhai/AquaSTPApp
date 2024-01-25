package Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
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
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.Activities.PumpMoterDailyLogActivity;
import com.ziac.aquastpapp.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.PumpMotorBlower_LogClass;

public class RunningPumpMotorAdapter extends RecyclerView.Adapter<RunningPumpMotorAdapter.Viewholder> {

    private List<PumpMotorBlower_LogClass> pumpMotorDailyLogClass;
    private Context context;

    public RunningPumpMotorAdapter(List<PumpMotorBlower_LogClass> pumpMotorDailyLogClass, Context context) {
        this.pumpMotorDailyLogClass = pumpMotorDailyLogClass;
        this.context = context;
    }
    @NonNull
    @Override
    public RunningPumpMotorAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pumpmotordetails_start_log, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RunningPumpMotorAdapter.Viewholder holder, int position) {

        holder.Pumpeqipname.setText(pumpMotorDailyLogClass.get(position).getEquip_name());
        holder.Pumpstatedtime.setText(pumpMotorDailyLogClass.get(position).getStart_time());
        holder.Pumprunningtime.setText(pumpMotorDailyLogClass.get(position).getRunning_time());

        holder.Pump_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
                StopRolloverMotorPumps(position,1);
                Intent pump = new Intent(context, PumpMoterDailyLogActivity.class);
                context.startActivity(pump);
            }
        });

        holder.Pump_Reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopRolloverMotorPumps(position,2);
                ((Activity) context).finish();
                Intent pump = new Intent(context, PumpMoterDailyLogActivity.class);
                context.startActivity(pump);
            }
        });

    }

    private void StopRolloverMotorPumps(int position,int type) {
        RequestQueue queue = Volley.newRequestQueue(context);
        // url
        String MotorPumpsUrl = "";

        switch (type){
            case 2:
                MotorPumpsUrl = Global.RolloverMotorPumpsUrl;
                break;
            case 1:
                MotorPumpsUrl = Global.StopMotorPumpsUrl;
                break;
            default:
                MotorPumpsUrl="";
                break;
        }


        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String dlog_date = Global.sharedPreferences.getString("dlogdate", "0");
        String ayear = Global.sharedPreferences.getString("ayear", "0");
        String tstp2_code = Global.RunningPumpsMotors_LogClass.get(position).get_tstp2_code();

        MotorPumpsUrl = MotorPumpsUrl + "comcode=" + com_code + "&sstp1_code=" + sstp1_code + "&dlog_date=" + dlog_date + "&tstp2_code=" + tstp2_code + "&ayear=" + ayear;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MotorPumpsUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(context, "Rollover Motor Pumps", Toast.LENGTH_LONG).show();
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
                params.put("com_code", Global.sharedPreferences.getString("com_code", null));
                params.put("sstp1_code", Global.sharedPreferences.getString("sstp1_code", null));
                params.put("dlogdate", Global.sharedPreferences.getString("dlogdate", null));
                params.put("ayear", Global.sharedPreferences.getString("ayear", null));
                params.put("tstp2_code", Global.RunningPumpsMotors_LogClass.get(position).get_tstp2_code());
                return params;
            }

        };

        queue.add(jsonObjectRequest);

    }

    @Override
    public int getItemCount() {
        return pumpMotorDailyLogClass.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView Pumpeqipname, Pumpstatedtime, Pumprunningtime;
        ImageView Pump_Reload, Pump_Stop;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Pumpeqipname = itemView.findViewById(R.id.pump_equip_name);
            Pumpstatedtime = itemView.findViewById(R.id.pumpmotor_start_time);
            Pumprunningtime = itemView.findViewById(R.id.pump_running_time);
            Pump_Reload = itemView.findViewById(R.id.pump_reload);
            Pump_Stop = itemView.findViewById(R.id.pump_stop);
        }
    }

/*
    private void RunningPump_stop(int position) {
        RequestQueue queue = Volley.newRequestQueue(context);
        // url
        String stopRunningPump = Global.GetStopMotorPumps;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String dlog_date = Global.sharedPreferences.getString("dlogdate", "0");
        String ayear = Global.sharedPreferences.getString("ayear", "0");
        String tstp2_code = Global.RunningPumpsMotors_LogClass.get(position).get_tstp2_code();

        stopRunningPump = stopRunningPump + "comcode=" + com_code + "&sstp1_code=" + sstp1_code + "&dlog_date=" + dlog_date + "&tstp2_code=" + tstp2_code + "&ayear=" + ayear;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, stopRunningPump, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Pump Stopped", Toast.LENGTH_LONG).show();
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
                params.put("com_code", Global.sharedPreferences.getString("com_code", null));
                params.put("sstp1_code", Global.sharedPreferences.getString("sstp1_code", null));
                params.put("dlogdate", Global.sharedPreferences.getString("dlogdate", null));
                params.put("ayear", Global.sharedPreferences.getString("ayear", null));
                params.put("tstp2_code", Global.RunningPumpsMotors_LogClass.get(position).get_tstp2_code());
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }*/


}
