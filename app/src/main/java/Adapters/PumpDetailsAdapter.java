package Adapters;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.R;

import java.util.List;

import Models.CommonModelClass;

public class PumpDetailsAdapter extends RecyclerView.Adapter<PumpDetailsAdapter.CommonViewHolder> {
    private  List<CommonModelClass> commonModelClassList;
    Context context;

    CardView dc;
    public PumpDetailsAdapter(List<CommonModelClass> commonModelClassList, Context context) {
        this.commonModelClassList = commonModelClassList;
        this.context =context;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public PumpDetailsAdapter.CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pump_details, parent , false);
        //dc = view.findViewById(R.id.cardView);
       // dc.setCardBackgroundColor(Color.parseColor("#FF6363"));
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PumpDetailsAdapter.CommonViewHolder holder, int position) {

      //  String userimage = Global.userImageurl + Global.sharedPreferences.getString("name_plate", "");

        Picasso.Builder builder=new Picasso.Builder(context);
        Picasso picasso=builder.build();
        //picasso.load(Uri.parse(Global.baseurl + commonModelClassList.get(position).getImage().substring(2))).error(R.drawable.no_image_available_icon).into(holder.ImageView);
        picasso.load(Uri.parse(Global.baseurl + commonModelClassList.get(position).getImage().substring(2))).error(R.drawable.no_image_available_icon)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.ImageView);
        //picasso.load(Global.baseurl + commonModelClassList.get(position).getImage().substring(2)).into(holder.ImageView);
        //Log.d("imageurl",Global.baseurl + commonModelClassList.get(position).getImage().substring(2));

        holder.Manufacturer.setText(commonModelClassList.get(position).getManufacturer());
        holder.EquipmentName.setText(commonModelClassList.get(position).getEquipmentName());
        holder.Equipment_id.setText(commonModelClassList.get(position).getEquipmentNumber_Id());
        holder.Specification.setText(commonModelClassList.get(position).getSpecification());
        holder.Rating_Capacity.setText(commonModelClassList.get(position).getRating_Capacity());
        holder.FormFactor.setText(commonModelClassList.get(position).getForm_Factor());
        holder.Phase.setText(commonModelClassList.get(position).getPhase());
        holder.CleaningRunningFrequencyHRS.setText(commonModelClassList.get(position).getCleaning_RunningFrequency_HRS());
        holder.ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = holder.getAdapterPosition();
                String userimage = Global.baseurl + commonModelClassList.get(pos).getImage().substring(2);
                showImage(picasso,userimage);

            }
        });
    }

    public void showImage(Picasso picasso, String userimage) {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // Nothing
            }
        });

        // Calculate display dimensions
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Load the image using Picasso
        picasso.load(Uri.parse(userimage)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ImageView imageView = new ImageView(context);

                // Calculate dimensions to fit the image within the screen
                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();
                float aspectRatio = (float) imageWidth / imageHeight;

                int newWidth = screenWidth;
                int newHeight = (int) (screenWidth / aspectRatio);
                if (newHeight > screenHeight) {
                    newHeight = screenHeight;
                    newWidth = (int) (screenHeight * aspectRatio);
                }

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
                imageView.setLayoutParams(layoutParams);

                imageView.setImageBitmap(bitmap);

                builder.addContentView(imageView, layoutParams);
                builder.show();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                // Handle bitmap loading failure
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Prepare bitmap loading
            }
        });
    }

    @Override
    public int getItemCount() {
        return commonModelClassList.size();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {
     private    ImageView ImageView;
     private    TextView Manufacturer,EquipmentName,Specification,Rating_Capacity,Equipment_id,
             FormFactor,Phase,CleaningRunningFrequencyHRS,Process_name;

        public CommonViewHolder(@NonNull View itemView) {
            super(itemView);

            ImageView=itemView.findViewById(R.id.image_);
            Manufacturer=itemView.findViewById(R.id.manufacturer_);
            EquipmentName=itemView.findViewById(R.id.equipmentName_);
            Equipment_id=itemView.findViewById(R.id.equipmentId_);
            Specification=itemView.findViewById(R.id.specification_);
            Rating_Capacity=itemView.findViewById(R.id.rating_capacity_);
            FormFactor=itemView.findViewById(R.id.formFactor_);
            Phase=itemView.findViewById(R.id.phase_);
            CleaningRunningFrequencyHRS=itemView.findViewById(R.id.cleaning_Running_);
        }
    }
}
