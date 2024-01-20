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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meter_daily_log_edit,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeterDailyLogEditAdapter.ViewHolder holder, int position) {

        holder.Meter_equip_name.setText(metersDailyLogClass.get(position).getMeters_equip_name());
        holder.Meter_equip_name.setText(metersDailyLogClass.get(position).getMeters_reading_edit());

    }

    @Override
    public int getItemCount() {
        return metersDailyLogClass.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Meter_equip_name;
        EditText Meter_reading_edit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Meter_equip_name = itemView.findViewById(R.id.meter_equip_name);
            Meter_reading_edit = itemView.findViewById(R.id.meter_reading_edit);
        }
    }
}
