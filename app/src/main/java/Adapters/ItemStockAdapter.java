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

import Models.ItemStockModel;

public class ItemStockAdapter extends RecyclerView.Adapter<ItemStockAdapter.Viewholder> {
    Context context;
    private List<ItemStockModel> itemStockModels;

    public ItemStockAdapter(Context context, List<ItemStockModel> itemStockModels) {
        this.context = context;
        this.itemStockModels = itemStockModels;
    }

    @NonNull
    @Override
    public ItemStockAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemstock_design, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemStockAdapter.Viewholder holder, int position) {

        holder.Consumables_Code.setText(itemStockModels.get(position).get_Number());
        holder.Consumables_Stock.setText(itemStockModels.get(position).get_Stock());

    }

    @Override
    public int getItemCount() {
        return itemStockModels.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView Consumables_Code, Consumables_Name, Consumables_Stock, Consumables_Units;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Consumables_Code = itemView.findViewById(R.id.part_number);
            Consumables_Stock = itemView.findViewById(R.id.stock_);

        }
    }
}
