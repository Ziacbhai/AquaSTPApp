package Adapters;

import static com.google.android.material.internal.ViewUtils.getBackgroundColor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.R;

import java.util.List;

import Models.CommonModelClass;

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.CommonViewHolder> {
    private  List<CommonModelClass> commonModelClassList;
    Context context;

    CardView dc;
    public CommonAdapter(List<CommonModelClass> commonModelClassList, Context context) {
        this.commonModelClassList = commonModelClassList;
        this.context =context;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public CommonAdapter.CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pump_details, parent , false);
        //dc = view.findViewById(R.id.cardView);
       // dc.setCardBackgroundColor(Color.parseColor("#FF6363"));
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonAdapter.CommonViewHolder holder, int position) {

      //  String userimage = Global.userImageurl + Global.sharedPreferences.getString("name_plate", "");

        Picasso.Builder builder=new Picasso.Builder(context);
        Picasso picasso=builder.build();
        //picasso.load(Uri.parse(Global.baseurl + commonModelClassList.get(position).getImage().substring(2))).error(R.drawable.no_image_available_icon).into(holder.ImageView);
        picasso.load(Uri.parse(Global.baseurl + commonModelClassList.get(position).getImage().substring(2))).error(R.drawable.no_image_available_icon)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.ImageView);
        //picasso.load(Global.baseurl + commonModelClassList.get(position).getImage().substring(2)).into(holder.ImageView);
        Log.d("imageurl",Global.baseurl + commonModelClassList.get(position).getImage().substring(2));

        holder.Manufacturer.setText(commonModelClassList.get(position).getManufacturer());
        holder.EquipmentName.setText(commonModelClassList.get(position).getEquipmentName());
        holder.Specification.setText(commonModelClassList.get(position).getSpecification());
        holder.EquipmentNumber.setText(commonModelClassList.get(position).getEquipmentNumber_Id());
        holder.Rating_Capacity.setText(commonModelClassList.get(position).getRating_Capacity());
        holder.FormFactor.setText(commonModelClassList.get(position).getForm_Factor());
        holder.Phase.setText(commonModelClassList.get(position).getPhase());
        holder.CleaningRunningFrequencyHRS.setText(commonModelClassList.get(position).getCleaning_RunningFrequency_HRS());

    }

    @Override
    public int getItemCount() {
        return commonModelClassList.size();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {
     private    ImageView ImageView;
     private    TextView Manufacturer,EquipmentName,Specification,EquipmentNumber,Rating_Capacity,
             FormFactor,Phase,CleaningRunningFrequencyHRS;

        public CommonViewHolder(@NonNull View itemView) {
            super(itemView);

            ImageView=itemView.findViewById(R.id._image1);
            Manufacturer=itemView.findViewById(R.id.manufacturer);
            EquipmentName=itemView.findViewById(R.id.equipmentName);
            Specification=itemView.findViewById(R.id.specification);
            EquipmentNumber=itemView.findViewById(R.id.equipmentNumber);
            Rating_Capacity=itemView.findViewById(R.id.rating_capacity);
            FormFactor=itemView.findViewById(R.id.formFactor);
            Phase=itemView.findViewById(R.id.phase);
            CleaningRunningFrequencyHRS=itemView.findViewById(R.id.cleaning_Running);
        }
    }
}
