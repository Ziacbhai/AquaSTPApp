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

import Models.MetersDailyLogModel;

public class MeterDailyLogAdapter extends RecyclerView.Adapter<MeterDailyLogAdapter.ViewHolder> {
    private List<MetersDailyLogModel> metersDailyLogModels;
    Context context;

    public MeterDailyLogAdapter(List<MetersDailyLogModel> metersDailyLogModels, Context context) {
        this.metersDailyLogModels = metersDailyLogModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MeterDailyLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meter_daily_log_deatils, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeterDailyLogAdapter.ViewHolder holder, int position) {


        holder.Meter_equip_name.setText(metersDailyLogModels.get(position).getMeters_equip_name());
        holder.Meter_reading_time.setText(metersDailyLogModels.get(position).getMeters_reading_time());
        holder.Meter_total.setText(metersDailyLogModels.get(position).getMeters_total());

        double rawValue = Double.parseDouble(metersDailyLogModels.get(position).getMeters_reading_edit());
        String formattedValue = removeTrailingZeros(rawValue);
        holder.Meter_reading_edit.setText(formattedValue);

        double rawValue1 = Double.parseDouble(metersDailyLogModels.get(position).getMeters_total());
        String formattedValue1 = removeTrailingZeros(rawValue1);
        holder.Meter_total.setText(formattedValue1);


    }

    private String removeTrailingZeros(double value) {
        String stringValue = String.valueOf(value);
        stringValue = stringValue.replaceAll("0*$", "").replaceAll("\\.$", "");
        return stringValue;
    }

    @Override
    public int getItemCount() {
        return metersDailyLogModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Meter_equip_name, Meter_reading_edit, Meter_reading_time, Meter_total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Meter_equip_name = itemView.findViewById(R.id.meter_name);
            Meter_reading_edit = itemView.findViewById(R.id.meter_reading);
            Meter_reading_time = itemView.findViewById(R.id.meter_reading_time);
            Meter_total = itemView.findViewById(R.id.meter_total);
        }
    }
}
