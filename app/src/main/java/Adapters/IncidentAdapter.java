package Adapters;



import static android.app.Activity.RESULT_OK;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;
import com.ziac.aquastpapp.Activities.ProfileActivity;
import com.ziac.aquastpapp.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Models.IncidentsClass;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.Viewholder> {
    private static final int CAMERA_REQUEST =1 ;
    private static final int GALLERY_REQUEST =2;
    private List<IncidentsClass> incidentsClasses;
    Bitmap imageBitmap;

    EditText uName, uNumber, uEmail;
    TextView Uref_code;
    //private static final int CAMERA_REQUEST = 0;


    ImageView Backarrowbtn;
    String userimage;
    Picasso.Builder builder;
    Picasso picasso;
    Context context;

    public IncidentAdapter(Context context, ArrayList<IncidentsClass> incidentsClasses) {
        this.context=context;
        this.incidentsClasses=incidentsClasses;

    }

    @NonNull
    @Override
    public IncidentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_design, parent , false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentAdapter.Viewholder holder, int position) {

        holder.Incedent_Particlulars.setText(incidentsClasses.get(position).getIncidents_Particulars());


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

            }
        });


       /* holder.Inupload.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
            alertDialogBuilder.setTitle("Select an option");
            String[] options = {"Images", "Documents"};

            alertDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            //opencamera();
                            // Images option selected
                            // Do something for Images at position 'clickedPosition'
                            break;
                        case 1:
                            //openDocuments();
                            // Documents option selected
                            // Do something for Documents at position 'clickedPosition'
                            break;
                    }
                }
            });

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle positive button click (e.g., initiate photo upload)
                    // You can add your photo upload logic here
                }
            });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle negative button click (e.g., close the dialog)
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });*/

        holder.Inupload.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

            // Inflate the custom layout
            View customLayout = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog_layout_incident, null);
            alertDialogBuilder.setView(customLayout);

            // Find views in the custom layout
            TextView textViewTitle = customLayout.findViewById(R.id.textViewTitle);
            ListView listViewOptions = customLayout.findViewById(R.id.listViewOptions);
            Button buttonYes = customLayout.findViewById(R.id.buttonYes);
            Button buttonNo = customLayout.findViewById(R.id.buttonNo);

            // Set the title and options
            textViewTitle.setText("Select an option");
            String[] options = {"Images", "Documents"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_list_item_1, options);
            listViewOptions.setAdapter(adapter);

            // Handle option selection

            listViewOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            //opencamera();
                            // Images option selected
                            // Do something for Images at position 'position'
                            break;
                        case 1:
                            // openDocuments();
                            // Documents option selected
                            // Do something for Documents at position 'position'
                            break;
                    }
                }
            });
            // Handle positive button click
            buttonYes.setOnClickListener(view -> {
                // Handle positive button click (e.g., initiate photo upload)
                // You can add your photo upload logic here
            });

            // Handle negative button click
            buttonNo.setOnClickListener(view -> {
                // Handle negative button click (e.g., close the dialog)
                alertDialogBuilder.create().dismiss();
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });


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
        private ImageView Inupload,Ininfo;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Incno=itemView.findViewById(R.id.incident_incno);
            Incedent_Particlulars=itemView.findViewById(R.id.incident_perticulars);
            Inc_date=itemView.findViewById(R.id.incident_date);
            Ininfo=itemView.findViewById(R.id.incident_info);
            Inupload=itemView.findViewById(R.id.incident_photo_upload);

        }
    }
}
