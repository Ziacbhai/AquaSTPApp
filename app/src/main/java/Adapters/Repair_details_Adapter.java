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

import Models.RepairsClass;

public class Repair_details_Adapter extends RecyclerView.Adapter<Repair_details_Adapter.Viewholder> {

    private List<RepairsClass> repairsClassList;
    Context context;

    public Repair_details_Adapter(List<RepairsClass> repairsClassList, Context context) {
        this.repairsClassList = repairsClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public Repair_details_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repair_details_design, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Repair_details_Adapter.Viewholder holder, int position) {

        holder.Eq_Name.setText(repairsClassList.get(position).getD_Equipment_Name());
        holder.Eq_number.setText(repairsClassList.get(position).getD_Equipment_Number());
        holder.Repaired.setText(repairsClassList.get(position).getD_Repaired());
        holder.Remark.setText(repairsClassList.get(position).getD_Remark());
        holder.Amount.setText(repairsClassList.get(position).getD_Amount());

    }

    @Override
    public int getItemCount() {
        return repairsClassList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView Eq_Name,Eq_number,Repaired,Remark,Amount;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Eq_Name = itemView.findViewById(R.id.repair_equipment_name);
            Eq_number = itemView.findViewById(R.id.repair_equipment_number);
            Repaired = itemView.findViewById(R.id.repair_repaired);
            Remark = itemView.findViewById(R.id.repair_remark);
            Amount = itemView.findViewById(R.id.repair_amount);

        }
    }
}
