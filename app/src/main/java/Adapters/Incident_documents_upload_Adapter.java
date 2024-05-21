package Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.R;

import java.util.ArrayList;

import Models.IncidentsModelClass;

public class Incident_documents_upload_Adapter extends RecyclerView.Adapter<Incident_documents_upload_Adapter.Viewholder> {

    private Context context;
    private ArrayList<IncidentsModelClass> incidentsModelClasses;

    public Incident_documents_upload_Adapter(Context context, ArrayList<IncidentsModelClass> incidentsModelClasses) {
        this.context = context;
        this.incidentsModelClasses = incidentsModelClasses;
    }

    @NonNull
    @Override
    public Incident_documents_upload_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_docs_list_upload, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Incident_documents_upload_Adapter.Viewholder holder, int position) {

        IncidentsModelClass incidentsModelClass = incidentsModelClasses.get(position);
        holder.In_doc_name.setText(incidentsModelClass.getInc_doc_name());

        Picasso.Builder builder = new Picasso.Builder(context);
        Picasso picasso = builder.build();
        picasso.load(Uri.parse(Global.incident_image + incidentsModelClasses.get(position).getImageList())).error(R.drawable.no_image_available_icon).into(holder.In_doc_show);
    }


    @Override
    public int getItemCount() {
        return incidentsModelClasses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView Incident_delete_doc, In_doc_show;
        TextView In_doc_name;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            In_doc_show = itemView.findViewById(R.id.in_upload);
          //  Incident_delete_doc = itemView.findViewById(R.id.incident_delete_doc);
            In_doc_name = itemView.findViewById(R.id.in_doc_name);
        }
    }
}
