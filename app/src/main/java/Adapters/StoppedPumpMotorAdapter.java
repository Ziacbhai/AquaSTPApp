package Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ziac.aquastpapp.Activities.PumpMotorDailyLogActivity;
import com.ziac.aquastpapp.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.PumpMotorBlower_LogClass;

public class StoppedPumpMotorAdapter extends RecyclerView.Adapter<StoppedPumpMotorAdapter.Viewholder> {

    private List<PumpMotorBlower_LogClass> pumpMotorDailyLogClass;
    Context context;
    public StoppedPumpMotorAdapter(List<PumpMotorBlower_LogClass> pumpMotorDailyLogClass, Context context) {
        this.pumpMotorDailyLogClass = pumpMotorDailyLogClass;
        this.context = context;
    }
    @NonNull
    @Override
    public StoppedPumpMotorAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pumpmotordetails_stopped_log, parent, false);
        return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull StoppedPumpMotorAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.Pumpeqipname.setText(pumpMotorDailyLogClass.get(position).getEquip_name());
        holder.Pumpstoptime.setText(pumpMotorDailyLogClass.get(position).getEnd_time());
        holder.Pumprunningtime.setText(pumpMotorDailyLogClass.get(position).getRunning_time());

        holder.Pump_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_motor_pump(position);
                //pumpMotorDailyLogClass.clear();
            }
        });

// Check if running_status is "S" and hide Pump_start
        if (pumpMotorDailyLogClass.size() > position && pumpMotorDailyLogClass.get(position).getRunning_status().equals("S")) {
            holder.Pump_start.setVisibility(View.GONE);
        } else {
            holder.Pump_start.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return pumpMotorDailyLogClass.size();
    }
    private void start_motor_pump(int position) {
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        // url
        String startMotorPump = Global.StartMotorPumpsUrl;
        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String dlog_date = Global.sharedPreferences.getString("dlogdate", "0");
        String ayear = Global.sharedPreferences.getString("ayear", "0");
        String tstp2_code = Global.StoppedPumpsMotors_LogClass.get(position).get_tstp2_code();
        startMotorPump = startMotorPump + "comcode=" + com_code + "&sstp1_code=" + sstp1_code + "&dlog_date=" + dlog_date + "&tstp2_code=" + tstp2_code + "&ayear=" + ayear;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, startMotorPump, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent pump = new Intent(context, PumpMotorDailyLogActivity.class);
                context.startActivity(pump);
                ((Activity) context).finish();
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

            @Override
            protected Map<String, String> getParams() {
                // If you have any parameters to send in the request body, you can set them here
                Map<String, String> params = new HashMap<>();
                params.put("com_code", Global.sharedPreferences.getString("com_code", null));
                params.put("sstp1_code", Global.sharedPreferences.getString("sstp1_code", null));
                params.put("dlogdate", Global.sharedPreferences.getString("dlogdate", null));
                params.put("ayear", Global.sharedPreferences.getString("ayear", null));
                params.put("tstp2_code", Global.StoppedPumpsMotors_LogClass.get(position).get_tstp2_code());
                //params.put("running_status", Global.StoppedPumpsMotors_LogClass.get(position).getRunning_status());
                return params;
            }
        };

        queue.add(jsonObjectRequest);
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        TextView Pumpeqipname, Pumprunningtime, Pumpstoptime;
        ImageView Pump_start;
        View viewpump;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Pumpeqipname = itemView.findViewById(R.id.pumpmotor_equip_name);
            Pumpstoptime = itemView.findViewById(R.id.pumpmotor_end_time);
            Pumprunningtime = itemView.findViewById(R.id.pumpmotor_running_time);
            Pump_start = itemView.findViewById(R.id.pump_start);
            viewpump = itemView.findViewById(R.id.vipump);

            String usertype=Global.sharedPreferences.getString("user_type","");
            if (usertype.equals("C")){
                viewpump.setVisibility(View.GONE);

            }else {
                viewpump.setVisibility(View.VISIBLE);
                ;
            }

        }
    }
}
