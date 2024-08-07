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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import Adapters.RepairBreakUpAdapter;
import Adapters.RepairDetailsAdapter;
import Models.EquipmentClassRepairBreakUp;
import Models.ItemListRepair_BreakUpModel;
import Models.RepairModel3;

public class RepairBreakUpActivity extends AppCompatActivity {

    RepairModel3 repairModel3;
    TextView Equipment_Item, Breakup_Unit, Breakup_qty, Breakup_price;
    TextView Breakup_remark;
    AppCompatButton Update_A, Cancel_A;
    RecyclerView Repair_breakup_recyclerview;
    private Dialog zDialog;
    BottomSheetDialog bottomSheetDialog;
    EquipmentClassRepairBreakUp equipment_spinner;
    ImageView Repair_back_btn;
    ItemListRepair_BreakUpModel item_spinner;
    SwipeRefreshLayout swipeRefreshLayout;
    Context context;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_break_up);

        context = this;
        user_topcard();


        FloatingActionButton breakup = findViewById(R.id.repairbreakupfab);
        breakup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDetailsDialog(context);
            }
        });

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

        get_Breakup_Details_Repair();
        Repair_breakup_recyclerview = findViewById(R.id.repair_breakup_recyclerview);

        Repair_breakup_recyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Repair_breakup_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        if (dy > 0 || dy < 0 && breakup.isShown()) {
                            breakup.hide();
                        }
                    }

                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            breakup.show();
                        }
                        super.onScrollStateChanged(recyclerView, newState);
                    }
                });
            }
        });

        Repair_breakup_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        Repair_breakup_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void refreshScreen() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                Global.repair2list.clear();
                RepairDetailsAdapter repair_details_adapter = new RepairDetailsAdapter(Global.repair2list, context);
                Repair_breakup_recyclerview.setAdapter(repair_details_adapter);
                repair_details_adapter.notifyDataSetChanged();
                get_Breakup_Details_Repair();
            }
        }, 2000);

    }

    private void user_topcard() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String stpname, sitename, processname, repair_date, repair_no, repair_amount, stpcapacity;
        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        processname = sharedPreferences.getString("user_name", "");
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

    private String removeTrailingZero(double value) {
        // Use DecimalFormat to remove trailing zeros
        DecimalFormat decimalFormat = new DecimalFormat("#.###"); // Adjust the format as needed
        return decimalFormat.format(value);
    }

    @SuppressLint("MissingInflatedId")
    private void showAddDetailsDialog(Context context) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        LayoutInflater inflater = getLayoutInflater();
        bottomSheetDialog.setContentView(R.layout.custom_dialog_repair_breakup_layout);

        Equipment_Item = bottomSheetDialog.findViewById(R.id.repair_breakup_item_alert_spinner);
        Breakup_Unit = bottomSheetDialog.findViewById(R.id.repair_breakup_unit_alert_spinner);

        Breakup_qty = bottomSheetDialog.findViewById(R.id.repair_breakup_qty_alert_spinner);
        Breakup_price = bottomSheetDialog.findViewById(R.id.repair_breakup_price_alert_spinner);
        Breakup_remark = bottomSheetDialog.findViewById(R.id.remark_breakup);
        Update_A = bottomSheetDialog.findViewById(R.id.update_alert_breakup);
        Cancel_A = bottomSheetDialog.findViewById(R.id.cancel_alert_breakup);

        Equipment_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEquipmentBreakupSpinnerPopup();
            }
        });
        Breakup_Unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItemBreakUpSpinnerPopup();
            }
        });

        bottomSheetDialog.show();

        Update_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRepairBreakupdetails();
                bottomSheetDialog.dismiss();
            }
        });
        Cancel_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });


    }

    private void updateRepairBreakupdetails() {
        String qty, price, repair_item_code, unit_code, item_name, remarks;
        repair_item_code = equipment_spinner.getEquipmentBreakup_code();
        unit_code = item_spinner.getBreakup_unit_code();
        qty = Breakup_qty.getText().toString();
        price = Breakup_price.getText().toString();
        remarks = Breakup_remark.getText().toString();


        if (repair_item_code.isEmpty()) {
            Toast.makeText(RepairBreakUpActivity.this, "Repair Item should not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (unit_code.isEmpty()) {
            Toast.makeText(RepairBreakUpActivity.this, "Unit should not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (qty.isEmpty()) {
            Toast.makeText(RepairBreakUpActivity.this, "Qty should not be empty!", Toast.LENGTH_SHORT).show();
            return;

        }
        if (qty.equals("0") || qty.matches("0+")) {
            Toast.makeText(RepairBreakUpActivity.this, "Qty should not be zero", Toast.LENGTH_LONG).show();
            return;
        }
        if (price.isEmpty()) {
            Toast.makeText(RepairBreakUpActivity.this, "Price should not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (price.equals("0") || price.matches("0+")) {
            Toast.makeText(RepairBreakUpActivity.this, "Price should not be zero", Toast.LENGTH_LONG).show();
            return;
        }


        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Global.Repair_BreakUp_update;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String sresponse) {
                try {
                    JSONObject jsonObject = new JSONObject(sresponse);
                    boolean success = jsonObject.getBoolean("isSuccess");
                    String error = jsonObject.getString("error");

                    if (success) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        get_Breakup_Details_Repair();
                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
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
                String repair2_code = Global.repairModel2.getD_Repairedtwo();
                params.put("repair_item_code", repair_item_code);
                params.put("qty", qty);
                params.put("remarks", remarks);
                params.put("price", price);
                params.put("unit_code", unit_code);
                params.put("repair2_code", repair2_code);
                params.put("com_code", Global.sharedPreferences.getString("com_code", "0"));
                params.put("ayear", Global.sharedPreferences.getString("ayear", "0"));
                //params.put("sstp1_code", Global.sharedPreferences.getString("sstp1_code", "0"));
                params.put("repair3_code", "0");
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

    private void get_Breakup_Details_Repair() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String Repair_Breakup_API = Global.RepairsRepairBreakUp;
        String url = Repair_Breakup_API + "repair2_code=" + Global.repairModel2.getD_Repairedtwo();
        // String url = Repair_Breakup_API + "repair2_code=" +"2";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.repair3list = new ArrayList<RepairModel3>();
                repairModel3 = new RepairModel3();
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
                    repairModel3 = new RepairModel3();
                    try {
                        repairModel3.setRepair_Breakup_Item_name(e.getString("repair_item_name"));
                        repairModel3.setRepair_Breakup_Unit(e.getString("unit_name"));
                        repairModel3.setRepair_Breakup_Qty(e.getString("qty"));
                        repairModel3.setRepair_Breakup_Price(e.getString("price"));
                        repairModel3.setRepair_Breakup_Remark(e.getString("remarks"));
                        repairModel3.setRepair_Breakup_amount(e.getString("amt"));

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.repair3list.add(repairModel3);
                    RepairBreakUpAdapter repairBreakUpAdapter = new RepairBreakUpAdapter(Global.repair3list, context);
                    repairBreakUpAdapter.notifyDataSetChanged();
                    Repair_breakup_recyclerview.setAdapter(repairBreakUpAdapter);
                }
                getEquipmentsRepairlist();
                getItemRepairList();
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
                    Toast.makeText(RepairBreakUpActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
                }

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

    private void getEquipmentsRepairlist() {
        String Url = Global.api_Repair_List_Get_Equipments + "comcode=" + Global.sharedPreferences.getString("com_code", "0");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Global.Repair_Equipment_Breakup = new ArrayList<EquipmentClassRepairBreakUp>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject equipmentJson = response.getJSONObject(i);
                        EquipmentClassRepairBreakUp equipment = new EquipmentClassRepairBreakUp();

                        equipment.setEquipmentBreakup_id(equipmentJson.getString("com_code"));
                        equipment.setEquipmentBreakup_code(equipmentJson.getString("repair_item_code"));
                        equipment.setEquipmentBreakup_Name(equipmentJson.getString("repair_item_name"));


                        Global.Repair_Equipment_Breakup.add(equipment);

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + Global.sharedPreferences.getString("access_token", ""));
                return headers;
            }
        };


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonArrayRequest);
    }

    private void getEquipmentBreakupSpinnerPopup() {
        bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.item, null);
        bottomSheetDialog.setContentView(sheetView);

        ListView lvEqName = bottomSheetDialog.findViewById(R.id.lvequipment);

        if (Global.Repair_Equipment_Breakup == null || Global.Repair_Equipment_Breakup.size() == 0) {
            Toast.makeText(getBaseContext(), "Item list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        final EquipmentSelect_Adapter EqA = new EquipmentSelect_Adapter(Global.Repair_Equipment_Breakup);
        lvEqName.setAdapter(EqA);


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

    private class EquipmentSelect_Adapter extends BaseAdapter implements Filterable {
        private ArrayList<EquipmentClassRepairBreakUp> eQarrayList;

        public EquipmentSelect_Adapter(ArrayList<EquipmentClassRepairBreakUp> eQarrayList) {
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

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View v = getLayoutInflater().inflate(R.layout.popup_equipmentlist, null);

            LinearLayout layout = v.findViewById(R.id.select);

            TextView equipmentnameitem = v.findViewById(R.id.tvsingle);
            TextView eqnameitem = v.findViewById(R.id.tvtwoeq);
            equipment_spinner = eQarrayList.get(i);

            equipmentnameitem.setText(equipment_spinner.getEquipmentBreakup_Name());
            eqnameitem.setText(equipment_spinner.getEquipmentBreakup_id());

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    equipment_spinner = eQarrayList.get(i);
                    Equipment_Item.setText(equipment_spinner.getEquipmentBreakup_Name());
                    bottomSheetDialog.dismiss();
                }
            });

            return v;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    ArrayList<EquipmentClassRepairBreakUp> mFilteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = Global.Repair_Equipment_Breakup;
                    } else {
                        for (EquipmentClassRepairBreakUp dataList : Global.Repair_Equipment_Breakup) {
                            if (dataList.getEquipmentBreakup_Name().toLowerCase().contains(charString) ||
                                    dataList.getEquipmentBreakup_id().toLowerCase().contains(charString)) {
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
                    eQarrayList = (ArrayList<EquipmentClassRepairBreakUp>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }

    private void getItemRepairList() {
        String Url = Global.api_Repair_List_Get_Units + "comcode=" + Global.sharedPreferences.getString("com_code", "0");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Global.Repair_Item_Breakup = new ArrayList<ItemListRepair_BreakUpModel>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject equipmentJson = response.getJSONObject(i);
                        ItemListRepair_BreakUpModel equipment = new ItemListRepair_BreakUpModel();

                        equipment.setBreakup_unit(equipmentJson.getString("com_code"));
                        equipment.setBreakup_unit_code(equipmentJson.getString("unit_code"));
                        equipment.setBreakup_unit_name(equipmentJson.getString("unit_name"));


                        Global.Repair_Item_Breakup.add(equipment);

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + Global.sharedPreferences.getString("access_token", ""));
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonArrayRequest);
    }

    private void getItemBreakUpSpinnerPopup() {

        bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.unit, null);
        bottomSheetDialog.setContentView(sheetView);

        ListView lvEqName = bottomSheetDialog.findViewById(R.id.lvequipment);

        if (Global.Repair_Item_Breakup == null || Global.Repair_Item_Breakup.size() == 0) {
            Toast.makeText(getBaseContext(), "Unit list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        ItemSelect_Adapter ItA = new ItemSelect_Adapter(Global.Repair_Item_Breakup);
        lvEqName.setAdapter(ItA);


        SearchView sveq = bottomSheetDialog.findViewById(R.id.svequipment);

        sveq.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ItA.getFilter().filter(newText);
                return false;
            }
        });

        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        bottomSheetDialog.show();
    }

    private class ItemSelect_Adapter extends BaseAdapter implements Filterable {
        private ArrayList<ItemListRepair_BreakUpModel> eQarrayList;

        public ItemSelect_Adapter(ArrayList<ItemListRepair_BreakUpModel> eQarrayList) {
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

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View v = getLayoutInflater().inflate(R.layout.popup_equipmentlist, null);

            LinearLayout layout = v.findViewById(R.id.select);

            TextView equipmentnameitem = v.findViewById(R.id.tvsingle);
            TextView eqnameitem = v.findViewById(R.id.tvtwoeq);
            item_spinner = eQarrayList.get(i);

            equipmentnameitem.setText(item_spinner.getBreakup_unit_name());
            eqnameitem.setText(item_spinner.getBreakup_unit());

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item_spinner = eQarrayList.get(i);
                    Breakup_Unit.setText(item_spinner.getBreakup_unit_name());
                    bottomSheetDialog.dismiss();
                }
            });


            return v;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    ArrayList<ItemListRepair_BreakUpModel> mFilteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = Global.Repair_Item_Breakup;
                    } else {
                        for (ItemListRepair_BreakUpModel dataList : Global.Repair_Item_Breakup) {
                            if (dataList.getBreakup_unit_name().toLowerCase().contains(charString) ||
                                    dataList.getBreakup_unit().toLowerCase().contains(charString)) {
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
                    eQarrayList = (ArrayList<ItemListRepair_BreakUpModel>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}