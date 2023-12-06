package Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.Activities.IncidentReportingActivity;
import com.ziac.aquastpapp.Activities.Incident_Image_doc_Select_Activity;
import com.ziac.aquastpapp.Activities.Incident_image_upload_Activity;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.IncidentsClass;
import Models.LabTestClass;

public class Incident_image_upload_Adapter extends RecyclerView.Adapter<Incident_image_upload_Adapter.Viewholder> {

    String userimage;

    Picasso.Builder builder;
    Picasso picasso;
    private ArrayList<IncidentsClass> incidentsClasses;
    private Context context;

    private static List<Uri> imageList = new ArrayList<>();

    public static void updateImageList(List<Uri> newImageList) {
        imageList.clear();
        imageList.addAll(newImageList);
    }
    public Incident_image_upload_Adapter(ArrayList<IncidentsClass> incidentsClasses, Context context) {
        this.incidentsClasses = incidentsClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public Incident_image_upload_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_image_list_upload, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Incident_image_upload_Adapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.In_image_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               userimage = Global.incident_image + incidentsClasses.get(position).getImageList();
//                userimage = Global.userImageurl + Global.sharedPreferences.getString("user_image", "");
                showImage(picasso, userimage);
            }
        });

        holder.In_image_name.setText(incidentsClasses.get(position).getIn_image_name());
        Picasso.Builder builder=new Picasso.Builder(context);
        Picasso picasso=builder.build();
        picasso.load(Uri.parse(Global.incident_image+ incidentsClasses.get(position).getImageList())).error(R.drawable.no_image_available_icon).into(holder.In_image_show);

        holder.Incident_delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Delete Confirmation");
                alertDialogBuilder.setMessage("Are you sure you want to delete?");
                //alertDialogBuilder.setIcon(R.drawable.aqua_logo);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String incident_code = Global.Incident_s.get(position).getInc_No();
                        deleteItem(incident_code);
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
        });
    }

    public void showImage(Picasso picasso, String userimage) {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        builder.setOnDismissListener(dialogInterface -> {
            // Nothing
        });

        // Calculate display dimensions
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Load the image using Picasso
        picasso.load(Uri.parse(userimage)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ImageView imageView = new ImageView((Activity)context);

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
                // Show the dialog after adding the content view
                builder.show();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                // Handle bitmap loading failure
                e.printStackTrace();
                builder.dismiss(); // Dismiss the dialog in case of failure
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Prepare bitmap loading
            }
        });
    }


    public void deleteItem(String incidentcode) {

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        String url = Global.Get_Incidents_delete ;
        url=url+"incident_code="+incidentcode;
        //progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //JSONObject respObj = new JSONObject(response);
                try {
                    String msg = response.getString("msg");
                    boolean isSuccess = response.getBoolean("isSuccess");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Intent intent = new Intent(context, Incident_image_upload_Activity.class);
                context.startActivity(intent);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Global.customtoast(context.getApplicationContext(), getLayoutInflater(),"Failed to get my stock .." + error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }


        };
        queue.add(jsonObjectRequest);

    }

    @Override
    public int getItemCount() {
        return incidentsClasses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView Incident_delete_image,In_image_show;
        TextView In_image_name;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            In_image_show = itemView.findViewById(R.id.in_image_show);
            Incident_delete_image = itemView.findViewById(R.id.incident_delete_image);
            In_image_name = itemView.findViewById(R.id.in_image_name);
        }
    }
}
