package Fragments;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.Activities.IncidentImageUploadActivity;
import com.ziac.aquastpapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Models.IncidentsModelClass;
public class IncidentReportingFragment extends Fragment {
    RecyclerView Incident_recyclerview;
    Context context;
    IncidentsModelClass incidents;
    ProgressDialog progressDialog;
    IncidentAdapter incidentAdapter;
    private TextView tvSelectedDate;
    TextInputEditText Remark_A;
    MaterialButton Update_A, Cancel_A;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton AddIncedent;
    String currentDatevalue, currentDateValue2;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incident_reporting, container, false);

        context = getContext();
        user_topcard(view);

        AddIncedent = view.findViewById(R.id.incedentfab);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshScreen();
            }
        });
        AddIncedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDetailsDialog(context);
            }
        });


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


        Incident_recyclerview = view.findViewById(R.id.fragment_incident_recyclerview);

        Incident_recyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Incident_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        if (dy > 0 || dy < 0 && AddIncedent.isShown()) {
                            AddIncedent.hide();
                        }
                    }

                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            AddIncedent.show();
                        }
                        super.onScrollStateChanged(recyclerView, newState);
                    }
                });
            }
        });


        Incident_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        Incident_recyclerview.setHasFixedSize(true);
        Incident_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getIncidentReport();
        return view;

    }

    private void user_topcard(View view) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        TextView txtsitename, txtstpname, txtsiteaddress, txtuseremail, txtusermobile, txtpersonname;

        txtsitename = view.findViewById(R.id.sitename);
        txtstpname = view.findViewById(R.id.stpname);
        // txtsiteaddress = view.findViewById(R.id.siteaddress);
        txtuseremail = view.findViewById(R.id.useremail);
        txtusermobile = view.findViewById(R.id.usermobile);
        txtpersonname = view.findViewById(R.id.personname);

        txtsitename.setText(sharedPreferences.getString("site_name", ""));
        txtstpname.setText(sharedPreferences.getString("stp_name", "") +
                " " + sharedPreferences.getString("process_name", "") + " " +
                sharedPreferences.getString("stp_capacity", ""));
        //  txtsiteaddress.setText(sharedPreferences.getString("site_address", ""));
        txtuseremail.setText(sharedPreferences.getString("user_email", ""));
        txtusermobile.setText(sharedPreferences.getString("user_mobile", ""));
        txtpersonname.setText(sharedPreferences.getString("user_name", ""));
    }

    private void refreshScreen() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                Global.Incident_Class.clear();
                IncidentAdapter incidentAdapter1 = new IncidentAdapter(context, Global.Incident_Class);
                Incident_recyclerview.setAdapter(incidentAdapter1);
                incidentAdapter1.notifyDataSetChanged();
                getIncidentReport();
            }
        }, 2000);

    }

    @SuppressLint("MissingInflatedId")
    private void showAddDetailsDialog(Context context) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_incident_layout, null);

        tvSelectedDate = dialogView.findViewById(R.id.tvSelectedDate);
        Remark_A = dialogView.findViewById(R.id.incident_particulars);
        Update_A = dialogView.findViewById(R.id.incident_update_alert);
        Cancel_A = dialogView.findViewById(R.id.incident_cancel_alert);

        tvSelectedDate.setText(currentDateValue2);

        Update_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateIncident();
                bottomSheetDialog.dismiss();
            }
        });

        Cancel_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
    }

    private void updateIncident() {
        String incident_desc = Remark_A.getText().toString();
        String incident_date = currentDatevalue.toString();
        // String Con_code = consumables_Class.getCon_no();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Global.updateIncidents;

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
                        getIncidentReport();
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
                params.put("incident_date", incident_date);
                params.put("incident_desc", incident_desc);
                params.put("com_code", Global.sharedPreferences.getString("com_code", "0"));
                params.put("ayear", Global.sharedPreferences.getString("ayear", "0"));
                params.put("sstp1_code", Global.sharedPreferences.getString("sstp1_code", "0"));
                params.put("site_code", Global.sharedPreferences.getString("site_code", "0"));
                params.put("incident_code", "0");
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

    private void getIncidentReport() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String incident = Global.Get_Incidents;
        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String ayear = Global.sharedPreferences.getString("ayear", "2023");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        incident = incident + "comcode=" + com_code + "&ayear=" + ayear + "&sstp1_code=" + sstp1_code;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, incident, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Incident_Class = new ArrayList<IncidentsModelClass>();
                incidents = new IncidentsModelClass();
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
                    incidents = new IncidentsModelClass();
                    try {
                        incidents.setIncident_Code(e.getString("incident_code"));
                        incidents.setIncident_Date(e.getString("incident_date"));
                        incidents.setIncidents_Particulars(e.getString("incident_desc"));
                        incidents.setInc_Created_by(e.getString("createdby"));
                        incidents.setIncident_no(e.getString("incident_no"));

                       /* String incident_code = incidents.getIncident_Code();
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.putString("incident_code", incident_code);
                        Global.editor.commit();*/

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Incident_Class.add(incidents);
                    incidentAdapter = new IncidentAdapter(requireContext(), Global.Incident_Class);
                    Incident_recyclerview.setAdapter(incidentAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.Viewholder> {
        private List<IncidentsModelClass> incidentsModelClasses;
        Context context;

        public IncidentAdapter(Context context, ArrayList<IncidentsModelClass> incidentsModelClasses) {
            this.context = context;
            this.incidentsModelClasses = incidentsModelClasses;
        }

        @NonNull
        @Override
        public IncidentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident_design, parent, false);
            Viewholder viewHolder = new Viewholder(view);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            viewHolder.itemView.startAnimation(animation);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull IncidentAdapter.Viewholder holder, int position) {

            holder.Incedent_Particlulars.setText(incidentsModelClasses.get(position).getIncidents_Particulars());
/*
        holder.Inc_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Delete Confirmation");
                alertDialogBuilder.setMessage("Are you sure you want to delete?");
                // alertDialogBuilder.setIcon(R.drawable.swasticars_logo);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String incidentcode = Global.Incident_Class.get(position).getIncident_no();
                        deleteItem(incidentcode);
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing (dismiss the dialog)
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
*/
            String conNoString = incidentsModelClasses.get(position).getIncident_no();
            double conNo;
            try {
                conNo = Double.parseDouble(conNoString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }
            String formattedConNo = removeTrailingZero(conNo);
            holder.Incno.setText(formattedConNo);

            String dateString = incidentsModelClasses.get(position).getIncident_Date();
            java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date;
            try {
                date = inputFormat.parse(dateString);
                String Date = outputFormat.format(date);
                holder.Inc_date.setText(Date);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
            holder.Inc_created.setText(incidentsModelClasses.get(position).getInc_Created_by());

            holder.Incedentupload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.incidentsModelClass = incidentsModelClasses.get(position);
                    Intent intent = new Intent(context, IncidentImageUploadActivity.class);
                    context.startActivity(intent);
                }
            });

        }

        public void deleteItem(String incident_code2) {
            RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
            String url = Global.Get_Incidents_delete;
            url = url + "incident_code=" + incident_code2;
            //progressDialog.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //JSONObject respObj = new JSONObject(response);
                    try {
                        String msg = response.getString("msg");
                        boolean isSuccess = response.getBoolean("isSuccess");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    Intent intent = new Intent(context, IncidentReportingFragment.class);
                    context.startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Global.customtoast(context.getApplicationContext(), getLayoutInflater(),"Failed to get my stock .." + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<String, String>();
                    String accesstoken = Global.sharedPreferences.getString("access_token", "");
                    headers.put("Authorization", "Bearer " + accesstoken);
                    return headers;
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);

        }

        private String removeTrailingZero(double value) {
            // Convert the double to a string
            String formattedValue = String.valueOf(value);

            // Remove trailing zero if it's a decimal number
            if (formattedValue.indexOf(".") > 0) {
                formattedValue = formattedValue.replaceAll("0*$", "").replaceAll("\\.$", "");
            }

            return formattedValue;
        }

        @Override
        public int getItemCount() {
            return incidentsModelClasses.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            private TextView Incno, Incedent_Particlulars, Inc_date, Inc_created;
            private ImageView Incedentupload, Inc_delete;

            public Viewholder(@NonNull View itemView) {
                super(itemView);

                Incno = itemView.findViewById(R.id.incident_incno);
                Incedent_Particlulars = itemView.findViewById(R.id.incident_perticulars);
                Inc_date = itemView.findViewById(R.id.incident_date);
                Incedentupload = itemView.findViewById(R.id.incident_photo_upload);
                Inc_created = itemView.findViewById(R.id.incident_createdby);
                //Inc_delete = itemView.findViewById(R.id.incident_delete);

            }
        }
    }
}