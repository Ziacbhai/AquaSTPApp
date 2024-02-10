package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziac.aquastpapp.R;

import Models.IncidentsClass;

public class Incident_Image_doc_Select_Activity extends AppCompatActivity {

    Context context;
    CardView In_image_, In_docs_;
    private ProgressDialog progressDialog;
    ImageView Repair_back_btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_image_list);
        context = this;
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);
        In_image_ = findViewById(R.id.in_images_);
        //In_docs_ = findViewById(R.id.in_docs_);

        Repair_back_btn = findViewById(R.id.repair_back_btn);
        Repair_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        In_image_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Incident_Image_doc_Select_Activity.this, Incident_image_upload_Activity.class));
            }
        });
    }
}