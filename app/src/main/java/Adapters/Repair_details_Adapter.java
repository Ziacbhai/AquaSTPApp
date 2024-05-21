package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.Activities.RepairBreakUpActivity;
import com.ziac.aquastpapp.Activities.RepairTwoImageListActivity;
import com.ziac.aquastpapp.R;

import java.util.List;

import Models.RepairModel2;

public class Repair_details_Adapter extends RecyclerView.Adapter<Repair_details_Adapter.Viewholder> {

    private List<RepairModel2> repairModel2;
    Context context;

    public Repair_details_Adapter(List<RepairModel2> repairModel2, Context context) {
        this.repairModel2 = repairModel2;
        this.context = context;
    }

    @NonNull
    @Override
    public Repair_details_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repair_details_design, parent, false);
        Viewholder viewholder = new Viewholder(view);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        viewholder.itemView.startAnimation(animation);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Repair_details_Adapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {

        holder.Eq_Name.setText(repairModel2.get(position).getD_Equipment_Name());
        holder.Eq_number.setText(repairModel2.get(position).getD_Equipment_Number());
        //  holder.Repair_repaired_check.setText(repairsClassList.get(position).getD_Repaired());
        holder.Remark.setText(repairModel2.get(position).getD_Remark());
        holder.Amount.setText(repairModel2.get(position).getD_Amount());
        holder.RImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.repairModel2 = repairModel2.get(position);
                Intent intent = new Intent(context, RepairTwoImageListActivity.class);
                context.startActivity(intent);
            }
        });
        holder.RBreakup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.repairModel2 = repairModel2.get(position);
                Intent intent = new Intent(context, RepairBreakUpActivity.class);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return repairModel2.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView Eq_Name, Eq_number, Repaired, Remark, Amount;

        CheckBox Repair_repaired_check;
        ImageView RImage, RBreakup;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Eq_Name = itemView.findViewById(R.id.repair_equipment_name);
            Eq_number = itemView.findViewById(R.id.repair_equipment_id);

            Remark = itemView.findViewById(R.id.repair_remark);
            Amount = itemView.findViewById(R.id.repair_amount);
            RImage = itemView.findViewById(R.id.repair2_image);
            RBreakup = itemView.findViewById(R.id.repair_breakup);
        }
    }
}
