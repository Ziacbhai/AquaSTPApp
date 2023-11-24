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

import Models.CommonModelClass;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.Viewholder> {

    private List<CommonModelClass> commonModelClassList;
    Context context;

    public IncidentAdapter(List<CommonModelClass> commonModelClassList, Context context) {
        this.commonModelClassList = commonModelClassList;
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
        holder.Incno.setText(commonModelClassList.get(position).getTRNO());
        holder.Incedent.setText(commonModelClassList.get(position).getRefno());

    }

    @Override
    public int getItemCount() {
        return commonModelClassList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView Incno,Incedent,Site_,STP_;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Incno=itemView.findViewById(R.id.trno_);
            Incedent=itemView.findViewById(R.id.lab_refno_);

        }
    }
}
