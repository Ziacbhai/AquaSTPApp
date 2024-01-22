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

import Models.PumpMotor_Blower_DailyLogClass;

public class PumpMoterDailyLogStartAdapter extends RecyclerView.Adapter<PumpMoterDailyLogStartAdapter.Viewholder> {

    private List<PumpMotor_Blower_DailyLogClass> pumpMotorDailyLogClass;
    Content content;

    public PumpMoterDailyLogStartAdapter(List<PumpMotor_Blower_DailyLogClass> pumpMotorDailyLogClass) {
        this.pumpMotorDailyLogClass = pumpMotorDailyLogClass;
        this.content = content;
    }

    @NonNull
    @Override
    public PumpMoterDailyLogStartAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.motor_pump_details_stoped_log,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PumpMoterDailyLogStartAdapter.Viewholder holder, int position) {
        holder.Pumpeqipname.setText(pumpMotorDailyLogClass.get(position).getEquip_name());
        holder.Pumpstoptime.setText(pumpMotorDailyLogClass.get(position).getEnd_time());
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
            Pumpstoptime = itemView.findViewById(R.id.pumpmotor_end_time);
            Pumprunningtime = itemView.findViewById(R.id.pumpmotor_running_time);
        }
    }
}
