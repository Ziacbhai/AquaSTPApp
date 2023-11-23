package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ziac.aquastpapp.R;

public class EquipmentsFragment extends Fragment {
    private CardView Pump ,Meters;
    private String mail,Stpname ,Sitename ,mobile_;
    TextView personnameH ,usersiteH,userstpH  ;
    TextView  usersiteaddressH,Mailid,Mobno;
    //uProcess
    private String Personname  ,SiteAddress,Process;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_equipments, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Pump = view.findViewById(R.id.pump_p);
        Meters = view.findViewById(R.id.moter_s);

        Sitename = sharedPreferences.getString("site_name", "");
        Stpname = sharedPreferences.getString("stp_name", "");
        SiteAddress = sharedPreferences.getString("site_address", "");
        Process = sharedPreferences.getString("process_name", "");
         mail = sharedPreferences.getString("user_email", "");
        mobile_ = sharedPreferences.getString("user_mobile", "");
        Personname = sharedPreferences.getString("person_name", "");

        usersiteH = view.findViewById(R.id.site_name);
        Mailid = view.findViewById(R.id.email);
        Mobno = view.findViewById(R.id._mobile);
        userstpH = view.findViewById(R.id.stp_name);
      //  uProcess = view.findViewById(R.id.processname_);
        usersiteaddressH = view.findViewById(R.id.site_address);
        personnameH = view.findViewById(R.id.person_name);

        Mailid.setText(mail);
        Mobno.setText(mobile_);
        usersiteaddressH.setText(SiteAddress);
        userstpH.setText(Stpname + " / " + Process);
       // userstpH.setText(Stpname);
       // uProcess.setText(Process);
        usersiteH.setText(Sitename);
        personnameH.setText(Personname);

        Pump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PumpsFragment pumpFragment = new PumpsFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, pumpFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        Meters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MetersFragment metersFragment = new MetersFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, metersFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}