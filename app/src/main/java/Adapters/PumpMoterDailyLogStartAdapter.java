package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
import com.ziac.aquastpapp.R;

import java.util.List;

import Models.PumpMotor_Blower_DailyLogClass;

public class PumpMoterDailyLogStartAdapter extends RecyclerView.Adapter<PumpMoterDailyLogStartAdapter.Viewholder> {


    private List<PumpMotor_Blower_DailyLogClass> pumpMotorDailyLogClass;
    Content content;

    public PumpMoterDailyLogStartAdapter(Context context, List<PumpMotor_Blower_DailyLogClass> pumpMotorDailyLogClass) {
        this.pumpMotorDailyLogClass = pumpMotorDailyLogClass;
        this.content = content;
    }
    @NonNull
    @Override
    public PumpMoterDailyLogStartAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pumpmotordetails_started_log,parent,false);
        return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PumpMoterDailyLogStartAdapter.Viewholder holder, int position) {

        holder.Pumpeqipname.setText(pumpMotorDailyLogClass.get(position).getEquip_name());
        holder.Pumpstarttime.setText(pumpMotorDailyLogClass.get(position).getStart_time());
        holder.Pumprunningtime.setText(pumpMotorDailyLogClass.get(position).getRunning_time());
    }
    @Override
    public int getItemCount() {
        return  pumpMotorDailyLogClass.size();
    }
    public class Viewholder extends RecyclerView.ViewHolder {
        TextView Pumpeqipname,Pumpstarttime,Pumprunningtime;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Pumpeqipname = itemView.findViewById(R.id.pumpmotor_equip_name);
            Pumpstarttime = itemView.findViewById(R.id.pumpmotor_start_time);
            Pumprunningtime = itemView.findViewById(R.id.pumpmotor_running_time);
        }
    }
}
