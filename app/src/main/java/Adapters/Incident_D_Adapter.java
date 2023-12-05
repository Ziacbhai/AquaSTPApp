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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Models.IncidentsClass;

public class Incident_D_Adapter extends RecyclerView.Adapter<Incident_D_Adapter.Viewholder> {
    private List<IncidentsClass> incidentsClasses;
    private Context context;

    public Incident_D_Adapter(List<IncidentsClass> incidentsClasses, Context context) {
        this.incidentsClasses = incidentsClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public Incident_D_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_details_design, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Incident_D_Adapter.Viewholder holder, int position) {
        holder.Incident_Particlulars.setText(incidentsClasses.get(position).getIncidents_Particulars());

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

    @Override
    public int getItemCount() {
        return incidentsClasses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView Incident_Particlulars,Inc_date;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Incident_Particlulars=itemView.findViewById(R.id.incident_perticulars);
            Inc_date=itemView.findViewById(R.id.incident_date);
        }
    }
}
