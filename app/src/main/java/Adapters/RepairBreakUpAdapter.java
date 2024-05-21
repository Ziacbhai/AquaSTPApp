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

import Models.RepairModel3;


public class RepairBreakUpAdapter extends RecyclerView.Adapter<RepairBreakUpAdapter.Viewholder> {

    List<RepairModel3> repairModel3;
    Context context;

    public RepairBreakUpAdapter(List<RepairModel3> repairModel3, Context context) {
        this.repairModel3 = repairModel3;
        this.context = context;
    }

    @NonNull
    @Override
    public RepairBreakUpAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repair_breakup_design, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepairBreakUpAdapter.Viewholder holder, int position) {
        holder.Breakup_Item_name.setText(repairModel3.get(position).getRepair_Breakup_Item_name());
        holder.Breakup_qty.setText(repairModel3.get(position).getRepair_Breakup_Qty() + 0);
        holder.Breakup_price.setText(repairModel3.get(position).getRepair_Breakup_Price() + 0);
        holder.Breakup_total.setText(repairModel3.get(position).getRepair_Breakup_amount() + 0);
        holder.Breakup_unit.setText(repairModel3.get(position).getRepair_Breakup_Unit());
        holder.Breakup_remark.setText(repairModel3.get(position).getRepair_Breakup_Remark());
    }

    @Override
    public int getItemCount() {
        return repairModel3.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView Breakup_Item_name, Breakup_qty, Breakup_price, Breakup_total, Breakup_unit, Breakup_remark;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Breakup_Item_name = itemView.findViewById(R.id.breakup_item);
            Breakup_qty = itemView.findViewById(R.id.breakup_qty);
            Breakup_price = itemView.findViewById(R.id.breakup_price);
            Breakup_total = itemView.findViewById(R.id.breakup_total);
            Breakup_unit = itemView.findViewById(R.id.breakup_unit);
            Breakup_remark = itemView.findViewById(R.id.breakup_remark);
        }
    }
}
