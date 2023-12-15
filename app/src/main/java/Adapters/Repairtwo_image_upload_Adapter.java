package Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.R;

import java.util.ArrayList;

import Models.IncidentsClass;
import Models.RepairsClass;

public class Repairtwo_image_upload_Adapter extends RecyclerView.Adapter<Repairtwo_image_upload_Adapter.Viewholder> {
     ArrayList<RepairsClass> repairsClasses;
     Context context;


    public Repairtwo_image_upload_Adapter(ArrayList<RepairsClass> repairsClasses, Context context) {
        this.repairsClasses = repairsClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public Repairtwo_image_upload_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repairtwoimage, parent , false);
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Repairtwo_image_upload_Adapter.Viewholder holder, int position) {
        Picasso.Builder builder=new Picasso.Builder(context);
        Picasso picasso=builder.build();
        picasso.load(Uri.parse(Global.incident_image + repairsClasses.get(position).getR_ImageList())).error(R.drawable.no_image_available_icon).into(holder.repair_image_show);
        holder.repair_image_remark.setText(repairsClasses.get(position).getD_Repairedtwo());

    }

    @Override
    public int getItemCount() {
        return repairsClasses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView repair_image_show;
        TextView repair_image_remark;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            repair_image_show = itemView.findViewById(R.id.repair_image_show);
            repair_image_remark = itemView.findViewById(R.id.repair_image_remark);
        }
    }
}
