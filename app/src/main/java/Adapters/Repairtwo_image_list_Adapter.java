package Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.R;

import java.util.ArrayList;

import Models.RepairsClass;

public class Repairtwo_image_list_Adapter extends RecyclerView.Adapter<Repairtwo_image_list_Adapter.Viewholder> {
     ArrayList<RepairsClass> repairsClasses;
     Context context;


    public Repairtwo_image_list_Adapter(ArrayList<RepairsClass> repairsClasses, Context context) {
        this.repairsClasses = repairsClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public Repairtwo_image_list_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repairtwoimagelist, parent , false);
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Repairtwo_image_list_Adapter.Viewholder holder, int position) {
        Picasso.Builder builder=new Picasso.Builder(context);
        Picasso picasso=builder.build();
        String originalImageUrl = repairsClasses.get(position).getRepairtwo_image();
        String trimmedImageUrl = originalImageUrl.replace('~', ' ').trim();
        picasso.load(Uri.parse(Global.repair_images + trimmedImageUrl))
                .error(R.drawable.no_image_available_icon)
                .into(holder.repair_image_show);

        holder.repair_image_remark.setText(repairsClasses.get(position).getD_Remark());
        holder.repair_image_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(repairsClasses.get(position).getR_ImageList());
            }
        });

    }

    private void showImage(String imageUrl) {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Load the image using Picasso or your preferred image loading library
        Picasso.get().load(Uri.parse(Global.repair_images + imageUrl)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // Display the image in a larger size
                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(bitmap);

                builder.addContentView(imageView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                builder.show();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }

            // Handle onBitmapFailed and onPrepareLoad methods as needed
        });
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

            repair_image_show = itemView.findViewById(R.id.repairtqo_image_list);
            repair_image_remark = itemView.findViewById(R.id.repair_image_remark);
        }
    }
}
