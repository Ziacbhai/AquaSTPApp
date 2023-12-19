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

public class Repair_BreakUp_Adapter extends RecyclerView.Adapter<Repair_BreakUp_Adapter.Viewholder> {

    List<RepairsClass> repairsClasses;
    Context context;

    public Repair_BreakUp_Adapter(List<RepairsClass> repairsClasses, Context context) {
        this.repairsClasses = repairsClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public Repair_BreakUp_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repair_breakup_design, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Repair_BreakUp_Adapter.Viewholder holder, int position) {
        holder.Breakup_Item.setText(repairsClasses.get(position).getRepair_Breakup_Item());
        holder.Breakup_qty.setText(repairsClasses.get(position).getRepair_Breakup_Qty());
        holder.Breakup_price.setText(repairsClasses.get(position).getRepair_Breakup_Price());
        holder.Breakup_total.setText(repairsClasses.get(position).getRepair_Breakup_amount());
        holder.Breakup_unit.setText(repairsClasses.get(position).getRepair_Breakup_Unit());
        holder.Breakup_remark.setText(repairsClasses.get(position).getRepair_Breakup_Remark());
    }
    @Override
    public int getItemCount() {
        return repairsClasses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView Breakup_Item,Breakup_qty,Breakup_price,Breakup_total ,Breakup_unit,Breakup_remark;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Breakup_Item = itemView.findViewById(R.id.breakup_item);
            Breakup_qty = itemView.findViewById(R.id.breakup_qty);
            Breakup_price = itemView.findViewById(R.id.breakup_price);
            Breakup_total = itemView.findViewById(R.id.breakup_total);
            Breakup_unit = itemView.findViewById(R.id.breakup_unit);
            Breakup_remark = itemView.findViewById(R.id.breakup_remark);
        }
    }
}
