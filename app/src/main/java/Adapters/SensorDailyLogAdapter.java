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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ziac.aquastpapp.Activities.FiltersDailyLogActivity;
import com.ziac.aquastpapp.Activities.FiltersDailyLog_Image_Upload_Activity;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.Activities.SensorsDailyLog_Image_Upload_Activity;
import com.ziac.aquastpapp.R;

import java.util.List;

import Models.SensorsModelClass;

public class SensorDailyLogAdapter extends RecyclerView.Adapter<SensorDailyLogAdapter.Viewholder> {


    private List<SensorsModelClass> sensorsModelClassList;
    Context context;

    public SensorDailyLogAdapter(List<SensorsModelClass> sensorsModelClassList, Context context) {
        this.sensorsModelClassList = sensorsModelClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public SensorDailyLogAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensors_daily_log_deatils, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorDailyLogAdapter.Viewholder holder, int position) {
        holder.Sensor_equip_name.setText(sensorsModelClassList.get(position).getEquip_name());
        holder.Sensor_reading.setText(sensorsModelClassList.get(position).getReading());
        holder.Sensor_reading_time.setText(sensorsModelClassList.get(position).getReading_time());
        holder.Sensor_total.setText(sensorsModelClassList.get(position).getSensor_total());

        Picasso.Builder builder = new Picasso.Builder(context);
        Picasso picasso = builder.build();
        String originalImageUrl = sensorsModelClassList.get(position).getSensor_image();
        String trimmedImageUrl = originalImageUrl.replace('~', ' ').trim();
        picasso.load(Uri.parse(Global.baseurl + trimmedImageUrl))
                .error(R.drawable.no_image_available_icon)
                .into(holder.Sensor_image);

        double rawValue = Double.parseDouble(sensorsModelClassList.get(position).getReading());
        String formattedValue = removeTrailingZeros(rawValue);
        holder.Sensor_reading.setText(formattedValue);



        holder.Sensor_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(sensorsModelClassList.get(position).getSensor_image());
            }
        });

       holder.Sensor_image_upload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Global.sensorclass = sensorsModelClassList.get(position);
               Intent intent = new Intent(context, SensorsDailyLog_Image_Upload_Activity.class);
               context.startActivity(intent);
               ((Activity) context).finish();
           }
       });

    }

    private String removeTrailingZeros(double value) {
        String stringValue = String.valueOf(value);
        stringValue = stringValue.replaceAll("0*$", "").replaceAll("\\.$", "");

        return stringValue;
    }

    private void showImage(String sensorImage) {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // Calculate display dimensions
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        String trimmedImageUrl = sensorImage.replace('~', ' ').trim();
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
        return sensorsModelClassList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView Sensor_image,Sensor_image_upload;

        TextView Sensor_equip_name, Sensor_reading, Sensor_reading_time, Sensor_total;

        View viewhide2,viewhide;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Sensor_equip_name = itemView.findViewById(R.id.sensor_name);
            Sensor_reading = itemView.findViewById(R.id.sensor_reading);
            Sensor_reading_time = itemView.findViewById(R.id.sensor_reading_time);
            Sensor_total = itemView.findViewById(R.id.sensor_total);
            Sensor_image = itemView.findViewById(R.id.sensor_image);
            Sensor_image_upload = itemView.findViewById(R.id.sensor_image_upload_btn);
            viewhide = itemView.findViewById(R.id.viewhide);
            viewhide2 = itemView.findViewById(R.id.viewhide2);

            String usertype=Global.sharedPreferences.getString("user_type","");
            if (usertype.equals("C")){
                Sensor_image_upload.setVisibility(View.GONE);
                viewhide.setVisibility(View.GONE);
            }else {
                Sensor_image_upload.setVisibility(View.VISIBLE);
                Sensor_equip_name.setVisibility(View.VISIBLE);
                Sensor_reading_time.setVisibility(View.VISIBLE);
                Sensor_reading.setVisibility(View.VISIBLE);
                viewhide.setVisibility(View.VISIBLE);

            }
        }
    }
}
