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


public class LabTestAdapter extends RecyclerView.Adapter<LabTestAdapter.Viewholder> {
    private List<CommonModelClass> commonModelClassList;
    Context context;

    public LabTestAdapter(List<CommonModelClass> commonModelClassList, Context context) {
        this.commonModelClassList = commonModelClassList;
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
        holder.TRNO.setText(commonModelClassList.get(position).getTRNO());
        holder.Refno.setText(commonModelClassList.get(position).getRefno());
        holder.CustomerNo.setText(commonModelClassList.get(position).getCustomerNo());
    }

    @Override
    public int getItemCount() {
        return commonModelClassList.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        private TextView TRNO,Refno,CustomerNo,Site_,STP_;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            TRNO=itemView.findViewById(R.id.trno_);
            Refno=itemView.findViewById(R.id.lab_refno_);
            CustomerNo=itemView.findViewById(R.id.lab_customerref_);

        }
    }
}
