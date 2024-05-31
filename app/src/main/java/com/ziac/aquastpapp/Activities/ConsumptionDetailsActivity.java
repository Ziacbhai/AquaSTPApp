package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Adapters.ConsumptionDetailsAdapter;
import Models.ConsumptionModel2;
import Models.EquipmentListClassConsumption;
import Models.ItemListConsumptionModel;

public class ConsumptionDetailsActivity extends AppCompatActivity {
    ConsumptionModel2 consumptionModel2;
    RecyclerView Consumables_D_Rv;
    Context context;
    private EquipmentListClassConsumption equipment_spinner;
    private ItemListConsumptionModel Item_spinner;

    BottomSheetDialog bottomSheetDialog;
    TextView Equipment_code, Item_codeTV;
    EditText Qty_cb;
    AppCompatButton Update_A, Cancel_A;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isSwipeRefreshTriggered = false;

    ImageView Repair_back_btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption_details_design);

        context = this;
        user_topcard();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading please wait...");
        progressDialog.setCancelable(true);
        Repair_back_btn = findViewById(R.id.repair_back_btn);
        Repair_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshScreen();
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDetailsBottomSheetDialog(context);
            }
        });
        isSwipeRefreshTriggered = true;
        getConsumablesDetails();
        Consumables_D_Rv = findViewById(R.id.consumables_details_recyclerview);
        Consumables_D_Rv.setLayoutManager(new LinearLayoutManager(context));
        Consumables_D_Rv.setHasFixedSize(true);
        Consumables_D_Rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

    }

    private void user_topcard() {
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String stpname, sitename, processname, consumption_date, consumption_no, consumption_amount, stpcapacity;
        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        processname = sharedPreferences.getString("process_name", "");
        stpcapacity = sharedPreferences.getString("stp_capacity", "");

        consumption_date = Global.ConsumptionModel.getDate();
        consumption_no = Global.ConsumptionModel.getCon_no();
        consumption_amount = Global.ConsumptionModel.getAmount();

        TextView txtsitename, txtstpname, textno, textdate, texamount;
        txtsitename = findViewById(R.id.sitename);
        txtstpname = findViewById(R.id.stpname);
        textno = findViewById(R.id.consumption_no);
        textdate = findViewById(R.id.consumption_date);
        texamount = findViewById(R.id.consumption_amount);
        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " / " + processname + " / " + stpcapacity);

        textno.setText(consumption_no);
        texamount.setText(consumption_amount + "0");

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date date = inputFormat.parse(consumption_date);
            String formattedDate = outputFormat.format(date);
            String onlyDate = formattedDate.split(" ")[0];
            textdate.setText(onlyDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        consumption_no = Global.ConsumptionModel.getCon_no();
        double conNo;
        try {
            conNo = Double.parseDouble(consumption_no);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        String formattedConNo = removeTrailingZero(conNo);
        textno.setText(formattedConNo);
    }

    private String removeTrailingZero(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.###"); // Adjust the format as needed
        return decimalFormat.format(value);
    }

    private void showAddDetailsBottomSheetDialog(Context context) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.custom_dialog_consumption_details_layout);

        LinearLayout Equipment, Item;
        Equipment_code = bottomSheetDialog.findViewById(R.id.equipment_name_alert_spinner);
        Item_codeTV = bottomSheetDialog.findViewById(R.id.item_name_alert_spinner);

        Equipment = bottomSheetDialog.findViewById(R.id.eqipment_alert_spinner);
        Item = bottomSheetDialog.findViewById(R.id.Item_alert_spinner);

        Equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEquipmentsSpinnerPopup();
            }
        });
        Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItemSpinnerPopup();
            }
        });

        Qty_cb = bottomSheetDialog.findViewById(R.id.qty_alert_cd);
        Update_A = bottomSheetDialog.findViewById(R.id.update_alert_cd);
        Cancel_A = bottomSheetDialog.findViewById(R.id.cancel_alert_cd);

        if (Update_A != null) {
            Update_A.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String equipment_code = Equipment_code.getText().toString();
                    String item_code = Item_codeTV.getText().toString();
                    String qty = Qty_cb.getText().toString();


                    if (equipment_code.isEmpty()) {
                        Toast.makeText(ConsumptionDetailsActivity.this, "Equipment  should not be empty !!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (item_code.isEmpty()) {
                        Toast.makeText(ConsumptionDetailsActivity.this, "Item should not be empty !!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (qty.isEmpty()) {
                        Toast.makeText(ConsumptionDetailsActivity.this, "Qty should not be empty !!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (qty.equals("0") || qty.matches("0+")) {
                        Toast.makeText(ConsumptionDetailsActivity.this, "Qty should not be zero !!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    updateConsumables_details();
                    bottomSheetDialog.dismiss();
                }
            });
        }

        if (Cancel_A != null) {
            Cancel_A.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                }
            });
        }

        bottomSheetDialog.show();
        getEquipmentsList();
        getItemSpinner();
    }


    private void updateConsumables_details() {

        String qty = Qty_cb.getText().toString();
        String equipment_code = equipment_spinner.getEquipment_code();
        String item_code = Item_spinner.getItem_code();


        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Global.updateDConsumption;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String sresponse) {
                Log.d("Response", sresponse); // Add this line
                JSONObject response;
                try {
                    response = new JSONObject(sresponse);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (response.getBoolean("isSuccess")) {
                        Toast.makeText(context, "Updated successfully !!", Toast.LENGTH_SHORT).show();
                        getConsumablesDetails();


                    } else {
                        Toast.makeText(context, response.getString("error"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //getConsumablesDetails();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(context, "failed to upload", Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
                }
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
                String con1_code = Global.ConsumptionModel.getCon1_code();
                params.put("item_code", item_code);
                // params.put("item_code", String.valueOf(equipment_spinner.getEquipment_code()));
                params.put("equip_code", equipment_code);
                // params.put("equip_code", String.valueOf(equipment_spinner.getEquipment_id()));
                params.put("qty", qty);
                params.put("com_code", Global.sharedPreferences.getString("com_code", "0"));
                params.put("ayear", Global.sharedPreferences.getString("ayear", "0"));
                params.put("sstp1_code", Global.sharedPreferences.getString("sstp1_code", "0"));
                params.put("con1_code", con1_code);
                params.put("con2_code", "0");

                Log.d("params", "parameter" + params);
                return params;
            }


        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);

    }

    private void refreshScreen() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                Global.Consumption2list.clear();
                ConsumptionDetailsAdapter consumablesDetailsAdapter = new ConsumptionDetailsAdapter(context, Global.Consumption2list);
                Consumables_D_Rv.setAdapter(consumablesDetailsAdapter);
                consumablesDetailsAdapter.notifyDataSetChanged();
                getConsumablesDetails();
            }
        }, 2000);
    }

    private void getConsumablesDetails() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String consumables_d = Global.Get_Consumables_Details;
        String Consumable_Details_API = consumables_d + "con1_code=" + Global.ConsumptionModel.getCon1_code();
        // Toast.makeText(context, ""+Global.ConsumablesClass.getCon_no(), Toast.LENGTH_SHORT).show();

        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Consumable_Details_API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Global.Consumption2list = new ArrayList<ConsumptionModel2>();
                consumptionModel2 = new ConsumptionModel2();
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
                    consumptionModel2 = new ConsumptionModel2();
                    try {
                        consumptionModel2.setEquipment_Name(e.getString("equip_name"));
                        consumptionModel2.setEquipment_id(e.getString("equip_slno"));
                        consumptionModel2.setEquip_code(e.getString("equip_code"));
                        consumptionModel2.setItem_code(e.getString("item_code"));
                        consumptionModel2.setD_qty(e.getString("qty"));
                        consumptionModel2.setD_Amount(e.getString("prd_amt"));
                        consumptionModel2.setD_item(e.getString("part_no"));
                        consumptionModel2.setD_item_name(e.getString("prd_name"));
                        consumptionModel2.setD_unit(e.getString("unit_name"));
                        consumptionModel2.setD_rate(e.getString("prch_price"));

                        // Toast.makeText(context, ""+consumables_Class.getD_item_name(), Toast.LENGTH_SHORT).show();
                    /*    Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.putString("equip_code", consumables_Class.getEquip_code());
                        Global.editor.putString("item_code", consumables_Class.getItem_code());
                        Global.editor.putString("qty", consumables_Class.getD_qty());
                        Global.editor.commit();*/

                    } catch (JSONException ex) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "No Data Available", Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(ex);
                    }
                    Global.Consumption2list.add(consumptionModel2);
                    ConsumptionDetailsAdapter consumablesDetailsAdapter = new ConsumptionDetailsAdapter(context, Global.Consumption2list);
                    Consumables_D_Rv.setAdapter(consumablesDetailsAdapter);
                }
                getEquipmentsList();
                getItemSpinner();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
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

    private void getEquipmentsList() {

        String Url = Global.api_List_Get_Equipments +
                "comcode=" + Global.sharedPreferences.getString("com_code", "0") +
                "&sstp1_code=" + Global.sharedPreferences.getString("sstp1_code", "0") +
                "&typ=C";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Global.Consumption_equipment = new ArrayList<EquipmentListClassConsumption>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject equipmentJson = response.getJSONObject(i);
                                EquipmentListClassConsumption equipment = new EquipmentListClassConsumption();

                                equipment.setEquipment_id(equipmentJson.getString("equip_slno"));
                                equipment.setEquipment_code(equipmentJson.getString("equip_code"));
                                equipment.setEquipment_Name(equipmentJson.getString("equip_name"));
                                //equipment.setEquipment_Name(equipmentJson.getString("equip_name"));

                                Global.Consumption_equipment.add(equipment);

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

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonArrayRequest);
    }

    private void getEquipmentsSpinnerPopup() {
        bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.equipment_item, null);
        bottomSheetDialog.setContentView(sheetView);

        ListView lvEqName = sheetView.findViewById(R.id.lvequipment);

        getEquipmentsList();
        if (Global.Consumption_equipment == null || Global.Consumption_equipment.size() == 0) {
            Toast.makeText(getBaseContext(), "Equipment list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        EquipmentSelect_Adapter EqA = new EquipmentSelect_Adapter(Global.Consumption_equipment);
        lvEqName.setAdapter(EqA);

        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        bottomSheetDialog.show();

        SearchView sveq = sheetView.findViewById(R.id.svequipment);

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
        private ArrayList<EquipmentListClassConsumption> eQarrayList;

        public EquipmentSelect_Adapter(ArrayList<EquipmentListClassConsumption> eQarrayList) {
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

            equipment_spinner = eQarrayList.get(i);

            equipmentnameitem.setText(equipment_spinner.getEquipment_Name());


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    equipment_spinner = eQarrayList.get(i);
                    Equipment_code.setText(equipment_spinner.getEquipment_Name());
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
                    ArrayList<EquipmentListClassConsumption> mFilteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = Global.Consumption_equipment;
                    } else {
                        for (EquipmentListClassConsumption dataList : Global.Consumption_equipment) {
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
                    eQarrayList = (ArrayList<EquipmentListClassConsumption>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }


    private void getItemSpinner() {
        String Url = Global.api_List_Get_Item + "comcode=" + Global.sharedPreferences.getString("com_code", "0");

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Global.Consumption_item = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject equipmentJson = response.getJSONObject(i);
                        ItemListConsumptionModel item = new ItemListConsumptionModel();

                        item.setItem(equipmentJson.getString("part_no"));
                        item.setItem_code(equipmentJson.getString("item_code"));
                        item.setItem_name(equipmentJson.getString("prd_name"));

                        Global.Consumption_item.add(item);

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(ConsumptionDetailsActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
                }

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


    private void getItemSpinnerPopup() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.item);

        ListView lvItem = bottomSheetDialog.findViewById(R.id.lvequipment);

        if (Global.Consumption_item == null || Global.Consumption_item.size() == 0) {
            Toast.makeText(getBaseContext(), "Item list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        ItemSelect_Adapter laItem = new ItemSelect_Adapter(Global.Consumption_item);
        lvItem.setAdapter(laItem);

        bottomSheetDialog.setOnShowListener(dialog -> {
            bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        });


        SearchView sveq = bottomSheetDialog.findViewById(R.id.svequipment);
        sveq.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                laItem.getFilter().filter(newText);
                return false;
            }
        });

        bottomSheetDialog.show();
    }

    public class ItemSelect_Adapter extends BaseAdapter implements Filterable {

        private ArrayList<ItemListConsumptionModel> mDataArrayList;

        public ItemSelect_Adapter(ArrayList<ItemListConsumptionModel> mDataArrayList) {
            this.mDataArrayList = mDataArrayList;
        }

        @Override
        public int getCount() {
            return mDataArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_itemlist_consumption, parent, false);
            TextView tvequipmentnameitem = v.findViewById(R.id.tvitemfirst);
            Item_spinner = mDataArrayList.get(i);
            tvequipmentnameitem.setText(Item_spinner.getItem_name());


            tvequipmentnameitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item_spinner = mDataArrayList.get(i);
                    Item_codeTV.setText(Item_spinner.getItem_name());
                    bottomSheetDialog.dismiss(); // assuming bottomSheetDialog is accessible here
                }
            });


            return v;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    List<ItemListConsumptionModel> mFilteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = Global.Consumption_item;
                    } else {
                        for (ItemListConsumptionModel dataList : Global.Consumption_item) {
                            if (dataList.getItem_name().toLowerCase().contains(charString) ||
                                    dataList.getItem().toLowerCase().contains(charString)) {
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
                    mDataArrayList = (ArrayList<ItemListConsumptionModel>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }

}