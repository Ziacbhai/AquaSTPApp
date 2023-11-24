package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziac.aquastpapp.R;

import Models.CommonModelClass;
import Models.zList;


public class LabTestFragment extends Fragment {
    private zList company_code ,location_code,stp1_code;
    RecyclerView LabTestRecyclerview;
    // private ImageView mImageView;
    private String userimage;
    TextView usersiteH,userstpH,usersiteaddressH ,Mailid,Mobno,personnameH;
    private String Personname,mail,Stpname ,Sitename ,SiteAddress,Process;
    private TextView Manufacturer,EquipmentName,Specification,EquipmentNumber_Id,Rating_Capacity,
            FormFactor,Phase,CleaningRunningFrequencyHRS ,Address_M ,Process_name_;
    CommonModelClass commonModelClassList;
    private ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_lab_test, container, false);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (!Global.isNetworkAvailable(getActivity())) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Internet connection lost !!");
        }
        new InternetCheckTask().execute();

        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        Sitename = sharedPreferences.getString("site_name", "");
        Stpname = sharedPreferences.getString("stp_name", "");
        SiteAddress = sharedPreferences.getString("site_address", "");
        Process = sharedPreferences.getString("process_name", "");
        String mail = Global.sharedPreferences.getString("user_email", "");
        String mobile = Global.sharedPreferences.getString("user_mobile", "");
        Personname = sharedPreferences.getString("person_name", "");

        usersiteH = view.findViewById(R.id.site_name);
        userstpH = view.findViewById(R.id.stp_name);
        usersiteaddressH = view.findViewById(R.id.site_address);
        //uProcess = view.findViewById(R.id.processname_);
        Mailid = view.findViewById(R.id.email);
        Mobno = view.findViewById(R.id._mobile);
        personnameH = view.findViewById(R.id.person_name);

        usersiteH.setText(Sitename);
        userstpH.setText(Stpname + " / " + Process);
        usersiteaddressH.setText(SiteAddress);
        //uProcess.setText(Process);
        Mailid.setText(mail);
        Mobno.setText(mobile);
        personnameH.setText(Personname);

        LabTestRecyclerview = view.findViewById(R.id.labTestRecyclerview);
        LabTestRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        LabTestRecyclerview.setHasFixedSize(true);
        LabTestRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getLabTestReports();
        return view;
    }

    private void getLabTestReports() {
    }
}