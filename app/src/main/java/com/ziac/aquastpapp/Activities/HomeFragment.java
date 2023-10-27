package com.ziac.aquastpapp.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ziac.aquastpapp.R;


public class HomeFragment extends Fragment {
    private RelativeLayout Blower,Pump,Meter,Sensor,Filter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_home, container, false);

        Pump = view.findViewById(R.id.pump);
        Blower = view.findViewById(R.id.blower);
        Meter=view.findViewById(R.id.meter);
        Sensor=view.findViewById(R.id.sensor);
        Filter=view.findViewById(R.id.filter);



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

        Blower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlowersFragment blowersFragment = new BlowersFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, blowersFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Meter.setOnClickListener(new View.OnClickListener() {
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

        Sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorsFragment sensorsFragment = new SensorsFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, sensorsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltersFragment filtersFragment = new FiltersFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, filtersFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return view;
    }
}