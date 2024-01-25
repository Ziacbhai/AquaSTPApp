package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.R;

import java.util.List;

import Models.SensorsModelClass;

public class SensorDailyLogEditAdapter extends RecyclerView.Adapter<SensorDailyLogEditAdapter.Viewholder> {

    private List<SensorsModelClass> sensorsModelClasses;
    private  Context context;


    public SensorDailyLogEditAdapter(List<SensorsModelClass> sensorsModelClasses) {
        this.sensorsModelClasses = sensorsModelClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public SensorDailyLogEditAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensors_daily_log_edit,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorDailyLogEditAdapter.Viewholder holder, int position) {

        holder.Sensor_equip_name.setText(sensorsModelClasses.get(position).getEquip_name());
        //holder.Sensor_reading_edit.setText(sensorsModelClasses.get(position).getReading_time());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView Sensor_equip_name;
        EditText Sensor_reading_edit;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Sensor_equip_name = itemView.findViewById(R.id.sensor_equip_name);
           // Sensor_reading_edit = itemView.findViewById(R.id.sensor_reading_edit);
        }
    }
}
