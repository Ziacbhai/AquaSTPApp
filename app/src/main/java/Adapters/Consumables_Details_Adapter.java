package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.R;

import java.util.List;

import Models.ConsumablesClass;

public class Consumables_Details_Adapter extends RecyclerView.Adapter<Consumables_Details_Adapter.Viewholder> {

    Context context;
    private List<ConsumablesClass> consumablesClassList;

    public Consumables_Details_Adapter(Context context, List<ConsumablesClass> consumablesClassList) {
        this.context = context;
        this.consumablesClassList = consumablesClassList;
    }

    @NonNull
    @Override
    public Consumables_Details_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consumables_details_design, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Consumables_Details_Adapter.Viewholder holder, int position) {
      holder.C_eq_name.setText(consumablesClassList.get(position).getEquipment_Name());
      holder.C_eq_id.setText(consumablesClassList.get(position).getEquipment_id());
      holder.C_item.setText(consumablesClassList.get(position).getD_item() +" / "+ consumablesClassList.get(position).getD_item_name() );
      holder.C_rate.setText(consumablesClassList.get(position).getD_rate());
      holder.C_unit.setText(consumablesClassList.get(position).getD_unit());
      holder.C_qty.setText(consumablesClassList.get(position).getD_qty());
      holder.C_amount.setText(consumablesClassList.get(position).getD_Amount()+"0");
     // holder.C_item_name.setText(consumablesClassList.get(position).getD_item_name());

    }

    @Override
    public int getItemCount() {
        return consumablesClassList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView C_eq_name,C_eq_id,C_item,C_rate,C_unit,C_qty,C_item_name,C_amount;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            C_eq_name= itemView.findViewById(R.id.consumables_eq_name);
            C_eq_id= itemView.findViewById(R.id.consumables_d_eq_id);
            C_item= itemView.findViewById(R.id.consumables_d_item);
            C_rate= itemView.findViewById(R.id.consumables_d_rate);
            C_unit= itemView.findViewById(R.id.consumables_d_unit);
            C_qty= itemView.findViewById(R.id.consumables_d_qty);
           // C_item_name= itemView.findViewById(R.id.consumables_d_item_name);
            C_amount= itemView.findViewById(R.id.consumables_d_amount);

        }
    }
}
