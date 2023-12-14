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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Adapters.Repair_details_Adapter;
import Models.EquipmentListClassConsumables;
import Models.EquipmentRepairListClass;
import Models.RepairsClass;

public class Repair_Details_Activity extends AppCompatActivity {
    RepairsClass repair_s;

    TextView Equipment_name,Remark_A,Equipment_code;
    AppCompatButton Update_A,Cancel_A;
    RecyclerView Repair_details_recyclerview;
    private Dialog zDialog;
    EquipmentRepairListClass equipment_spinner;
    Context context;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details_design);
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

        Repair_details_recyclerview = findViewById(R.id.repair_details_recyclerview);
        Repair_details_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        Repair_details_recyclerview.setHasFixedSize(true);
        Repair_details_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        get_Details_Repair();

    }

    private void user_topcard() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String personname,useremail,stpname,sitename,siteaddress,processname,usermobile;
        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        siteaddress = sharedPreferences.getString("site_address", "");
        processname = sharedPreferences.getString("process_name", "");
        useremail = sharedPreferences.getString("user_email", "");
        usermobile = sharedPreferences.getString("user_mobile", "");
        personname = sharedPreferences.getString("person_name", "");

        TextView txtsitename,txtstpname,txtsiteaddress,txtuseremail,txtusermobile,txtpersonname;

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
    private void showAddDetailsDialog (Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_repair_details_layout, null);
        Equipment_name = dialogView.findViewById(R.id.equipment_name_alert);
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

        Equipment_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRepairEquipmentsSpinnerPopup();
            }
        });

        Update_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             updateRepairdetails();
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
    private void get_Details_Repair() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Global.Get_Repairs_Details;

        String Repair_Details_API = url + "repair1_code=" + Global.sharedPreferences.getString("repair1_code", "0");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Repair_Details_API, null, new Response.Listener<JSONObject>() {
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
                        repair_s.setD_Equipment_Name(e.getString("equip_name"));
                        repair_s.setD_Equipment_Number(e.getString("equip_slno"));
                        repair_s.setD_Amount(e.getString("repaired_amt"));
                        repair_s.setD_Repaired(e.getString("repaired_flag"));
                        repair_s.setD_Remark(e.getString("repaired_remarks"));
                        //String repair_code= repair_s.getRepair_code();
                        // System.out.println(repair_code);
                        //  Global.editor = Global.sharedPreferences.edit();
                        // Global.editor.putString("",repair_code);
                        //Global.editor.commit();
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Repair_s.add(repair_s);
                    Repair_details_Adapter repair_details_adapter = new Repair_details_Adapter(Global.Repair_s, context);
                    Repair_details_recyclerview.setAdapter(repair_details_adapter);
                }
                getEquipmentsListRepairdetails();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){
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
        queue.add(jsonObjectRequest);
    }
    private void getRepairEquipmentsSpinnerPopup() {
        zDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);

        zDialog.setContentView(R.layout.equipment_item);

        ListView lvEqName = zDialog.findViewById(R.id.lvequipment);
        TextView Equipment_name =zDialog.findViewById(R.id.euipment_name);
        TextView Equipment_id = zDialog.findViewById(R.id.euipment_id);

        if (Global.Repair_equipment == null || Global.Repair_equipment.size() == 0) {
            Toast.makeText(getBaseContext(), "Equipment list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        final Repair_Details_Activity.EquipmentSelectRepair_Adapter EqA = new Repair_Details_Activity.EquipmentSelectRepair_Adapter(Global.Repair_equipment);
        lvEqName.setAdapter(EqA);

        Equipment_name.setText("Equipment Name");
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

    private void updateRepairdetails() {
        String remarks=Remark_A.getText().toString();
        String equipment_code = equipment_spinner.getREquipment_code();
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
                        Toast.makeText(Repair_Details_Activity.this, "Updated successfully !!",Toast.LENGTH_SHORT).show();
                        get_Details_Repair();
                    } else {
                        Toast.makeText(Repair_Details_Activity.this, response.getString("error"), Toast.LENGTH_SHORT).show();

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
                params.put("equip_code", equipment_code);
                params.put("repaired_remarks",remarks);
                params.put("com_code", Global.sharedPreferences.getString("com_code", "0"));
                params.put("ayear", Global.sharedPreferences.getString("ayear", "0"));
                params.put("sstp1_code", Global.sharedPreferences.getString("sstp1_code", "0"));
                params.put("repair2_code ", "0");
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(2500), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    private void getEquipmentsListRepairdetails() {

        String Url = Global.api_List_Get_Equipments + "comcode=" + Global.sharedPreferences.getString("com_code", "0");
        Url = Url + "&sstp1_code=" + Global.sharedPreferences.getString("sstp1_code", "0");

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

                                equipment.setREquipment_id(equipmentJson.getString("equip_slno"));
                                equipment.setREquipment_code(equipmentJson.getString("equip_code"));
                                equipment.setREquipment_Name(equipmentJson.getString("equip_name"));
                                //equipment.setEquipment_Name(equipmentJson.getString("equip_name"));

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


    public class EquipmentSelectRepair_Adapter extends BaseAdapter implements Filterable{

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
            TextView equipmentnameitem = v.findViewById(R.id.tvsingle);
            TextView eqnameitem = v.findViewById(R.id.tvtwoeq);
            equipment_spinner = eQarrayList.get(i);

            equipmentnameitem.setText(equipment_spinner.getREquipment_Name());
            eqnameitem.setText(equipment_spinner.getREquipment_id());

            equipmentnameitem.setOnClickListener(view1 -> {
                equipment_spinner = eQarrayList.get(i);
                Equipment_name.setText(equipment_spinner.getREquipment_Name());
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
                            if (dataList.getREquipment_Name().toLowerCase().contains(charString) ||
                                    dataList.getREquipment_id().toLowerCase().contains(charString)) {
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