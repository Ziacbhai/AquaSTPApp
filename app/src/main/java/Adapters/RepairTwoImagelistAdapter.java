package Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.R;

import java.util.ArrayList;


import Models.RepairModel4;

public class RepairTwoImagelistAdapter extends RecyclerView.Adapter<RepairTwoImagelistAdapter.Viewholder> {
    ArrayList<RepairModel4> repairClasses4;
    Context context;
    Picasso.Builder builder;
    Picasso picasso;

    public RepairTwoImagelistAdapter(ArrayList<RepairModel4> repairClasses4, Context context) {
        this.repairClasses4 = repairClasses4;
        this.context = context;
    }

    @NonNull
    @Override
    public RepairTwoImagelistAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repairtwoimagelist, parent, false);
        Viewholder viewholder = new Viewholder(view);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        viewholder.itemView.startAnimation(animation);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull RepairTwoImagelistAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.Builder builder = new Picasso.Builder(context);
        Picasso picasso = builder.build();
        String originalImageUrl = repairClasses4.get(position).getI_Repair_image();
        String trimmedImageUrl = originalImageUrl.replace('~', ' ').trim();
        picasso.load(Uri.parse(Global.repair_images + trimmedImageUrl))
                .error(R.drawable.no_image_available_icon)
                .into(holder.repair_image_show);

        holder.repair_image_remark.setText(repairClasses4.get(position).getI_Remark());
        holder.repair_image_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImage(repairClasses4.get(position).getI_Repair_image());
            }
        });

    }



    public void showImage(String imageUrl) {

        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // Calculate display dimensions
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        String trimmedImageUrl = imageUrl.replace('~', ' ').trim();
        Picasso.get().load(Uri.parse(Global.repair_images + trimmedImageUrl)).into(new Target() {
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

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return repairClasses4.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView repair_image_show;
        TextView repair_image_remark;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            repair_image_show = itemView.findViewById(R.id.repairtwo_image_list);
            repair_image_remark = itemView.findViewById(R.id.repair_image_remark);
        }
    }
}
