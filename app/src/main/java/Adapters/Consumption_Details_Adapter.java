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

import Models.ConsumptionClass;
import Models.ConsumptionClass2;

public class Consumption_Details_Adapter extends RecyclerView.Adapter<Consumption_Details_Adapter.Viewholder> {

    Context context;
    private List<ConsumptionClass2> consumptionClass2;

    public Consumption_Details_Adapter(Context context, List<ConsumptionClass2> consumptionClass2) {
        this.context = context;
        this.consumptionClass2 = consumptionClass2;
    }

    @NonNull
    @Override
    public Consumption_Details_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consumption_details_design, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Consumption_Details_Adapter.Viewholder holder, int position) {
        holder.Consumption_eq_name.setText(consumptionClass2.get(position).getEquipment_Name());
        holder.Consumption_eq_id.setText(consumptionClass2.get(position).getEquipment_id());
        holder.Consumption_item.setText(consumptionClass2.get(position).getD_item() + " / " + consumptionClass2.get(position).getD_item_name());
        holder.Consumption_rate.setText(consumptionClass2.get(position).getD_rate());
        holder.Consumption_unit.setText(consumptionClass2.get(position).getD_unit());
        holder.Consumption_qty.setText(consumptionClass2.get(position).getD_qty());
        holder.Consumptionamount.setText(consumptionClass2.get(position).getD_Amount() + "0");
        // holder.C_item_name.setText(consumablesClassList.get(position).getD_item_name());

    }

    @Override
    public int getItemCount() {
        return consumptionClass2.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView Consumption_eq_name, Consumption_eq_id, Consumption_item, Consumption_rate, Consumption_unit, Consumption_qty, Consumption_item_name, Consumptionamount;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Consumption_eq_name = itemView.findViewById(R.id.consumption_d_eq_name);
            Consumption_eq_id = itemView.findViewById(R.id.cconsumption_d_eq_id);
            Consumption_item = itemView.findViewById(R.id.consumption_d_item);
            Consumption_rate = itemView.findViewById(R.id.consumption_d_rate);
            Consumption_unit = itemView.findViewById(R.id.consumption_d_unit);
            Consumption_qty = itemView.findViewById(R.id.consumption_d_qty);
            // C_item_name= itemView.findViewById(R.id.consumables_d_item_name);
            Consumptionamount = itemView.findViewById(R.id.consumption_d_amount);

        }
    }
}
