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

import java.util.List;

import Models.CommonModelClass;

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.CommonViewHolder> {
    private  List<CommonModelClass> commonModelClassList;
    Context context;
    public CommonAdapter(List<CommonModelClass> commonModelClassList, Context context) {
        this.commonModelClassList = commonModelClassList;
        this.context = this.context;
    }

    @NonNull
    @Override
    public CommonAdapter.CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pump_details, parent , false);
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonAdapter.CommonViewHolder holder, int position) {
        Picasso.Builder builder=new Picasso.Builder(context);
        Picasso picasso=builder.build();
        picasso.load(Uri.parse(Global.userImageurl + commonModelClassList.get(position).getImage_id())).error(R.drawable.no_image_available_icon);

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

            ImageView=itemView.findViewById(R.id.pump_image);
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
