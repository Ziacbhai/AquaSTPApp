package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.R;

import java.util.List;

import Models.PumpMotor_Blower_DailyLogClass;

public class PumpMoterDailyLogStopAdapter extends RecyclerView.Adapter<PumpMoterDailyLogStopAdapter.Viewholder> {


    private List<PumpMotor_Blower_DailyLogClass> pumpMotorDailyLogClass;
    private LayoutInflater inflater;
    private boolean isPlaying = false;

    public PumpMoterDailyLogStopAdapter(List<PumpMotor_Blower_DailyLogClass> pumpMotorDailyLogClass) {
        this.pumpMotorDailyLogClass = pumpMotorDailyLogClass;

    }
    @NonNull
    @Override
    public PumpMoterDailyLogStopAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pumpmotordetails_start_log,parent,false);
        return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PumpMoterDailyLogStopAdapter.Viewholder holder, int position) {
       // PumpMotor_Blower_DailyLogClass currentItem = pumpMotorDailyLogClass.get(position);
        holder.Pumpeqipname.setText(pumpMotorDailyLogClass.get(position).getEquip_name());
        holder.Pumpstatedtime.setText(pumpMotorDailyLogClass.get(position).getStart_time());
        holder.Pumprunningtime.setText(pumpMotorDailyLogClass.get(position).getRunning_time());

       /* holder.Pump_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the corresponding item in PumpMoterDailyLogStartAdapter to hide views
                currentItem.setHideViews(true);
                togglePlayPause(holder.Pump_start);
                // Notify PumpMoterDailyLogStartAdapter that the data has changed
                notifyDataSetChanged();
            }
        });*/
    }

    private void togglePlayPause(ImageView imageView) {

        if (isPlaying) {
            imageView.setImageResource(R.drawable.play);
        }
        imageView.setVisibility(isPlaying ? View.VISIBLE : View.INVISIBLE);

        // Toggle the state
        isPlaying = !isPlaying;
    }

    @Override
    public int getItemCount() {
        return  pumpMotorDailyLogClass.size();
    }
    public class Viewholder extends RecyclerView.ViewHolder {
        TextView Pumpeqipname,Pumpstatedtime,Pumprunningtime;
        ImageView Pump_start,Pump_stop;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Pumpeqipname = itemView.findViewById(R.id.pump_equip_name);
            Pumpstatedtime = itemView.findViewById(R.id.pumpmotor_start_time);
            Pumprunningtime = itemView.findViewById(R.id.pump_running_time);
            Pump_start = itemView.findViewById(R.id.pump_reload);
            Pump_stop = itemView.findViewById(R.id.pump_stop);
        }
    }
}
