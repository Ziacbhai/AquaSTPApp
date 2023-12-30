package Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.Activities.Repair_Details_Activity;
import com.ziac.aquastpapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Models.RepairClass1;

public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.Viewholder> {
    private List<RepairClass1> repairClass1;
    Context context;

    public RepairAdapter(List<RepairClass1> repairClass1, Context context) {
        this.repairClass1 = repairClass1;
        this.context = context;
    }

    @NonNull
    @Override
    public RepairAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repair_design, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepairAdapter.Viewholder holder, int position) {
        //holder.Repno.setText(repairS.get(position).getREPNo());
        holder.Amount.setText(repairClass1.get(position).getRepair_Amount() + "0");

        holder.Repair_remark.setText(repairClass1.get(position).getRemark());
        holder.Created_by.setText(repairClass1.get(position).getR_createdby());

        String dateString = repairClass1.get(position).getRepair_Date();

        String conNoString = repairClass1.get(position).getREPNo();
        double conrepNo;
        try {
            conrepNo = Double.parseDouble(conNoString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        String formattedRepNo = removeTrailingZero(conrepNo);
        holder.Repno.setText(formattedRepNo);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date;
        try {
            date = inputFormat.parse(dateString);
            String Date = outputFormat.format(date);
            holder.RepairDate.setText(Date);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        holder.Repair_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.repairClass1 = repairClass1.get(position);
                Intent i = new Intent(context, Repair_Details_Activity.class);
                context.startActivity(i);
//                ((Activity) context).finish();

            }
        });
    }

    private String removeTrailingZero(double value) {
        // Convert the double to a string
        String formattedValue = String.valueOf(value);
        // Remove trailing zero if it's a decimal number
        if (formattedValue.indexOf(".") > 0) {
            formattedValue = formattedValue.replaceAll("0*$", "").replaceAll("\\.$", "");
        }
        return formattedValue;
    }

    @Override
    public int getItemCount() {
        return repairClass1.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView Repno, Amount, RepairDate, Repair_remark, Created_by;
        ImageView Repair_info;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Repno = itemView.findViewById(R.id.repno_);
            Amount = itemView.findViewById(R.id.repairamount);
            RepairDate = itemView.findViewById(R.id.repair_date);
            Repair_remark = itemView.findViewById(R.id.remarks_remark);
            Repair_info = itemView.findViewById(R.id.repair_info);
            Created_by = itemView.findViewById(R.id.repair_createdby);
        }
    }
}
