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

public class IncidentImageListAdapter extends RecyclerView.Adapter<IncidentImageListAdapter.Viewholder> {
    private List<IncidentsClass> incidentsClasses;
    Context context;

    public IncidentImageListAdapter(List<IncidentsClass> incidentsClasses, Context context) {
        this.incidentsClasses = incidentsClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public IncidentImageListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_image_list, parent , false);
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull IncidentImageListAdapter.Viewholder holder, int position) {
        holder.List_View.setText(incidentsClasses.get(position).getImageList());
    }

    @Override
    public int getItemCount() {
        return incidentsClasses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView List_View;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            List_View = itemView.findViewById(R.id.image_list_incident);
        }
    }
}
