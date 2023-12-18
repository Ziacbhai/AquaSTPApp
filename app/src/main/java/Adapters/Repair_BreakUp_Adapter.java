package Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.R;

import java.util.List;

import Models.CommonModelClass;
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Repair_BreakUp_Adapter.Viewholder holder, int position) {

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
