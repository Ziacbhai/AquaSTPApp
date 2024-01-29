package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.ziac.aquastpapp.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.MetersDailyLogClass;

public class MeterDailyLogEditAdapter extends RecyclerView.Adapter<MeterDailyLogEditAdapter.ViewHolder> {

    private List<MetersDailyLogClass> metersDailyLogClass;
    Context context;

    public MeterDailyLogEditAdapter(List<MetersDailyLogClass> metersDailyLogClass) {
        this.metersDailyLogClass = metersDailyLogClass;
        this.context = context;
    }

    @NonNull
    @Override
    public MeterDailyLogEditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meter_daily_log_edit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeterDailyLogEditAdapter.ViewHolder holder, int position) {

        holder.Meter_equip_name.setText(metersDailyLogClass.get(position).getMeters_equip_name());
        holder.Meter_reading_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Meter_reading = metersDailyLogClass.get(position).getMeters_reading_edit();
               ;
            }
        });

        holder.Meter_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.Meters_Class.get(position).getMeters_reading_edit();
                DailyLogMeters(position);
            }
        });

    }


    private void DailyLogMeters(int position) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String MeterUrl = Global.DailyLogUpdateMeterReadings;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MeterUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Meters_Update", Toast.LENGTH_LONG).show();
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
                Map<String, String> params = new HashMap<>();
                params.put("com_code", Global.sharedPreferences.getString("com_code", null));
                params.put("ayear", Global.sharedPreferences.getString("ayear", null));
                params.put("tstp3_code", Global.Meters_Class.get(position).getTstp3_code());
                params.put("reading_value", Global.Meters_Class.get(position).getMeters_reading_edit());
                return params;
            }
        };
        queue.add(jsonObjectRequest);

    }

    @Override
    public int getItemCount() {
        return metersDailyLogClass.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Meter_equip_name, Meter_save;
        EditText Meter_reading_edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Meter_equip_name = itemView.findViewById(R.id.meter_equip_name);
            Meter_reading_edit = itemView.findViewById(R.id.meter_reading_edit);
            Meter_save = itemView.findViewById(R.id.meter_save);
        }
    }
}
