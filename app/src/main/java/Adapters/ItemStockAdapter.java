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

import Models.ItemStockClass;

public class ItemStockAdapter extends RecyclerView.Adapter<ItemStockAdapter.Viewholder> {
    Context context;
    private List<ItemStockClass> itemStockClasses;

    public ItemStockAdapter(Context context, List<ItemStockClass> itemStockClasses) {
        this.context = context;
        this.itemStockClasses = itemStockClasses;
    }
    @NonNull
    @Override
    public ItemStockAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemstock_design, parent , false);
        return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ItemStockAdapter.Viewholder holder, int position) {

        holder.Consumables_Code.setText(itemStockClasses.get(position).get_Code());
        holder.Consumables_Name.setText(itemStockClasses.get(position).get_Name());
        holder.Consumables_Stock.setText(itemStockClasses.get(position).get_Stock());
        holder.Consumables_Units.setText(itemStockClasses.get(position).get_Units());
    }
    @Override
    public int getItemCount() {
        return itemStockClasses.size();
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
