package com.ziac.aquastpapp.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziac.aquastpapp.R;


public class FiltersFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_filters, container, false);


        if (Global.isNetworkAvailable(getActivity())) {
        } else {
            Global.customtoast(getActivity(), getLayoutInflater(), "Internet connection lost !!");
        }
        return view;
    }
}