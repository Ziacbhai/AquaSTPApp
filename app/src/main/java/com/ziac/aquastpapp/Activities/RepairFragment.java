package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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

import Adapters.RepairAdapter;

import Models.RepairClass1;


public class RepairFragment extends Fragment {

    RepairClass1 repairClass1;
    RepairAdapter repairAdapter;
    RecyclerView RepairRecyclerview;
    TextView tvSelectedDate, Remark_A;
    String currentDatevalue, currentDateValue2;
    ProgressDialog progressDialog;
    Context context;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repair, container, false);
        context = getContext();
        user_topcard(view);
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading please wait...");
        progressDialog.setCancelable(true);

        FloatingActionButton fab = view.findViewById(R.id.fab);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshScreen();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDetailsDialog(context);
            }
        });

        RepairRecyclerview = view.findViewById(R.id.repair_recyclerview);
        RepairRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        RepairRecyclerview.setHasFixedSize(true);
        RepairRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

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

        /*Global.sharedPreferences.edit();
        Global.editor.putString("current_date",currentDatevalue);
        Global.editor.commit();*/

        getRepair();
        return view;
    }

    private void refreshScreen() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                Global.repair1list.clear();
                RepairAdapter repairAdapter1 = new RepairAdapter(Global.repair1list, context);
                RepairRecyclerview.setAdapter(repairAdapter1);
                repairAdapter1.notifyDataSetChanged();
                getRepair();
            }
        }, 2000);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_repair_layout, null);
        AppCompatButton Update_A, Cancel_A;
        tvSelectedDate = dialogView.findViewById(R.id.tvSelectedDate);
        Remark_A = dialogView.findViewById(R.id.remark_alert_r);
        Update_A = dialogView.findViewById(R.id.update_alert_r);
        Cancel_A = dialogView.findViewById(R.id.cancel_alert_r);

        tvSelectedDate.setText(currentDateValue2);
        // tvSelectedDate.setText(currentDatevalue);
        tvSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                showDatePicker();
            }
        });
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        dialog.show();

        Update_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRepairs();
                dialog.dismiss();
            }
        });
        Cancel_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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

    // Update the TextView with the selected date
    private void updateDateTextView(int year, int month, int day) {
      /*  String selectedDate = day + "-" + (month + 1) + "-" + year;
        tvSelectedDate.setText(selectedDate);*/
        String current_date = day + "-" + month + "-" + year;
        tvSelectedDate.setText(current_date);
    }

    private void getRepair() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String repair = Global.GetRepairItems;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String ayear = Global.sharedPreferences.getString("ayear", "2023");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        repair = repair + "comcode=" + com_code + "&ayear=" + ayear + "&sstp1_code=" + sstp1_code;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, repair, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.repair1list = new ArrayList<RepairClass1>();
                repairClass1 = new RepairClass1();

                try {
                    JSONArray jarray = response.getJSONArray("data");

                    if (jarray.length() > 0) {
                        for (int i = 0; i < jarray.length(); i++) {
                            final JSONObject e = jarray.getJSONObject(i);
                            repairClass1 = new RepairClass1();

                            repairClass1.setREPNo(e.getString("rep_no"));
                            repairClass1.setRepair_Amount(e.getString("repaired_amt"));
                            repairClass1.setRepair_Date(e.getString("rep_date"));
                            repairClass1.setRepair_code(e.getString("repair1_code"));
                            repairClass1.setRemark(e.getString("remarks"));
                            repairClass1.setR_createdby(e.getString("createdby"));


                            /*String repair1_code = repairClass1.getRepair_code();
                            Global.editor = Global.sharedPreferences.edit();
                            Global.editor.putString("repair1_code", repair1_code);
                            Global.editor.commit();*/

                            Global.repair1list.add(repairClass1);
                        }

                        RepairAdapter repairAdapter = new RepairAdapter(Global.repair1list, getContext());
                        RepairRecyclerview.setAdapter(repairAdapter);
                        hideProgressDialog();
                    } else {
                        // Display toast message for no data
                        Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                Toast.makeText(getContext(), "Error fetching repair data", Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
            /*params.put("rep_no", Repno.getText().toString());
            params.put("repaired_amt",Amount.getText().toString());
            params.put("rep_date", RepairDate.getText().toString());*/
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }


    private void updateRepairs() {

        String remarks = Remark_A.getText().toString();
        String condate = currentDatevalue.toString();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Global.updateRepairAddUpdate;
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
                        getRepair();
                    } else {
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
                params.put("rep_date", condate);
                params.put("remarks", remarks);
                params.put("com_code", Global.sharedPreferences.getString("com_code", "0"));
                params.put("ayear", Global.sharedPreferences.getString("ayear", "0"));
                params.put("sstp1_code", Global.sharedPreferences.getString("sstp1_code", "0"));
                params.put("repair1_code", "0");
                // params.put("repair1_code", Global.sharedPreferences.getString("repair1_code", "0"));
                Log.d("rep_date", "rep_date: " + params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);

    }

    private void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}