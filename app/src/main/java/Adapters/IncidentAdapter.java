package Adapters;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
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
import com.ziac.aquastpapp.Activities.IncidentReportingActivity;
import com.ziac.aquastpapp.Activities.Incident_Image_doc_Select_Activity;
import com.ziac.aquastpapp.Activities.Incident_Details_Activity;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Models.IncidentsClass;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.Viewholder> {
    private List<IncidentsClass> incidentsClasses;
    Context context;


    public IncidentAdapter(Context context, ArrayList<IncidentsClass> incidentsClasses) {
        this.context=context;
        this.incidentsClasses=incidentsClasses;
    }
    @NonNull
    @Override
    public IncidentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_design, parent , false);
        Viewholder viewHolder = new Viewholder(view);
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.fade_in);
        viewHolder.itemView.startAnimation(animation);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentAdapter.Viewholder holder, int position) {

        holder.Incedent_Particlulars.setText(incidentsClasses.get(position).getIncidents_Particulars());

        holder.Inc_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Delete Confirmation");
                alertDialogBuilder.setMessage("Are you sure you want to delete?");
               // alertDialogBuilder.setIcon(R.drawable.swasticars_logo);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String incidentcode = Global.Incident_s.get(position).getInc_No();
                        deleteItem(incidentcode);
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


        String conNoString = incidentsClasses.get(position).getInc_No();
        double conNo;
        try {
            conNo = Double.parseDouble(conNoString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        String formattedConNo = removeTrailingZero(conNo);
        holder.Incno.setText(formattedConNo);

        String dateString = incidentsClasses.get(position).getInc_Date();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date;
        try {date = inputFormat.parse(dateString);
            String Date = outputFormat.format(date);
            holder.Inc_date.setText(Date);
        } catch (ParseException e) {e.printStackTrace();
            return;
        }
        holder.Ininfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context != null) {
                    Intent intent = new Intent(context, Incident_Details_Activity.class);
                    context.startActivity(intent);
                } else {
                    Log.e("Info", "Context is null");
                }
            }
        });

        holder.Inupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.editor.putString("incident_code",incidentsClasses.get(position).getInc_No());
                Global.editor.commit();
                    Intent intent = new Intent(context, Incident_Image_doc_Select_Activity.class);
                    context.startActivity(intent);
            }
        });

    }

    public void deleteItem(String incident_code2) {

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        String url = Global.Get_Incidents_delete ;
        url=url+"incident_code="+incident_code2;
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

                Intent intent = new Intent(context, IncidentReportingActivity.class);
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



    private String removeTrailingZero(double value) {
        // Convert the double to a string
        String formattedValue = String.valueOf(value);

        // Remove trailing zero if it's a decimal number
        if (formattedValue.indexOf(".") > 0) {
            formattedValue = formattedValue.replaceAll("0*$", "").replaceAll("\\.$", "");
        }

        return formattedValue;
    }

    @Override
    public int getItemCount() {
        return incidentsClasses.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView Incno,Incedent_Particlulars,Inc_date;
        private ImageView Inupload,Ininfo,Inc_delete;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Incno=itemView.findViewById(R.id.incident_incno);
            Incedent_Particlulars=itemView.findViewById(R.id.incident_perticulars);
            Inc_date=itemView.findViewById(R.id.incident_date);
            Ininfo=itemView.findViewById(R.id.incident_info);
            Inupload=itemView.findViewById(R.id.incident_photo_upload);
            Inc_delete=itemView.findViewById(R.id.incident_delete);


        }
    }
}
