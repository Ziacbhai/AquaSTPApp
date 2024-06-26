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

import Models.LabTestModel;

public class LabTestDetailsAdapter extends RecyclerView.Adapter<LabTestDetailsAdapter.Viewholder> {
    Context context;
    private List<LabTestModel> labTestModels;

    public LabTestDetailsAdapter(Context context, List<LabTestModel> labTestModels) {
        this.context = context;
        this.labTestModels = labTestModels;
    }

    @NonNull
    @Override
    public LabTestDetailsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_test_details_design, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabTestDetailsAdapter.Viewholder holder, int position) {
        holder.TestMethod_d.setText(labTestModels.get(position).getL_Test_Method());
        holder.Units_d.setText(labTestModels.get(position).getL_Units());
        holder.Result_d.setText(labTestModels.get(position).getL_result());
        holder.Kspcb_Standard.setText(labTestModels.get(position).getL_KSPCB_Standard());
    }

    @Override
    public int getItemCount() {
        return labTestModels.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView TestMethod_d, Result_d, Units_d, Kspcb_Standard;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            TestMethod_d = itemView.findViewById(R.id.d_lab_testmethod);
            Units_d = itemView.findViewById(R.id.d_lab_units);
            Result_d = itemView.findViewById(R.id.ld_lab_result);
            Kspcb_Standard = itemView.findViewById(R.id.d_lab_kspcd);

        }
    }
}
