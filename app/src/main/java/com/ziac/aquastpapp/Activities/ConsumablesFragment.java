package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.ConsumablesAdapter;
import Models.ConsumablesClass;

public class ConsumablesFragment extends Fragment {

    ConsumablesClass  consumables_Class;
    RecyclerView Consumables_rv;
    private AppCompatButton btnOpenDatePicker;
    private TextView tvSelectedDate;
    TextView Date_A,STP_A,Remark_A;
    AppCompatButton Update_A,Cancel_A;

    TextView usersiteH,userstpH,usersiteaddressH ,Mailid,Mobno,personnameH;
    private String Personname,Mail,Stpname ,Sitename ,SiteAddress,Process,Mobile;

    private ProgressDialog progressDialog;


    Context context;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_consumables, container, false);

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
        Mail = sharedPreferences.getString("user_email", "");
        Mobile = sharedPreferences.getString("user_mobile", "");
        Personname = sharedPreferences.getString("person_name", "");

        usersiteH = view.findViewById(R.id.site_name);
        userstpH = view.findViewById(R.id.stp_name);
        usersiteaddressH = view.findViewById(R.id.site_address);
        Mailid = view.findViewById(R.id.email);
        Mobno = view.findViewById(R.id._mobile);
        personnameH = view.findViewById(R.id.person_name);

        FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDetailsDialog(context);
            }
        });

        usersiteH.setText(Sitename);
        userstpH.setText(Stpname + " / " + Process);
        usersiteaddressH.setText(SiteAddress);
        Mailid.setText(Mail);
        Mobno.setText(Mobile);
        personnameH.setText(Personname);

        Consumables_rv = view.findViewById(R.id.consumables_recyclerview);
        Consumables_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        Consumables_rv.setHasFixedSize(true);
        Consumables_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getConsumables();
        return view;
    }

    private void showAddDetailsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_repair_layout, null);

       // btnOpenDatePicker = dialogView.findViewById(R.id.btnOpenDatePicker);
        tvSelectedDate = dialogView.findViewById(R.id.tvSelectedDate);
        Remark_A = dialogView.findViewById(R.id.remark_alert_r);
        Update_A = dialogView.findViewById(R.id.update_alert_r);
        Cancel_A = dialogView.findViewById(R.id.cancel_alert_r);

        tvSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();



        dialog.show();

        Update_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle update button click
                // You can add your logic here
                dialog.dismiss(); // Close the dialog if needed
            }
        });

        Cancel_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle cancel button click
                // You can add your logic here
                dialog.dismiss(); // Close the dialog if needed
            }
        });
    }

    private void showDatePicker() {
        // Use the current date as the initial date
        Calendar calendar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
        }
        int initialYear = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            initialYear = calendar.get(Calendar.YEAR);
        }
        int initialMonth = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            initialMonth = calendar.get(Calendar.MONTH);
        }
        int initialDay = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            initialDay = calendar.get(Calendar.DAY_OF_MONTH);
        }

        // Create a DatePickerDialog and set the listener
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update your TextView with the selected date
                        updateDateTextView(year, month, dayOfMonth);
                    }
                },
                initialYear,
                initialMonth,
                initialDay
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        // Show the date picker dialog
        datePickerDialog.show();
    }
    private void updateDateTextView(int year, int month, int day) {
        String selectedDate = day + "-" + (month + 1) + "-" + year;
        tvSelectedDate.setText(selectedDate);
    }


    @SuppressLint("MissingInflatedId")

    private void getConsumables() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String  consumables = Global.Get_Consumables;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String ayear = Global.sharedPreferences.getString("ayear", "2023");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        consumables = consumables + "comcode=" + com_code + "&ayear=" +  ayear + "&sstp1_code=" + sstp1_code ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, consumables, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Consumables_s = new ArrayList<ConsumablesClass>();
                consumables_Class = new ConsumablesClass();
                JSONArray jarray;
                try {
                    jarray = response.getJSONArray("data");

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < jarray.length(); i++) {
                    final JSONObject e;
                    try {
                        e = jarray.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    consumables_Class = new ConsumablesClass();
                    try {
                        consumables_Class.setCon_no(e.getString("con1_code"));
                        consumables_Class.setDate(e.getString("con_date"));
                        consumables_Class.setAmount(e.getString("con_amt"));
                        consumables_Class.setRemark(e.getString("remarks"));

                        String Con_code = consumables_Class.getCon_no();
                        // System.out.println(repair_code);
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.putString("con1_code", Con_code);
                        Global.editor.commit();
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Consumables_s.add(consumables_Class);
                    ConsumablesAdapter consumablesAdapter = new ConsumablesAdapter(getContext(),Global.Consumables_s);
                    Consumables_rv.setAdapter(consumablesAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            public Map<String, String> getHeaders() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
        queue.add(jsonObjectRequest);

    }
}