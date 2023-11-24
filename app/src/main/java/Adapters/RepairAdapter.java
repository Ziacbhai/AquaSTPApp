package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.R;

import java.util.ArrayList;
import java.util.List;

import Models.CommonModelClass;
import Models.RepairsClass;

public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.Viewholder> {
    private List<RepairsClass> repairS;
    Context context;


    public RepairAdapter(List<RepairsClass> repairS, Context context) {
        this.repairS = repairS;
        this.context = context;
    }

    @NonNull
    @Override
    public RepairAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repair_details, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepairAdapter.Viewholder holder, int position) {
        holder.Repno.setText(repairS.get(position).getREPNo());
        holder.Amount.setText(repairS.get(position).getRepair_Amount());
        holder.RepairDate.setText(repairS.get(position).getRepair_Date());
    }
    @Override
    public int getItemCount() {
        return repairS.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView  Repno,Amount,RepairDate;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Repno=itemView.findViewById(R.id.repno_);
            Amount=itemView.findViewById(R.id.amount_);
            RepairDate=itemView.findViewById(R.id.repair_date);

        }
    }
}
