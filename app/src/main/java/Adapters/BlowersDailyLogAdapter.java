package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.R;

import java.util.List;

import Models.PumpMotor_Blower_DailyLogClass;

public class BlowersDailyLogAdapter extends RecyclerView.Adapter<BlowersDailyLogAdapter.Viewholder> {
    private List<PumpMotor_Blower_DailyLogClass> browersDailyLogClass;
    Context context;

    public BlowersDailyLogAdapter(List<PumpMotor_Blower_DailyLogClass> browersDailyLogClass) {
        this.browersDailyLogClass = browersDailyLogClass;
        this.context = context;
    }

    @NonNull
    @Override
    public BlowersDailyLogAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blowers_details_daily_log,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlowersDailyLogAdapter.Viewholder holder, int position) {
        holder.Blower_equip_name.setText(browersDailyLogClass.get(position).getEquip_name());
        holder.Blower_start_time.setText(browersDailyLogClass.get(position).getStart_time());
        holder.Blower_stop_time.setText(browersDailyLogClass.get(position).getRunning_time());
        holder.Blower_running_time.setText(browersDailyLogClass.get(position).getRunning_time());
        holder.Blower_pause.setText(browersDailyLogClass.get(position).getRunning_time());
    }

    @Override
    public int getItemCount() {
        return browersDailyLogClass.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView Blower_equip_name,Blower_start_time,Blower_stop_time,Blower_running_time,Blower_pause;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Blower_equip_name = itemView.findViewById(R.id.blower_equip_name);
            Blower_start_time = itemView.findViewById(R.id.blower_start_time);
            Blower_stop_time = itemView.findViewById(R.id.blower_stop_time);
            Blower_running_time = itemView.findViewById(R.id.blower_running_time);
            Blower_pause = itemView.findViewById(R.id.blower_pause);
        }
    }
}
