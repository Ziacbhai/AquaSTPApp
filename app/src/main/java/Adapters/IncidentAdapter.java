package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ziac.aquastpapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Models.IncidentsClass;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.Viewholder> {

    private List<IncidentsClass> incidentsClasses;
    Context context;

    public IncidentAdapter(Context context, ArrayList<IncidentsClass> incidentsClasses) {
        this.context=context;
        this.incidentsClasses=incidentsClasses;

    }


    @NonNull
    @Override
    public IncidentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_design, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentAdapter.Viewholder holder, int position) {

        holder.Incedent_Particlulars.setText(incidentsClasses.get(position).getIncidents_Particulars());


        String conNoString = incidentsClasses.get(position).getInc_No();
        double conNo;
        try {
            conNo = Double.parseDouble(conNoString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        String formattedConNo = removeTrailingZero(conNo);
        holder.Incno.setText(formattedConNo);

        String dateString = incidentsClasses.get(position).getInc_Date();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date;
        try {date = inputFormat.parse(dateString);
            String Date = outputFormat.format(date);
            holder.Inc_date.setText(Date);
        } catch (ParseException e) {e.printStackTrace();
            return;
        }

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
        return incidentsClasses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView Incno,Incedent_Particlulars,Inc_date;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Incno=itemView.findViewById(R.id.incident_incno);
            Incedent_Particlulars=itemView.findViewById(R.id.incident_perticulars);
            Inc_date=itemView.findViewById(R.id.incident_date);

        }
    }
}
