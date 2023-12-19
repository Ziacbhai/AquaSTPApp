package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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

import Adapters.Repair_BreakUp_Adapter;
import Adapters.Repair_details_Adapter;
import Models.EquipmentRepairListClass;
import Models.RepairsClass;

public class RepairBreakUpActivity extends AppCompatActivity {

    RepairsClass repair_s;

    TextView Remark_A, Equipment_code,Unit;
    AppCompatButton Update_A, Cancel_A;
    RecyclerView Repair_breakup_recyclerview;
    private Dialog zDialog;
    EquipmentRepairListClass equipment_spinner;
    Context context;
    private ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_break_up);
        context = this;
        user_topcard();
        if (!Global.isNetworkAvailable(this)) {
            Global.customtoast(this, getLayoutInflater(), "Internet connection lost !!");
        }
        new InternetCheckTask().execute();
       FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDetailsDialog(context);
            }
        });

        Repair_breakup_recyclerview = findViewById(R.id.repair_breakup_recyclerview);
        Repair_breakup_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        Repair_breakup_recyclerview.setHasFixedSize(true);
        Repair_breakup_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        get_Breakup_Details_Repair();
    }
    private void user_topcard() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String personname, useremail, stpname, sitename, siteaddress, processname, usermobile;
        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        siteaddress = sharedPreferences.getString("site_address", "");
        processname = sharedPreferences.getString("process_name", "");
        useremail = sharedPreferences.getString("user_email", "");
        usermobile = sharedPreferences.getString("user_mobile", "");
        personname = sharedPreferences.getString("person_name", "");

        TextView txtsitename, txtstpname, txtsiteaddress, txtuseremail, txtusermobile, txtpersonname;

        txtsitename = findViewById(R.id.sitename);
        txtstpname = findViewById(R.id.stpname);
        txtsiteaddress = findViewById(R.id.siteaddress);
        txtuseremail = findViewById(R.id.useremail);
        txtusermobile = findViewById(R.id.usermobile);
        txtpersonname = findViewById(R.id.personname);

        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " / " + processname);
        txtsiteaddress.setText(siteaddress);
        txtuseremail.setText(useremail);
        txtusermobile.setText(usermobile);
        txtpersonname.setText(personname);
    }

    @SuppressLint("MissingInflatedId")
    private void showAddDetailsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_repair_details_layout, null);
        Equipment_code = dialogView.findViewById(R.id.repair_breakup_item_alert_spinner);
        Unit = dialogView.findViewById(R.id.repair_breakup_unit_alert_spinner);
        Remark_A = dialogView.findViewById(R.id.remark_alert_rd);
        Update_A = dialogView.findViewById(R.id.update_alert_rd);
        Cancel_A = dialogView.findViewById(R.id.cancel_alert_rd);

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
            dialog.getWindow().setAttributes(layoutParams);
        }

        dialog.show();

        Equipment_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getRepairBreakupSpinnerPopup();
            }
        });

        Update_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // updateRepairbreakupdetails();
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

    private void get_Breakup_Details_Repair() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String Repair_Breakup_API = Global.RepairsRepairBreakUp;
        String url = Repair_Breakup_API + "repair2_code=" + Global.repairsClass.getD_Repairedtwo();
       // String url = Repair_Breakup_API + "repair2_code=" +"2";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Repair_s = new ArrayList<RepairsClass>();
                repair_s = new RepairsClass();
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
                    repair_s = new RepairsClass();
                    try {
                        repair_s.setRepair_Breakup_Item(e.getString("repair_item_code"));
                        repair_s.setRepair_Breakup_Unit(e.getString("unit_code"));
                        repair_s.setRepair_Breakup_Qty(e.getString("qty"));
                        repair_s.setRepair_Breakup_Price(e.getString("price"));
                        repair_s.setRepair_Breakup_Remark(e.getString("remarks"));
                        repair_s.setRepair_Breakup_amount(e.getString("amt"));

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Repair_s.add(repair_s);
                    Repair_BreakUp_Adapter repairBreakUpAdapter = new Repair_BreakUp_Adapter(Global.Repair_s, context);
                    Repair_breakup_recyclerview.setAdapter(repairBreakUpAdapter);
                }
               // getEquipmentsListRepairdetails();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(RepairBreakUpActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(RepairBreakUpActivity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(RepairBreakUpActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(RepairBreakUpActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(RepairBreakUpActivity.this, "Parse Error", Toast.LENGTH_LONG).show();}

            }
        }){
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

  /*  private void updateRepairbreakupdetails() {
    }

    private void getRepairBreakupSpinnerPopup() {
        zDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);

        zDialog.setContentView(R.layout.equipment_item);

        ListView lvEqName = zDialog.findViewById(R.id.lvequipment);
        TextView Equipment_Name = zDialog.findViewById(R.id.euipment_name);
        TextView Equipment_id = zDialog.findViewById(R.id.euipment_id);

        if (Global.Repair_equipment == null || Global.Repair_equipment.size() == 0) {
            Toast.makeText(getBaseContext(), "Equipment list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        RepairBreakUpActivity.EquipmentSelect_breakupRepair_Adapter EqA = new RepairBreakUpActivity.EquipmentSelect_breakupRepair_Adapter(Global.Repair_equipment);
        lvEqName.setAdapter(EqA);

        Equipment_Name.setText("Equipment Name");
        Equipment_id.setText("Equipment ID");
        zDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        zDialog.show();

        SearchView sveq = zDialog.findViewById(R.id.svequipment);

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

    public class EquipmentSelect_breakupRepair_Adapter extends BaseAdapter implements Filterable {

        private ArrayList<EquipmentRepairListClass> eQarrayList;

        public EquipmentSelect_breakupRepair_Adapter(ArrayList<EquipmentRepairListClass> eQarrayList) {
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
            TextView equipmentnameitem = v.findViewById(R.id.tvsingle);
            TextView eqnameitem = v.findViewById(R.id.tvtwoeq);
            equipment_spinner = eQarrayList.get(i);

            equipmentnameitem.setText(equipment_spinner.getEquipment_Name());
            eqnameitem.setText(equipment_spinner.getEquipment_id());

            equipmentnameitem.setOnClickListener(view1 -> {
                equipment_spinner = eQarrayList.get(i);
                Equipment_code.setText(equipment_spinner.getEquipment_Name());
                zDialog.dismiss();
            });

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
    }*/
}