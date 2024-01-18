package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
import com.ziac.aquastpapp.R;

import java.util.List;

import Models.PumpMotorDailyLogClass;

public class PumpMoterDailyLogStopAdapter extends RecyclerView.Adapter<PumpMoterDailyLogStopAdapter.Viewholder> {

    private List<PumpMotorDailyLogClass> pumpMotorDailyLogClass;
    Content content;

    public PumpMoterDailyLogStopAdapter(List<PumpMotorDailyLogClass> pumpMotorDailyLogClass) {
        this.pumpMotorDailyLogClass = pumpMotorDailyLogClass;
        this.content = content;
    }

    @NonNull
    @Override
    public PumpMoterDailyLogStopAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.motor_pump_details_stopped_log,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PumpMoterDailyLogStopAdapter.Viewholder holder, int position) {
        holder.Pumpeqipname.setText(pumpMotorDailyLogClass.get(position).getEquip_name());
        holder.Pumpstoptime.setText(pumpMotorDailyLogClass.get(position).getStart_time());
        holder.Pumprunningtime.setText(pumpMotorDailyLogClass.get(position).getRunning_time());
    }

    @Override
    public int getItemCount() {
        return pumpMotorDailyLogClass.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView Pumpeqipname,Pumprunningtime,Pumpstoptime;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Pumpeqipname = itemView.findViewById(R.id.pumpmotor_equip_name);
            Pumpstoptime = itemView.findViewById(R.id.pumpmotor_stop_time);
            Pumprunningtime = itemView.findViewById(R.id.pumpmotor_running_time);
        }
    }
}
