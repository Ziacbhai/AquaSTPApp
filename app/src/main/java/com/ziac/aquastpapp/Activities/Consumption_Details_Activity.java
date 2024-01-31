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

import Adapters.Consumption_Details_Adapter;
import Models.ConsumptionClass2;
import Models.EquipmentListClassConsumption;
import Models.ItemListClassConsumption;

public class Consumption_Details_Activity extends AppCompatActivity {
    ConsumptionClass2 consumptionClass2;
    RecyclerView Consumables_D_Rv;
    Context context;
    private EquipmentListClassConsumption equipment_spinner;
    private ItemListClassConsumption Item_spinner;

    private Dialog zDialog;
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
                showAddDetailsDialog(context);
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

        consumption_date = Global.ConsumptionClass.getDate();
        consumption_no = Global.ConsumptionClass.getCon1_code();
        consumption_amount = Global.ConsumptionClass.getAmount();

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
        consumption_no = Global.ConsumptionClass.getCon1_code();
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

    @SuppressLint("MissingInflatedId")
    private void showAddDetailsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout Equipment, Item;
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_consumption_details_layout, null);

        Equipment_code = dialogView.findViewById(R.id.equipment_name_alert_spinner);
        Item_codeTV = dialogView.findViewById(R.id.item_name_alert_spinner);

        Equipment = dialogView.findViewById(R.id.eqipment_alert_spinner);
        Item = dialogView.findViewById(R.id.Item_alert_spinner);

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



        Qty_cb = dialogView.findViewById(R.id.qty_alert_cd);
        Update_A = dialogView.findViewById(R.id.update_alert_cd);
        Cancel_A = dialogView.findViewById(R.id.cancel_alert_cd);

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
       /* Update_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredQty = Qty_cb.getText().toString().trim();
                if (enteredQty.isEmpty()) {
                    Qty_cb.setError("Quantity cannot be empty");
                } else {
                    try {
                        int quantity = Integer.parseInt(enteredQty);
                        updateConsumables_details();
                        dialog.dismiss(); // Close the dialog if needed
                    } catch (NumberFormatException e) {
                        Qty_cb.setError("Invalid quantity");
                    }
                }
            }
        });*/

        Update_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String equipment_code = Equipment_code.getText().toString();
                String item_code = Item_codeTV.getText().toString();
                String qty = Qty_cb.getText().toString();

                if (equipment_code.isEmpty()) {
                    Toast.makeText(Consumption_Details_Activity.this, "Equipment_code  should not be empty !!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (item_code.isEmpty()) {
                    Toast.makeText(Consumption_Details_Activity.this, "Item_code should not be empty !!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (qty.isEmpty()) {
                    Toast.makeText(Consumption_Details_Activity.this, "Qty should not be empty !!", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateConsumables_details();
                dialog.dismiss();
            }
        });
        Cancel_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

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
                    Toast.makeText(Consumption_Details_Activity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Consumption_Details_Activity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Consumption_Details_Activity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Consumption_Details_Activity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Consumption_Details_Activity.this, "Parse Error", Toast.LENGTH_LONG).show();
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
                String con1_code = Global.ConsumptionClass.getCon1_code();
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
                Consumption_Details_Adapter consumablesDetailsAdapter = new Consumption_Details_Adapter(context, Global.Consumption2list);
                Consumables_D_Rv.setAdapter(consumablesDetailsAdapter);
                consumablesDetailsAdapter.notifyDataSetChanged();
                getConsumablesDetails();
            }
        }, 2000);
    }

    private void getConsumablesDetails() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String consumables_d = Global.Get_Consumables_Details;
        String Consumable_Details_API = consumables_d + "con1_code=" + Global.ConsumptionClass.getCon1_code();
        // Toast.makeText(context, ""+Global.ConsumablesClass.getCon_no(), Toast.LENGTH_SHORT).show();

        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Consumable_Details_API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Global.Consumption2list = new ArrayList<ConsumptionClass2>();
                consumptionClass2 = new ConsumptionClass2();
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
                    consumptionClass2 = new ConsumptionClass2();
                    try {
                        consumptionClass2.setEquipment_Name(e.getString("equip_name"));
                        consumptionClass2.setEquipment_id(e.getString("equip_slno"));
                        consumptionClass2.setEquip_code(e.getString("equip_code"));
                        consumptionClass2.setItem_code(e.getString("item_code"));
                        consumptionClass2.setD_qty(e.getString("qty"));
                        consumptionClass2.setD_Amount(e.getString("prd_amt"));
                        consumptionClass2.setD_item(e.getString("part_no"));
                        consumptionClass2.setD_item_name(e.getString("prd_name"));
                        consumptionClass2.setD_unit(e.getString("unit_name"));
                        consumptionClass2.setD_rate(e.getString("prch_price"));

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
                    Global.Consumption2list.add(consumptionClass2);
                    Consumption_Details_Adapter consumablesDetailsAdapter = new Consumption_Details_Adapter(context, Global.Consumption2list);
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
                    Toast.makeText(Consumption_Details_Activity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Consumption_Details_Activity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Consumption_Details_Activity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Consumption_Details_Activity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Consumption_Details_Activity.this, "Parse Error", Toast.LENGTH_LONG).show();
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

        queue.add(jsonArrayRequest);
    }

    private void getEquipmentsSpinnerPopup() {
        zDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        zDialog.setContentView(R.layout.equipment_item);
        ListView lvEqName = zDialog.findViewById(R.id.lvequipment);
        /*TextView Equipment_name =zDialog.findViewById(R.id.euipment_name);
        TextView Equipment_id = zDialog.findViewById(R.id.euipment_id);*/
        getEquipmentsList();
        if (Global.Consumption_equipment == null || Global.Consumption_equipment.size() == 0) {
            Toast.makeText(getBaseContext(), "Equipment list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        EquipmentSelect_Adapter EqA = new EquipmentSelect_Adapter(Global.Consumption_equipment);
        lvEqName.setAdapter(EqA);

       /* Equipment_name.setText("Equipment Name");
        Equipment_id.setText("Equipment ID");*/
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
            TextView eqnameitem = v.findViewById(R.id.tvtwoeq);
            equipment_spinner = eQarrayList.get(i);

            equipmentnameitem.setText(equipment_spinner.getEquipment_Name());
            eqnameitem.setText(equipment_spinner.getEquipment_id());

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    equipment_spinner = eQarrayList.get(i);
                    Equipment_code.setText(equipment_spinner.getEquipment_Name());
                    zDialog.dismiss();
                }
            });

            layout.setOnClickListener(view1 -> {
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
                        ItemListClassConsumption item = new ItemListClassConsumption();

                        item.setItem(equipmentJson.getString("part_no"));
                        item.setItem_code(equipmentJson.getString("item_code"));
                        item.setItem_name(equipmentJson.getString("prd_name"));

                        //Log.d("YourTag", "Name: " + equipmentJson.getString("equip_name"));
                        // Log.d("YourTag", "Code: " + equipmentJson.getString("sstp1_code"));
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
                    Toast.makeText(Consumption_Details_Activity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Consumption_Details_Activity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Consumption_Details_Activity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Consumption_Details_Activity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Consumption_Details_Activity.this, "Parse Error", Toast.LENGTH_LONG).show();
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
        queue.add(jsonArrayRequest);

    }

    private void getItemSpinnerPopup() {

        zDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        zDialog.setContentView(R.layout.equipment_item);
        ListView lvItem = zDialog.findViewById(R.id.lvequipment);
       /* TextView Equipment_name =zDialog.findViewById(R.id.euipment_name);
        TextView Equipment_id = zDialog.findViewById(R.id.euipment_id);*/

        if (Global.Consumption_item == null || Global.Consumption_item.size() == 0) {
            Toast.makeText(getBaseContext(), "Item list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        ItemSelect_Adapter laItem = new ItemSelect_Adapter(Global.Consumption_item);
        lvItem.setAdapter(laItem);

       /* Equipment_name.setText("Item Name");
        Equipment_id.setText("Item ID");*/
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
                laItem.getFilter().filter(newText);
                return false;
            }
        });
    }

    public class ItemSelect_Adapter extends BaseAdapter implements Filterable {

        private ArrayList<ItemListClassConsumption> mDataArrayList;

        public ItemSelect_Adapter(ArrayList<ItemListClassConsumption> mDataArrayList) {
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
        @SuppressLint("MissingInflatedId")
        public View getView(int i, View convertView, ViewGroup parent) {

            View v = getLayoutInflater().inflate(R.layout.popup_itemlist_consumption, null);
            LinearLayout select_item = findViewById(R.id.select_item);
            TextView tvequipmentnameitem = v.findViewById(R.id.tvitemfirst);
            TextView tvenameitem = v.findViewById(R.id.tvitemtwoc);
            Item_spinner = mDataArrayList.get(i);
            tvequipmentnameitem.setText(Item_spinner.getItem_name());
            tvenameitem.setText(Item_spinner.getItem_code());

            tvenameitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item_spinner = mDataArrayList.get(i);
                    Item_codeTV.setText(Item_spinner.getItem_name());
                    zDialog.dismiss();
                }
            });

            return v;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    List<ItemListClassConsumption> mFilteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = Global.Consumption_item;
                    } else {
                        for (ItemListClassConsumption dataList : Global.Consumption_item) {
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
                    mDataArrayList = (ArrayList<ItemListClassConsumption>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }
}