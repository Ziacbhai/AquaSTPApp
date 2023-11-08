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
    private String userimage,mail,Stpname ,Sitename ,Siteaddress ,mobile_;
    TextView personnameH,usermailH ,usersiteH,userstpH ,Mobile ,uderaddress ,mobile;
    private TextView ReferenceCode,Companyname,Personname, Mailid, Mobno,AlterMobileno;
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

        String dealername = Global.sharedPreferences.getString("person_name", "");
        String mail = Global.sharedPreferences.getString("user_email", "");
        String mobile = Global.sharedPreferences.getString("user_mobile", "");

        Personname = view.findViewById(R.id.person);
        Mailid = view.findViewById(R.id.email);
        Mobno = view.findViewById(R.id._mobile);

        Personname.setText(dealername);
        Mailid.setText(mail);
        Mobno.setText(mobile);

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