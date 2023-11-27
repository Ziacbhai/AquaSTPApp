package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        //holder.Repno.setText(repairS.get(position).getREPNo());
        holder.Amount.setText(repairS.get(position).getRepair_Amount()+"0");

        String dateString = repairS.get(position).getRepair_Date();

        String conNoString = repairS.get(position).getREPNo();
        double conNo;
        try {
            conNo = Double.parseDouble(conNoString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        String formattedConNo = removeTrailingZero(conNo);
        holder.Repno.setText(formattedConNo);


        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date;
        try {date = inputFormat.parse(dateString);
            String Date = outputFormat.format(date);
            holder.RepairDate.setText(Date);
        } catch (ParseException e) {e.printStackTrace();
            return;
        }



        holder.Repair_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        return repairS.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView  Repno,Amount,RepairDate;
        LinearLayout Repair_details;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Repno=itemView.findViewById(R.id.repno_);
            Amount=itemView.findViewById(R.id.amount_);
            RepairDate=itemView.findViewById(R.id.repair_date);
            Repair_details=itemView.findViewById(R.id.repair_details);

        }
    }
}
