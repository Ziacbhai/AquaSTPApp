package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.R;

import java.util.ArrayList;
import java.util.List;

import Models.ConsumableClass;

public class ConsumablesAdapter extends RecyclerView.Adapter<ConsumablesAdapter.Viewholder> {

    Context context;
    private List<ConsumableClass> consumableClasses;

    public ConsumablesAdapter(Context context, List<ConsumableClass> consumableClasses) {
        this.context = context;
        this.consumableClasses = consumableClasses;
    }

    @NonNull
    @Override
    public ConsumablesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consumable_design, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumablesAdapter.Viewholder holder, int position) {

        holder.Consumables_Code.setText(consumableClasses.get(position).get_Code());
        holder.Consumables_Name.setText(consumableClasses.get(position).get_Name());
        holder.Consumables_Stock.setText(consumableClasses.get(position).get_Stock());
        holder.Consumables_Units.setText(consumableClasses.get(position).get_Units());

    }

    @Override
    public int getItemCount() {
        return consumableClasses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView Consumables_Code ,Consumables_Name,Consumables_Stock,Consumables_Units;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Consumables_Code = itemView.findViewById(R.id.code_);
            Consumables_Name = itemView.findViewById(R.id.namelist_);
            Consumables_Stock = itemView.findViewById(R.id.stock_);
            Consumables_Units = itemView.findViewById(R.id.units_);

        }
    }
}
