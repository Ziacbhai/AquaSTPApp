package Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ziac.aquastpapp.Activities.FiltersDailyLogImageUploadActivity;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.R;

import java.util.List;

import Models.FiltersModel;
//import Models.PumpMotorBlower_LogClass;

public class FiltersDailyLogAdapter extends RecyclerView.Adapter<FiltersDailyLogAdapter.Viewholder> {

    Context context;
    private List<FiltersModel> filtersModelList;

    public FiltersDailyLogAdapter(Context context, List<FiltersModel> filtersModelList) {
        this.context = context;
        this.filtersModelList = filtersModelList;
    }
    @NonNull
    @Override
    public FiltersDailyLogAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_daily_log_details, parent, false);
        return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FiltersDailyLogAdapter.Viewholder holder, int position) {
        holder.Filter_equip_name.setText(filtersModelList.get(position).getEquip_name());
        holder.Filter_reading.setText(filtersModelList.get(position).getReading_time());

        Picasso.Builder builder = new Picasso.Builder(context);
        Picasso picasso = builder.build();
        String originalImageUrl = filtersModelList.get(position).getFilter_image();
        String trimmedImageUrl = originalImageUrl.replace('~', ' ').trim();
        picasso.load(Uri.parse(Global.baseurl + trimmedImageUrl))
                .error(R.drawable.no_image_available_icon)
                .into(holder.Filter_image);
        holder.Filter_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImage(filtersModelList.get(position).getFilter_image());
            }
        });



        if (filtersModelList.size() > position && filtersModelList.get(position).getFilter_status().equals("S")) {
            holder.Filter_image_upload.setVisibility(View.GONE);
        } else {
            holder.Filter_image_upload.setVisibility(View.VISIBLE);
        }

        holder.Filter_image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.filtersModel = filtersModelList.get(position);
                Intent intent = new Intent(context, FiltersDailyLogImageUploadActivity.class);
                context.startActivity(intent);
               ((Activity) context).finish();
            }
        });
    }

    private void showImage(String filterImage) {
            Dialog builder = new Dialog(context);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // Calculate display dimensions
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;
            String trimmedImageUrl = filterImage.replace('~', ' ').trim();
            Picasso.get().load(Uri.parse(Global.baseurl + trimmedImageUrl)).into(new Target() {
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
        return filtersModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView Filter_equip_name, Filter_reading;
        ImageView Filter_image, Filter_image_upload;
        LinearLayout layout;
        View filterview;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Filter_equip_name = itemView.findViewById(R.id.filter_equip_name);
            Filter_image = itemView.findViewById(R.id.filter_image);
            Filter_reading = itemView.findViewById(R.id.filter_reading);
            Filter_image_upload = itemView.findViewById(R.id.filter_image_upload);
            layout = itemView.findViewById(R.id.senser_image_hide);
            filterview = itemView.findViewById(R.id.filterview);


            String usertype=Global.sharedPreferences.getString("user_type","");

            if (usertype.equals("C")){
                layout.setVisibility(View.GONE);
                filterview.setVisibility(View.GONE);
            }else {
                layout.setVisibility(View.VISIBLE);
                filterview.setVisibility(View.VISIBLE);
            }

        }

    }

}
