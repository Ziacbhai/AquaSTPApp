package Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.Activities.MeterDailyLogActivity;
import com.ziac.aquastpapp.Activities.SensorsDailyLogActivity;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Models.SensorsModelClass;

public class SensorDailyLogEditAdapter extends RecyclerView.Adapter<SensorDailyLogEditAdapter.Viewholder> {

    List<SensorsModelClass> sensorsModelClasses;
    Context context;
    String enteredValue;

    public SensorDailyLogEditAdapter(List<SensorsModelClass> sensorsModelClasses, Context context) {
        this.sensorsModelClasses = sensorsModelClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public SensorDailyLogEditAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensors_daily_log_edit, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorDailyLogEditAdapter.Viewholder holder, int position) {

        holder.Sensor_equip_name.setText(sensorsModelClasses.get(position).getEquip_name());
        holder.Sensor_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredValue = holder.Sensor_reading_edit.getText().toString();
                if (enteredValue.isEmpty()) {
                    Toast.makeText(context, "Please enter the reading value", Toast.LENGTH_LONG).show();
                } else {
                    DailyLogSensors(enteredValue, position);
                }
            }
        });

    }

    private void DailyLogSensors(String enteredValue, int position) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Global.DailyLogUpdateSensorsReadings;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String error = jsonObject.getString("error");

                    if (success) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, SensorsDailyLogActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
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
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("com_code", Global.sharedPreferences.getString("com_code", null));
                params.put("ayear", Global.sharedPreferences.getString("ayear", null));
                params.put("tstp6_code", sensorsModelClasses.get(position).getSensor_tstp6_code());
                params.put("reading_value", enteredValue);
                System.out.println(params);
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

    }

    @Override
    public int getItemCount() {
        return sensorsModelClasses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        public TextView Sensor_equip_name;
        public MaterialButton Sensor_save;
        public EditText Sensor_reading_edit;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Sensor_equip_name = itemView.findViewById(R.id.sensor_name);
            Sensor_reading_edit = itemView.findViewById(R.id.sensor_reading_edit);
            Sensor_save = itemView.findViewById(R.id.sensor_save);
        }
    }
}
