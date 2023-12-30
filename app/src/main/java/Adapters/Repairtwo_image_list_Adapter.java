package Adapters;

import static com.ziac.aquastpapp.Activities.Global.repairClass4;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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


import Models.RepairClass2;
import Models.RepairClass4;

public class Repairtwo_image_list_Adapter extends RecyclerView.Adapter<Repairtwo_image_list_Adapter.Viewholder> {
    ArrayList<RepairClass4> repairClasses4;
    Context context;
    Picasso.Builder builder;
    Picasso picasso;

    public Repairtwo_image_list_Adapter(ArrayList<RepairClass4> repairClasses4, Context context) {
        this.repairClasses4 = repairClasses4;
        this.context = context;
    }

    @NonNull
    @Override
    public Repairtwo_image_list_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repairtwoimagelist, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Repairtwo_image_list_Adapter.Viewholder holder, int position) {
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
                showImage(picasso, repairClasses4.get(position).getIImageList());
            }
        });

    }

   /* private void showImage(String imageUrl) {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Create an ImageView
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // Load the image using Picasso or your preferred image loading library
        Picasso.get().load(Uri.parse(Global.repair_images + imageUrl)).into(imageView);

        // Set the ImageView as the content view of the dialog
        builder.setContentView(imageView);

        // Set layout parameters to fit the image within the screen
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        int dialogWidth = screenWidth;
        int dialogHeight = screenHeight;

        builder.getWindow().setLayout(dialogWidth, dialogHeight);
        // Show the dialog
        builder.show();



    }*/

    public void showImage(Picasso picasso, String imageUrl) {
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
        Picasso.get().load(Uri.parse(Global.repair_images + imageUrl)).into(new Target() {
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
