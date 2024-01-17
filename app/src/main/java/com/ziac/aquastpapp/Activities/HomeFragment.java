package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ziac.aquastpapp.R;


public class HomeFragment extends Fragment {
    PDFView PdfView;
    ProgressDialog progressDialog;
    FloatingActionButton Fab;

    RelativeLayout layoutpump,layoutblower,layoutmeter,layoutsensor,layoutfilter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user_topcard(view);

        layoutpump = view.findViewById(R.id.pumpmotor);
        layoutblower = view.findViewById(R.id.blower);
        layoutmeter = view.findViewById(R.id.meter);
        layoutsensor = view.findViewById(R.id.sensor);
        layoutfilter = view.findViewById(R.id.filter);

        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading please wait...");
        progressDialog.setCancelable(true);



        layoutpump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pump = new Intent(getActivity(), PumpMoterDetails.class);
                startActivity(pump);
            }
        });

        layoutblower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blower = new Intent(getActivity(), BlowersActivity.class);
                startActivity(blower);
            }
        });


        layoutmeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent meter = new Intent(getActivity(), MeterActivity.class);
                startActivity(meter);
            }
        });


        layoutsensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sensors = new Intent(getActivity(), SensorsActivity.class);
                startActivity(sensors);
            }
        });


        layoutfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filter = new Intent(getActivity(), FiltersActivity.class);
                startActivity(filter);
            }
        });


        // PdfView = view.findViewById(R.id.pdfview);
        //Fab = view.findViewById(R.id.fab);
       /* Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("application/pdf");
            }
        });*/
        return view;
    }

    private void user_topcard(View view) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        String personname, useremail, stpname, sitename, siteaddress, processname, usermobile,stpcapacity;
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        siteaddress = sharedPreferences.getString("site_address", "");
        processname = sharedPreferences.getString("process_name", "");
        useremail = sharedPreferences.getString("user_email", "");
        usermobile = sharedPreferences.getString("user_mobile", "");
        personname = sharedPreferences.getString("person_nameu", "");
        stpcapacity = sharedPreferences.getString("stp_capacity", "");

        TextView txtsitename, txtstpname, txtsiteaddress, txtuseremail, txtusermobile, txtpersonname;

        txtsitename = view.findViewById(R.id.sitename);
        txtstpname = view.findViewById(R.id.stpname);
        txtsiteaddress = view.findViewById(R.id.siteaddress);
        txtuseremail = view.findViewById(R.id.useremail);
        txtusermobile = view.findViewById(R.id.usermobile);
        txtpersonname = view.findViewById(R.id.personname);

        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " / " + processname +  " / " + stpcapacity);
        txtsiteaddress.setText(siteaddress);
        txtuseremail.setText(useremail);
        txtusermobile.setText(usermobile);
        txtpersonname.setText(personname);
    }

 /*   private final ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    PdfView.fromUri(uri).load();
                }
            }
    );*/
}