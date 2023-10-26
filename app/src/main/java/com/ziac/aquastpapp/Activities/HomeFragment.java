package com.ziac.aquastpapp.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
                startActivity(new Intent(getActivity(), PumpActivity.class));
            }
        });

        Blower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BlowerActivity.class));
            }
        });

        Meter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MeterActivity.class));
            }
        });

        Sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SensorActivity.class));
            }
        });

        Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FilterActivity.class));
            }
        });

        return view;
    }
}