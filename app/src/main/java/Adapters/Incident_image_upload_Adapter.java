package Adapters;

import android.annotation.SuppressLint;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.R;

import java.util.ArrayList;

import Models.IncidentsModelClass;

public class Incident_image_upload_Adapter extends RecyclerView.Adapter<Incident_image_upload_Adapter.Viewholder> {
    private ArrayList<IncidentsModelClass> incidentsModelClasses;
    private Context context;
    public Incident_image_upload_Adapter(ArrayList<IncidentsModelClass> incidentsModelClasses, Context context) {
        this.incidentsModelClasses = incidentsModelClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public Incident_image_upload_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_image_list_upload, parent, false);
        Viewholder viewHolder = new Viewholder(view);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        viewHolder.itemView.startAnimation(animation);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Incident_image_upload_Adapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {

        Global.loadWithPicasso(context, holder.In_image_show, Global.incident_image + incidentsModelClasses.get(position).getImageList());
//        picasso.load(Uri.parse(Global.incident_image + incidentsClasses.get(position).getImageList()))
//                .error(R.drawable.no_image_available_icon).into(holder.In_image_show);

        holder.In_image_name.setText(incidentsModelClasses.get(position).getIn_image_name());

        holder.In_image_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(incidentsModelClasses.get(position).getImageList());
            }
        });

       /* holder.Incident_delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Delete Confirmation");
                alertDialogBuilder.setMessage("Are you sure you want to delete?");
                //alertDialogBuilder.setIcon(R.drawable.aqua_logo);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String incident_code = Global.Incident_s.get(position).getDelete_Incident_code2();
                        deleteItem(incident_code, position);
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing (dismiss the dialog)
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });*/

    }

   /* public void deleteItem(String incidentcode, int position) {

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        String url = Global.Get_Incidents_delete;
        url = url + "id=" + incidentcode;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    JSONObject jsonResponse;
                    try {
                        jsonResponse = new JSONObject(response);
                        String msg = jsonResponse.getString("error");
                        boolean isSuccess = jsonResponse.getBoolean("isSuccess");
                        if (isSuccess) {
                            incidentsClasses.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Images deleted successfully!!!", Toast.LENGTH_SHORT).show();
                            //  Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                },
                error -> {
                    Toast.makeText(context, "Unable to delete Images!!!", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accessToken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // params.put("Image_code", vehimage);
                // Log.d("MyTag", "Image Code: " + vehimage);
                return params;
            }
        };

//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0, // timeout in milliseconds
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
        queue.add(stringRequest);
    }*/

    private void showImage(String imageUrl) {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Load the image using Picasso or your preferred image loading library
        Picasso.get().load(Uri.parse(Global.incident_image + imageUrl)).into(new Target() {
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
        return incidentsModelClasses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView Incident_delete_image, In_image_show;
        TextView In_image_name;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            In_image_show = itemView.findViewById(R.id.in_image_show);
          //  Incident_delete_image = itemView.findViewById(R.id.incident_delete_image);
            In_image_name = itemView.findViewById(R.id.in_image_name);

        }
    }

}
