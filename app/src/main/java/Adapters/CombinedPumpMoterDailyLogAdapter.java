package Adapters;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.R;

import java.util.List;

import Models.PumpMotor_Blower_DailyLogClass;

public class CombinedPumpMoterDailyLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_START = 1;
    private static final int VIEW_TYPE_STOP = 2;

    private List<PumpMotor_Blower_DailyLogClass> pumpMotorDailyLogClass;

    public CombinedPumpMoterDailyLogAdapter(List<PumpMotor_Blower_DailyLogClass> pumpMotorDailyLogClass) {
        this.pumpMotorDailyLogClass = pumpMotorDailyLogClass;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_START) {
            View startView = inflater.inflate(R.layout.motor_pump_details_stoped_log, parent, false);
            return new StartViewHolder(startView);
        } else if (viewType == VIEW_TYPE_STOP) {
            View stopView = inflater.inflate(R.layout.pumpmotordetails_start_log, parent, false);
            return new StopViewHolder(stopView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        if (viewType == VIEW_TYPE_START) {
            ((StartViewHolder) holder).bindStartView(pumpMotorDailyLogClass.get(position));
        } else if (viewType == VIEW_TYPE_STOP) {
            ((StopViewHolder) holder).bindStopView(pumpMotorDailyLogClass.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return pumpMotorDailyLogClass.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Use your logic to determine the view type (start or stop)
        return pumpMotorDailyLogClass.get(position).isHideViews() ? VIEW_TYPE_START : VIEW_TYPE_STOP;
    }


    public class StartViewHolder extends RecyclerView.ViewHolder {
        TextView Pumpeqipname, Pumpstoptime, Pumprunningtime;
        ImageView Pump_stop;

        public StartViewHolder(@NonNull View itemView) {
            super(itemView);
            Pumpeqipname = itemView.findViewById(R.id.pumpmotor_equip_name);
            Pumpstoptime = itemView.findViewById(R.id.pumpmotor_end_time);
            Pumprunningtime = itemView.findViewById(R.id.pumpmotor_running_time);
            Pump_stop = itemView.findViewById(R.id.pump_stop);
        }

        public void bindStartView(PumpMotor_Blower_DailyLogClass item) {

            Pumpeqipname.setText(item.getEquip_name());
            Pumpstoptime.setText(item.getEnd_time());
            Pumprunningtime.setText(item.getRunning_time());

            itemView.setVisibility(item.isStartViewVisible() ? View.VISIBLE : View.GONE);

            Pump_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toggle the visibility state when Pump_stop is clicked
                    item.setStartViewVisible(false);
                    itemView.setVisibility(View.GONE);
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }

    // ViewHolder for Stop View
    public class StopViewHolder extends RecyclerView.ViewHolder {
        TextView Pumpeqipname, Pumpstarttime, Pumprunningtime;
        ImageView Pump_start;

        public StopViewHolder(@NonNull View itemView) {
            super(itemView);
            Pumpeqipname = itemView.findViewById(R.id.pumpmotor_equip_name);
            Pumpstarttime = itemView.findViewById(R.id.pumpmotor_start_time);
            Pumprunningtime = itemView.findViewById(R.id.pumpmotor_running_time);
            Pump_start = itemView.findViewById(R.id.pump_start);
        }

        public void bindStopView(PumpMotor_Blower_DailyLogClass item) {
            PumpMotor_Blower_DailyLogClass currentItem = pumpMotorDailyLogClass.get(getAdapterPosition());
            Pumpeqipname.setText(item.getEquip_name());
            Pumpstarttime.setText(item.getStart_time());
            Pumprunningtime.setText(item.getRunning_time());

            Pump_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentItem.setStartViewVisible(true);
                    notifyDataSetChanged();
                }
            });

            // Add any additional logic specific to the stop view
        }
    }
}

