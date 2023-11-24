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
import Models.IncidentsClass;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.Viewholder> {

    private List<IncidentsClass> incidentsClasses;
    Context context;

    public IncidentAdapter(List<IncidentsClass> incidentsClasses, Context context) {
        this.incidentsClasses = incidentsClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public IncidentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_details, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentAdapter.Viewholder holder, int position) {
        holder.Incno.setText(incidentsClasses.get(position).getInc_No());
        holder.Incedent_Particlulars.setText(incidentsClasses.get(position).getIncidents_Particulars());

    }

    @Override
    public int getItemCount() {
        return incidentsClasses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView Incno,Incedent_Particlulars,Inc_date;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Incno=itemView.findViewById(R.id.trno_);
            Incedent_Particlulars=itemView.findViewById(R.id.lab_refno_);
            Inc_date=itemView.findViewById(R.id.lab_refno_);

        }
    }
}
