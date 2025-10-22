package Fragments;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EquipmentsFragment extends Fragment {
    private CardView Pump, Meters;
    ProgressDialog progressDialog;
    Context context;
    TextView Displaydate ;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipments, container, false);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        context = getContext();
        user_topcard(view);
        Pump = view.findViewById(R.id.pump_p);
        Meters = view.findViewById(R.id.moter_s);
        Displaydate = view.findViewById(R.id.displaydate);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                handler.postDelayed(this, 1000); // Update every 1000 milliseconds (1 second)
            }
        }, 0);

        Pump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PumpsFragment pumpFragment = new PumpsFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, pumpFragment);
                fragmentTransaction.addToBackStack("pumpFragment");
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
                fragmentTransaction.addToBackStack("metersFragment");
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    private void updateDateTime() {
        Date currentDate = new Date();
        // Update date
        SimpleDateFormat dateFormat = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            }
        }
        String formattedDate = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && dateFormat != null) {
            formattedDate = dateFormat.format(currentDate);
        }
        Displaydate.setText(formattedDate);

        SimpleDateFormat timeFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String formattedTime = timeFormat.format(currentDate);
            formattedTime = formattedTime.replace("am", "AM").replace("pm", "PM");

            //Displaytime.setText(formattedTime);
        }

    }


    private void user_topcard(View view) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        String personname, useremail, stpname, sitename, siteaddress, processname, usermobile,stpcapacity;

        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        siteaddress = sharedPreferences.getString("site_address", "");
        processname = sharedPreferences.getString("process_name", "");
        useremail = sharedPreferences.getString("user_email", "");
        usermobile = sharedPreferences.getString("user_mobile", "");
        personname = sharedPreferences.getString("user_name", "");
        stpcapacity = sharedPreferences.getString("stp_capacity", "");

        TextView txtsitename, txtstpname, txtsiteaddress, txtuseremail, txtusermobile, txtpersonname;

        txtsitename = view.findViewById(R.id.sitename);
        txtstpname = view.findViewById(R.id.stpname);
       // txtsiteaddress = view.findViewById(R.id.siteaddress);
        txtuseremail = view.findViewById(R.id.useremail);
        txtusermobile = view.findViewById(R.id.usermobile);
        txtpersonname = view.findViewById(R.id.personname);

        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " " + processname +  " " + stpcapacity);
      //  txtsiteaddress.setText(siteaddress);
        txtuseremail.setText(useremail);
        txtusermobile.setText(usermobile);
        txtpersonname.setText(personname);
    }
}