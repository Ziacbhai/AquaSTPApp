package Adapters;

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
import com.ziac.aquastpapp.Activities.BlowersDailyLogActivity;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.PumpMotorBlower_LogClass;

public class BlowersDailyLogStartAdapter extends RecyclerView.Adapter<BlowersDailyLogStartAdapter.Viewholder> {
    Context context;
    private List<PumpMotorBlower_LogClass> browersDailyLogClass;

    public BlowersDailyLogStartAdapter(Context context, List<PumpMotorBlower_LogClass> browersDailyLogClass) {
        this.context = context;
        this.browersDailyLogClass = browersDailyLogClass;
    }

    @NonNull
    @Override
    public BlowersDailyLogStartAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blowers_details_daily_log,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlowersDailyLogStartAdapter.Viewholder holder, int position) {
        holder.Blower_equip_name.setText(browersDailyLogClass.get(position).getEquip_name());
        holder.Blower_start_time.setText(browersDailyLogClass.get(position).getStart_time());
        holder.Blower_stop_time.setText(browersDailyLogClass.get(position).getEnd_time());
        holder.Blower_running_time.setText(browersDailyLogClass.get(position).getRunning_time());


        holder.Blower_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Blower Response", Toast.LENGTH_SHORT).show();
                start_blower(position, holder);
                browersDailyLogClass.clear();
            }
        });

        if (browersDailyLogClass.size() > position && browersDailyLogClass.get(position).getRunning_status().equals("S")) {
            holder.Blower_start.setVisibility(View.GONE);
        } else {
            holder.Blower_start.setVisibility(View.VISIBLE);
        }

    }

    private void start_blower(int position, Viewholder holder) {

        RequestQueue queue = Volley.newRequestQueue(context);
        // url
        String startBlower = Global.GetStartBlower;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String dlog_date = Global.sharedPreferences.getString("dlogdate", "0");
        String ayear = Global.sharedPreferences.getString("ayear", "0");
        String tstp5_code = Global.Start_Blower_LogClass.get(position).getTstp5_code();

        startBlower = startBlower + "comcode=" + com_code + "&sstp1_code=" + sstp1_code + "&dlog_date=" + dlog_date + "&tstp5_code=" + tstp5_code + "&ayear=" + ayear;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, startBlower, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent blower = new Intent(context, BlowersDailyLogActivity.class);
                context.startActivity(blower);
                ((Activity) context).finish();
                Toast.makeText(context, "Blower Response", Toast.LENGTH_SHORT).show();


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
        }){

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
                params.put("tstp5_code", Global.Start_Blower_LogClass.get(position).getTstp5_code());
                //params.put("running_status", Global.StoppedPumpsMotors_LogClass.get(position).getRunning_status());
                return params;
            }
        };

        queue.add(jsonObjectRequest);

    }

    @Override
    public int getItemCount() {
        return browersDailyLogClass.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView Blower_equip_name,Blower_start_time,Blower_stop_time,Blower_running_time;

        ImageView Blower_start,Blower_rollover,Blower_stop;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Blower_equip_name = itemView.findViewById(R.id.blower_equip_name);
            Blower_start_time = itemView.findViewById(R.id.blower_start_time);
            Blower_stop_time = itemView.findViewById(R.id.blower_stop_time);
            Blower_running_time = itemView.findViewById(R.id.blower_running_time);
            Blower_start = itemView.findViewById(R.id.blower_start);

        }
    }
}
