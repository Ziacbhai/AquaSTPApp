package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ziac.aquastpapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import Adapters.RepairDetailsAdapter;
import Models.EquipmentRepairListClass;
import Models.RepairModel2;

public class RepairDetailsActivity extends AppCompatActivity {
    RepairModel2 repairModel2;
    TextView Remark_A;
    TextView Equipment_code;
    MaterialButton Update_A, Cancel_A;
    RecyclerView Repair_details_recyclerview;
    private static Dialog zDialog;
    BottomSheetDialog bottomSheetDialog;
    static EquipmentRepairListClass equipment_spinner;
    Context context;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    ImageView Repair_back_btn;
    FloatingActionButton fab;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details_design);

        context = this;
        user_topcard();


         fab = findViewById(R.id.repairdetailsfab);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshScreen();
            }
        });

        Repair_back_btn = findViewById(R.id.repair_back_btn);
        Repair_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDetailsDialog(context);
            }
        });

        Repair_details_recyclerview = findViewById(R.id.repair_details_recyclerview);

        Repair_details_recyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Repair_details_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        if (dy > 0 || dy < 0 && fab.isShown()) {
                            fab.hide();
                        }
                    }

                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            fab.show();
                        }
                        super.onScrollStateChanged(recyclerView, newState);
                    }
                });
            }
        });
        Repair_details_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        Repair_details_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        get_Details_Repair();
    }

    private void user_topcard() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String stpname, sitename, processname, repair_date, repair_no, repair_amount, stpcapacity;

        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        processname = sharedPreferences.getString("process_name", "");
        stpcapacity = sharedPreferences.getString("stp_capacity", "");

        repair_date = Global.repairModel1.getRepair_Date();
        repair_no = Global.repairModel1.getREPNo();
        repair_amount = Global.repairModel1.getRepair_Amount();

        TextView txtsitename, txtstpname, textno, textdate, texamount;

        txtsitename = findViewById(R.id.sitename);
        txtstpname = findViewById(R.id.stpname);
        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " / " + processname + " / " + stpcapacity);

        textno = findViewById(R.id.repair_no);
        textdate = findViewById(R.id.repair_date);
        texamount = findViewById(R.id.repair_amount);

        textno.setText(repair_no);
        texamount.setText(repair_amount + "0");

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date date = inputFormat.parse(repair_date);
            String formattedDate = outputFormat.format(date);
            String onlyDate = formattedDate.split(" ")[0];

            textdate.setText(onlyDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        repair_no = Global.repairModel1.getREPNo();
        double conNo;
        try {
            conNo = Double.parseDouble(repair_no);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        String formattedConNo = removeTrailingZero(conNo);
        textno.setText(formattedConNo);
    }

    private void refreshScreen() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                Global.repair2list.clear();
                RepairDetailsAdapter repair_details_adapter = new RepairDetailsAdapter(Global.repair2list, context);
                Repair_details_recyclerview.setAdapter(repair_details_adapter);
                repair_details_adapter.notifyDataSetChanged();
                get_Details_Repair();
            }
        }, 2000);

    }

    private String removeTrailingZero(double value) {
        // Use DecimalFormat to remove trailing zeros
        DecimalFormat decimalFormat = new DecimalFormat("#.###"); // Adjust the format as needed
        return decimalFormat.format(value);
    }

    @SuppressLint("MissingInflatedId")
    private void showAddDetailsDialog(Context context) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        LinearLayout Equipment_spinner;
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_repair_details_layout, null);
        Equipment_code = dialogView.findViewById(R.id.equipment_name_alert);
        Equipment_spinner = dialogView.findViewById(R.id.repair_equipment_spinner);
        Remark_A = dialogView.findViewById(R.id.remark_alert_rd);
        Update_A = dialogView.findViewById(R.id.update_alert_rd);
        Cancel_A = dialogView.findViewById(R.id.cancel_alert_rd);

        Update_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String equipment_code = Equipment_code.getText().toString();

                if (equipment_code.isEmpty()) {
                    Toast.makeText(RepairDetailsActivity.this, "Equipment_code should not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateRepairdetails();
                bottomSheetDialog.dismiss();
            }
        });

        Cancel_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        Equipment_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRepairEquipmentsSpinnerPopup();
            }
        });

        bottomSheetDialog.setContentView(dialogView);

        bottomSheetDialog.show();

        getEquipmentsListRepairdetails();
    }
    private void updateRepairdetails() {

        String remarks = Remark_A.getText().toString();
        String equipment_code = equipment_spinner.getEquipment_code();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Global.updateRepairDAddUpdate;

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
                        Toast.makeText(RepairDetailsActivity.this, "Updated successfully !!", Toast.LENGTH_SHORT).show();
                        get_Details_Repair();
                    } else {
                        Toast.makeText(RepairDetailsActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
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
                params.put("equip_code", equipment_code);
                params.put("repaired_remarks", remarks);
                params.put("com_code", Global.sharedPreferences.getString("com_code", "0"));
                params.put("ayear", Global.sharedPreferences.getString("ayear", "0"));
                params.put("sstp1_code", Global.sharedPreferences.getString("sstp1_code", "0"));
                params.put("repair1_code", Global.repairModel1.getRepair_code());
                //params.put("repair1_code", "22");
                System.out.println(params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }
    private void get_Details_Repair() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Global.Get_Repairs_Details;

        String Repair_Details_API = url + "repair1_code=" + Global.repairModel1.getRepair_code();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Repair_Details_API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.repair2list = new ArrayList<RepairModel2>();
                repairModel2 = new RepairModel2();

                try {
                    JSONArray jarray = response.getJSONArray("data");

                    if (jarray.length() > 0) {
                        for (int i = 0; i < jarray.length(); i++) {
                            final JSONObject e = jarray.getJSONObject(i);
                            repairModel2 = new RepairModel2();

                            repairModel2.setD_Equipment_Name(e.getString("equip_name"));
                            repairModel2.setD_Equipment_Number(e.getString("equip_slno"));
                            repairModel2.setD_Amount(e.getString("repaired_amt"));
                            repairModel2.setD_Repaired(e.getString("repaired_flag"));
                            repairModel2.setD_Remark(e.getString("repaired_remarks"));
                            repairModel2.setD_Repairedtwo(e.getString("repair2_code"));

                           /* String repair2_code = repairClass2.getD_Repairedtwo();
                            Global.editor = Global.sharedPreferences.edit();
                            Global.editor.putString("repair2_code", repair2_code);
                            Global.editor.commit();*/

                            Global.repair2list.add(repairModel2);
                        }
                        RepairDetailsAdapter repair_details_adapter = new RepairDetailsAdapter(Global.repair2list, context);
                        Repair_details_recyclerview.setAdapter(repair_details_adapter);
                        repair_details_adapter.notifyDataSetChanged();
                        getEquipmentsListRepairdetails();
                    } else {
                        Toast.makeText(context, "No data available", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                Toast.makeText(context, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        }) {
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

    private void getEquipmentsListRepairdetails() {

        String Url = Global.api_List_Get_Equipments + "comcode=" + Global.sharedPreferences.getString("com_code", "0") +
                "&sstp1_code=" + Global.sharedPreferences.getString("sstp1_code", "0")
        + "&typ=R";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Global.Repair_equipment = new ArrayList<EquipmentRepairListClass>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject equipmentJson = response.getJSONObject(i);
                                EquipmentRepairListClass equipment = new EquipmentRepairListClass();

                                equipment.setEquipment_id(equipmentJson.getString("equip_slno"));
                                equipment.setEquipment_code(equipmentJson.getString("equip_code"));
                                equipment.setEquipment_Name(equipmentJson.getString("equip_name"));
                                Global.Repair_equipment.add(equipment);

                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + Global.sharedPreferences.getString("access_token", ""));
                return headers;
            }
        };

        queue.add(jsonArrayRequest);
    }




    private void getRepairEquipmentsSpinnerPopup() {
        bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.equipment_item, null);
        bottomSheetDialog.setContentView(sheetView);

        ListView lvEqName = bottomSheetDialog.findViewById(R.id.lvequipment);
        /*TextView Equipment_Name = zDialog.findViewById(R.id.euipment_name);
        TextView Equipment_id = zDialog.findViewById(R.id.euipment_id);
*/
        if (Global.Repair_equipment == null || Global.Repair_equipment.size() == 0) {
            Toast.makeText(getBaseContext(), "Equipment list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        final EquipmentSelectRepair_Adapter EqA = new EquipmentSelectRepair_Adapter(Global.Repair_equipment);
        lvEqName.setAdapter(EqA);

       /* Equipment_Name.setText("Equipment Name");
        Equipment_id.setText("Equipment ID");*/
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        bottomSheetDialog.show();

        SearchView sveq = bottomSheetDialog.findViewById(R.id.svequipment);
        sveq.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                EqA.getFilter().filter(newText);
                return false;
            }
        });
    }


    public class EquipmentSelectRepair_Adapter extends BaseAdapter implements Filterable {

        private ArrayList<EquipmentRepairListClass> eQarrayList;

        public EquipmentSelectRepair_Adapter(ArrayList<EquipmentRepairListClass> eQarrayList) {
            this.eQarrayList = eQarrayList;
        }

        @Override
        public int getCount() {
            return eQarrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return eQarrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public View getView(int i, View convertView, ViewGroup parent) {
            View v = getLayoutInflater().inflate(R.layout.popup_equipmentlist, null);
            LinearLayout layout = v.findViewById(R.id.select);
            TextView equipmentnameitem = v.findViewById(R.id.tvsingle);
            //TextView eqnameitem = v.findViewById(R.id.tvtwoeq);
            equipment_spinner = eQarrayList.get(i);

            equipmentnameitem.setText(equipment_spinner.getEquipment_Name());
            // eqnameitem.setText(equipment_spinner.getEquipment_id());
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    equipment_spinner = eQarrayList.get(i);
                    Equipment_code.setText(equipment_spinner.getEquipment_Name());
                    bottomSheetDialog.dismiss();
                }
            });
           /* layout.setOnClickListener(view1 -> {
                equipment_spinner = eQarrayList.get(i);
                Equipment_code.setText(equipment_spinner.getEquipment_code());
                zDialog.dismiss();
            });*/
            return v;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    ArrayList<EquipmentRepairListClass> mFilteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = Global.Repair_equipment;
                    } else {
                        for (EquipmentRepairListClass dataList : Global.Repair_equipment) {
                            if (dataList.getEquipment_Name().toLowerCase().contains(charString) ||
                                    dataList.getEquipment_id().toLowerCase().contains(charString)) {
                                mFilteredList.add(dataList);
                            }
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilteredList;
                    filterResults.count = mFilteredList.size();
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    eQarrayList = (ArrayList<EquipmentRepairListClass>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }


}