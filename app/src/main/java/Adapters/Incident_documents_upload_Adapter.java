package Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.Activities.Incident_image_upload_Activity;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.IncidentsClass;

public class Incident_documents_upload_Adapter extends RecyclerView.Adapter<Incident_documents_upload_Adapter.Viewholder> {
    String userimage;

    Picasso.Builder builder;
    Picasso picasso;


    private Context context;

    private ArrayList<IncidentsClass> incidentsClasses;

    public Incident_documents_upload_Adapter(Context context, ArrayList<IncidentsClass> incidentsClasses) {
        this.context = context;
        this.incidentsClasses = incidentsClasses;
    }

    @NonNull
    @Override
    public Incident_documents_upload_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_docs_list_upload, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Incident_documents_upload_Adapter.Viewholder holder, int position) {

        IncidentsClass incidentsClass = incidentsClasses.get(position);
        holder.In_doc_name.setText(incidentsClass.getInc_doc_name());
        holder.Incident_delete_doc.setOnClickListener(new View.OnClickListener() {
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

        Picasso.Builder builder=new Picasso.Builder(context);
        Picasso picasso=builder.build();
        picasso.load(Uri.parse(Global.incident_image + incidentsClasses.get(position).getImageList())).error(R.drawable.no_image_available_icon).into(holder.In_doc_show);
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

        ImageView Incident_delete_doc,In_doc_show;
        TextView In_doc_name;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            In_doc_show = itemView.findViewById(R.id.in_doc_show);
            Incident_delete_doc = itemView.findViewById(R.id.incident_delete_doc);
            In_doc_name = itemView.findViewById(R.id.in_doc_name);
        }
    }
}
