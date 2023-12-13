package Adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.Activities.Consumables_Details_Activity;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Models.ConsumablesClass;

public class ConsumablesAdapter extends RecyclerView.Adapter<ConsumablesAdapter.Viewholder> {
    private final List<ConsumablesClass> consumablesClasses;
    Context context;

    public ConsumablesAdapter(List<ConsumablesClass> consumablesClasses, Context context) {
        this.consumablesClasses = consumablesClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public ConsumablesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consumables_design, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumablesAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {

        holder.Amount.setText(consumablesClasses.get(position).getAmount()+"0");

        holder.Remark.setText(consumablesClasses.get(position).getRemark());

        String conNoString = consumablesClasses.get(position).getCon1_code();

// Parse the string into a double
        double conNo;
        try {
            conNo = Double.parseDouble(conNoString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return; // Handle the parse exception
        }

        String formattedConNo = removeTrailingZero(conNo);
        holder.Con1_code.setText(formattedConNo);

       // holder.Date.setText(consumablesClasses.get(position).getDate());

        String dateString = consumablesClasses.get(position).getDate();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date;
        try {date = inputFormat.parse(dateString);
            String Date = outputFormat.format(date);
            holder.Date.setText(Date);

        } catch (ParseException e) {e.printStackTrace();
            return;
        }

        holder.Consumable_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.ConsumablesClass = consumablesClasses.get(position);
                Intent i = new Intent(context, Consumables_Details_Activity.class);
                context.startActivity(i);
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
        return consumablesClasses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView Con1_code,Date,Amount,Remark;
        ImageView Consumable_info;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Con1_code= itemView.findViewById(R.id.consumables_con_no);
            Date= itemView.findViewById(R.id.consumables_date);
            Amount= itemView.findViewById(R.id.consumables_amount);
            Remark= itemView.findViewById(R.id.consumables_remark);
            Consumable_info= itemView.findViewById(R.id.consumable_info);
        }
    }
}
