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

public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.Viewholder> {
    private List<CommonModelClass> commonModelClassList;
    Context context;

    public RepairAdapter(List<CommonModelClass> commonModelClassList, Context context) {
        this.commonModelClassList = commonModelClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public RepairAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repair_details, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepairAdapter.Viewholder holder, int position) {
        holder.Repno.setText(commonModelClassList.get(position).getREPNo());
        holder.Amount.setText(commonModelClassList.get(position).getAmount());

    }

    @Override
    public int getItemCount() {
        return commonModelClassList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView  Repno,Amount;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Repno=itemView.findViewById(R.id.repno_);
            Amount=itemView.findViewById(R.id.amount_);

        }
    }
}
