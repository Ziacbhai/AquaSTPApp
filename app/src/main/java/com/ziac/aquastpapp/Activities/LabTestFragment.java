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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
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
import Adapters.LabTestAdapter;
import Models.LabTestClass;


public class LabTestFragment extends Fragment {
    RecyclerView LabTestRecyclerview;
    LabTestClass labTestClass;
    LabTestAdapter labTestAdapter;
    private ProgressDialog progressDialog;
    Context context;
    AppCompatButton Update_A, Cancel_A;
    TextView Ref_no, Ref_date,Rcp_date, Start_date, End_date, tvSelectedDate,Particulars,CustomerRef,RecivedOn;
    String currentDatevalue, currentDateValue2;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lab_test, container, false);

        context = getContext();
        user_topcard(view);

        FloatingActionButton fab = view.findViewById(R.id.fab);

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDetailsDialog(context);
            }
        });

        LabTestRecyclerview = view.findViewById(R.id.labTest_Recyclerview);
        LabTestRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        LabTestRecyclerview.setHasFixedSize(true);
        LabTestRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getLabTestReports();
        return view;
    }

    private void user_topcard(View view) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        String personname, useremail, stpname, sitename, siteaddress, processname, usermobile;
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        siteaddress = sharedPreferences.getString("site_address", "");
        processname = sharedPreferences.getString("process_name", "");
        useremail = sharedPreferences.getString("user_email", "");
        usermobile = sharedPreferences.getString("user_mobile", "");
        personname = sharedPreferences.getString("person_name", "");

        TextView txtsitename, txtstpname, txtsiteaddress, txtuseremail, txtusermobile, txtpersonname;

        txtsitename = view.findViewById(R.id.sitename);
        txtstpname = view.findViewById(R.id.stpname);
        txtsiteaddress = view.findViewById(R.id.siteaddress);
        txtuseremail = view.findViewById(R.id.useremail);
        txtusermobile = view.findViewById(R.id.usermobile);
        txtpersonname = view.findViewById(R.id.personname);

        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " / " + processname);
        txtsiteaddress.setText(siteaddress);
        txtuseremail.setText(useremail);
        txtusermobile.setText(usermobile);
        txtpersonname.setText(personname);
    }

    @SuppressLint("MissingInflatedId")
    private void showAddDetailsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_labtest_layout, null);
        Ref_no = dialogView.findViewById(R.id.lab_refno_update);
        tvSelectedDate = dialogView.findViewById(R.id.test_date);
        Start_date = dialogView.findViewById(R.id.test_Start_Date_update);
        End_date = dialogView.findViewById(R.id.test_Completion_date_update);
        Rcp_date = dialogView.findViewById(R.id.lab_test_date);
        CustomerRef = dialogView.findViewById(R.id.lab_customerRef);
        RecivedOn = dialogView.findViewById(R.id.received_date_update);
        Ref_date = dialogView.findViewById(R.id.lab_ref_date);


        Update_A = dialogView.findViewById(R.id.update_alert_lab);
        Cancel_A = dialogView.findViewById(R.id.cancel_alert_lab);

        tvSelectedDate.setText(currentDateValue2);
        Ref_date.setText(currentDateValue2);
        Start_date.setText(currentDateValue2);
        End_date.setText(currentDateValue2);
        RecivedOn.setText(currentDateValue2);


        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        dialog.show();

        tvSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        Rcp_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        Start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        End_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        Update_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLabTest();
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

    private void updateLabTest() {

        String testdate = currentDatevalue.toString();
        String refno = Ref_no.toString().toString();
        String customer_ref = CustomerRef.getText().toString();
        String starting_date = currentDatevalue.toString();
        String ending_date = currentDatevalue.toString();
        String particulars = Particulars.getText().toString();
        String sample_Received_Date = currentDatevalue.toString();
        String ref_date = currentDatevalue.toString();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Global.updateRepairAddUpdate;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ref_no", refno);
                params.put("test_date", testdate);
                params.put("cus_ref", customer_ref);
                params.put("rcp_date", sample_Received_Date);
                params.put("ref_date", ref_date);
                params.put("sample_desc", particulars);
                params.put("start_date", starting_date);
                params.put("end_date", ending_date);
                params.put("com_code", Global.sharedPreferences.getString("com_code", "0"));
                params.put("ayear", Global.sharedPreferences.getString("ayear", "0"));
                params.put("sstp1_code", Global.sharedPreferences.getString("sstp1_code", "0"));
                params.put("test_code", "0");

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

    private void getLabTestReports() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String labTest = Global.GetLab_Test_Items;
        String comcode = Global.sharedPreferences.getString("com_code", "");
        String ayear = Global.sharedPreferences.getString("ayear", "2023");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "");
        labTest = labTest + "comcode=" + comcode + "&ayear=" + ayear + "&sstp1_code=" + sstp1_code;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, labTest, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.LabTest_Class = new ArrayList<LabTestClass>();
                labTestClass = new LabTestClass();
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
                    labTestClass = new LabTestClass();
                    try {
                        labTestClass.setTRno(e.getString("test_code"));
                        labTestClass.setLabDate(e.getString("test_date"));
                        labTestClass.setRefno(e.getString("ref_no"));
                        labTestClass.setCustomerRef(e.getString("cus_ref"));
                        labTestClass.setLabRefDate(e.getString("ref_date"));

                        labTestClass.setSample_Received_Date(e.getString("rcp_date"));

                        labTestClass.setTest_Start_Date(e.getString("start_date"));
                        labTestClass.setTest_Completion_Date(e.getString("end_date"));

                        labTestClass.setSample_Received_By(e.getString("sample_receivedby"));
                        labTestClass.setSample_Particular(e.getString("sample_desc"));

                        labTestClass.setStatus(e.getString("test_status"));

                      /*  String test_code = labTestClass.getTRno();
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.putString("test_code", test_code);
                        Global.editor.commit();*/

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.LabTest_Class.add(labTestClass);
                    labTestAdapter = new LabTestAdapter(Global.LabTest_Class, getContext());
                    LabTestRecyclerview.setAdapter(labTestAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
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