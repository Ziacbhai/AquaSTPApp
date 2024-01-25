package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.R;

import java.util.List;

import Models.SensorsModelClass;

public class SensorDailyLogAdapter extends RecyclerView.Adapter<SensorDailyLogAdapter.Viewholder> {


    private List<SensorsModelClass> sensorsModelClassList;
    Context context;

    public SensorDailyLogAdapter(List<SensorsModelClass> sensorsModelClassList) {
        this.sensorsModelClassList = sensorsModelClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public SensorDailyLogAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensors_daily_log_deatils, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorDailyLogAdapter.Viewholder holder, int position) {
        holder.Sensor_equip_name.setText(sensorsModelClassList.get(position).getEquip_name());
        holder.Sensor_reading.setText(sensorsModelClassList.get(position).getReading());
        holder.Sensor_reading_time.setText(sensorsModelClassList.get(position).getReading_time());
        holder.Sensor_total.setText(sensorsModelClassList.get(position).getSensor_total());


      /* holder.Sensor_image_upload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });*/

    }

    @Override
    public int getItemCount() {
        return sensorsModelClassList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView Sensor_image,Sensor_image_upload;

        TextView Sensor_equip_name, Sensor_reading, Sensor_reading_time, Sensor_total;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

           /* Sensor_equip_name = itemView.findViewById(R.id.sensor_equip_name);
            Sensor_reading = itemView.findViewById(R.id.sensor_reading);
            Sensor_reading_time = itemView.findViewById(R.id.sensor_reading_time);
            Sensor_total = itemView.findViewById(R.id.sensor_total);*/
           // Sensor_image = itemView.findViewById(R.id.sensor_image);
           // Sensor_image_upload = itemView.findViewById(R.id.sensor_image_upload);
        }
    }
}
