package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Adapters.ConsumptionAdapter;
import Adapters.Consumption_Details_Adapter;
import Models.ConsumptionClass;

public class Consumption_Fragment extends Fragment {


    RecyclerView Consumables_rv;
    private TextView tvSelectedDate;
    TextView Date_A, STP_A, Remark_A;
    AppCompatButton Update_A, Cancel_A;
    ProgressDialog progressDialog;
    String currentDatevalue, currentDateValue2;
    Context context;
    ConsumptionAdapter consumptionAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consumption, container, false);

        context = getContext();
        user_topcard(view);

        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading please wait...");
        progressDialog.setCancelable(true);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshScreen();
            }
        });


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDetailsDialog(context);
            }
        });

        Consumables_rv = view.findViewById(R.id.consumables_recyclerview);
        Consumables_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        Consumables_rv.setHasFixedSize(true);
        Consumables_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Consumables_rv.setAdapter(consumptionAdapter);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            currentDatevalue = dateFormat1.format(currentDate);
        }
        SimpleDateFormat dateFormat2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dateFormat2 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            currentDateValue2 = dateFormat2.format(currentDate);
        }
//        Global.sharedPreferences.edit();
//        Global.editor.putString("current_date",currentDatevalue);
//        Global.editor.commit();
        getConsumables();
        return view;

    }
     private void refreshScreen() {
         new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
             @Override
             public void run() {
                 swipeRefreshLayout.setRefreshing(false);
                 Global.Consumption1list.clear();
                 ConsumptionAdapter consumablesAdapter = new ConsumptionAdapter(Global.Consumption1list, context);
                 Consumables_rv.setAdapter(consumablesAdapter);
                 consumablesAdapter.notifyDataSetChanged();
                 getConsumables();
             }
         },2000);

     }

    private void user_topcard(View view) {
        String personname, useremail, stpname, sitename, siteaddress, processname, usermobile,stpcapacity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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

    @SuppressLint("MissingInflatedId")
    private void showAddDetailsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_consumption_layout, null);
        // btnOpenDatePicker = dialogView.findViewById(R.id.btnOpenDatePicker);
        tvSelectedDate = dialogView.findViewById(R.id.tvSelectedDate);
        Remark_A = dialogView.findViewById(R.id.remark_alert);
        Update_A = dialogView.findViewById(R.id.update_alert);
        Cancel_A = dialogView.findViewById(R.id.cancel_alert);

        // tvSelectedDate.setText(currentDatevalue);
        tvSelectedDate.setText(currentDateValue2);
        tvSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showDatePicker();
            }
        });
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        dialog.show();

        Update_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateConsumables();
                dialog.dismiss();
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

    @SuppressLint("MissingInflatedId")
    private void getConsumables() {
        showProgressDialog();
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String consumablesUrl = Global.Get_Consumables;

        String comCode = Global.sharedPreferences.getString("com_code", "0");
        String aYear = Global.sharedPreferences.getString("ayear", "2023");
        String sstp1Code = Global.sharedPreferences.getString("sstp1_code", "0");

        consumablesUrl += "comcode=" + comCode + "&ayear=" + aYear + "&sstp1_code=" + sstp1Code;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, consumablesUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            Global.Consumption1list = new ArrayList<>();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                ConsumptionClass consumablesClass = new ConsumptionClass();

                                consumablesClass.setCon1_code(dataObject.getString("con1_code"));
                                consumablesClass.setDate(dataObject.getString("con_date"));
                                consumablesClass.setAmount(dataObject.getString("con_amt"));
                                consumablesClass.setRemark(dataObject.getString("remarks"));
                                consumablesClass.setCreated_by(dataObject.getString("createdby"));
                                consumablesClass.setCon_no(dataObject.getString("con_no"));

                                Global.Consumption1list.add(consumablesClass);
                            }

                            if (Global.Consumption1list.isEmpty()) {
                                Toast.makeText(context, "No Data Available", Toast.LENGTH_SHORT).show();
                            } else {
                                consumptionAdapter = new ConsumptionAdapter(Global.Consumption1list, context);
                                Consumables_rv.setAdapter(consumptionAdapter);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } finally {
                            hideProgressDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accessToken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };

        queue.add(jsonObjectRequest);
    }
    private void updateConsumables() {

        String remarks = Remark_A.getText().toString();
        String condate = currentDatevalue.toString();
        // String Con_code = consumables_Class.getCon_no();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Global.updateConsumption;
        // Toast.makeText(context, "network error", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String sresponse) {

                JSONObject response;
                try {
                    response = new JSONObject(sresponse);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (response.getBoolean("isSuccess")) {
                        Toast.makeText(getActivity(), "Updated successfully !!", Toast.LENGTH_SHORT).show();
                        getConsumables();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), response.getString("error"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "failed to upload", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("con_date", condate);
                // params.put("con_date", sharedPreferences.getString("current_date",""));
                params.put("remarks", remarks);
                params.put("com_code", Global.sharedPreferences.getString("com_code", "0"));
                params.put("ayear", Global.sharedPreferences.getString("ayear", "0"));
                params.put("sstp1_code", Global.sharedPreferences.getString("sstp1_code", "0"));
                params.put("con1_code", "0");

                Log.d("params", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);

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
                        updateDateTextView(year, month + 1, dayOfMonth);
                        currentDatevalue = year + "-" + (month + 1) + "-" + dayOfMonth;
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
        String current_date = day + "-" + month + "-" + year;
        tvSelectedDate.setText(current_date);
        /*Global.sharedPreferences.edit();
        Global.editor.putString("current_date",current_date);
        Global.editor.commit();*/
    }

    private void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}