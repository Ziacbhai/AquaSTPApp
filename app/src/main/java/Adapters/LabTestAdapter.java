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
import Models.LabTestClass;


public class LabTestAdapter extends RecyclerView.Adapter<LabTestAdapter.Viewholder> {
    private List<LabTestClass> labTestClasses;
    Context context;

    public LabTestAdapter(List<LabTestClass> labTestClasses, Context context) {
        this.labTestClasses = labTestClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public LabTestAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_test_details, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabTestAdapter.Viewholder holder, int position) {
        holder.TRno.setText(labTestClasses.get(position).getTRno());
        holder.Refno.setText(labTestClasses.get(position).getRefno());
        holder.CustomerRef.setText(labTestClasses.get(position).getCustomerRef());
    }

    @Override
    public int getItemCount() {
        return labTestClasses.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private TextView TRno, Refno,LabDate,LabRefDate,CustomerRef,Sample_Received_Date,
                Test_Start_Date,Test_Completion_Date,Sample_Received_By,Sample_Particular;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            TRno=itemView.findViewById(R.id.trno_);
            Refno=itemView.findViewById(R.id.lab_refno_);
            CustomerRef=itemView.findViewById(R.id.lab_customerref_);

        }
    }
}
