package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    }

    @Override
    public int getItemCount() {
        return browersDailyLogClass.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
