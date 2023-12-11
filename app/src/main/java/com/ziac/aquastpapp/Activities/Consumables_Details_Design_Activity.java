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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Adapters.Consumables_Details_Adapter;
import Models.ConsumablesClass;

public class Consumables_Details_Design_Activity extends AppCompatActivity {
    ConsumablesClass consumables_Class;
    RecyclerView Consumables_D_Rv;
    Context context;
    private ConsumablesClass equipment_spinner;
    private ConsumablesClass Item_spinner;
    private Dialog zDialog;
    TextView usersiteH, userstpH, usersiteaddressH, Mailid, Mobno, personnameH;
    private String Personname, Mail, Stpname, Sitename, SiteAddress, Process, Mobile;
    TextView Equipment_name_cd, Item_cd, Qty_cb;
    ImageView Equipment_name_cd_spinner, Item_cd_spinner;
    AppCompatButton Update_A, Cancel_A;
    private ProgressDialog progressDialog;

    private ConsumablesClass equipment_;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumables_details_design);
        context = this;

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!Global.isNetworkAvailable(this)) {
            Global.customtoast(this, getLayoutInflater(), "Internet connection lost !!");
        }
        new InternetCheckTask().execute();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        Sitename = sharedPreferences.getString("site_name", "");
        Stpname = sharedPreferences.getString("stp_name", "");
        SiteAddress = sharedPreferences.getString("site_address", "");
        Process = sharedPreferences.getString("process_name", "");
        Mail = sharedPreferences.getString("user_email", "");
        Mobile = sharedPreferences.getString("user_mobile", "");
        Personname = sharedPreferences.getString("person_name", "");

        usersiteH = findViewById(R.id.site_name);
        userstpH = findViewById(R.id.stp_name);
        usersiteaddressH = findViewById(R.id.site_address);
        Mailid = findViewById(R.id.email);
        Mobno = findViewById(R.id._mobile);
        personnameH = findViewById(R.id.person_name);

        usersiteH.setText(Sitename);
        userstpH.setText(Stpname + " / " + Process);
        usersiteaddressH.setText(SiteAddress);
        Mailid.setText(Mail);
        Mobno.setText(Mobile);
        personnameH.setText(Personname);
        getConsumables_Details();
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDetailsDialog(context);
            }
        });

        Consumables_D_Rv = findViewById(R.id.consumables_details_recyclerview);
        Consumables_D_Rv.setLayoutManager(new LinearLayoutManager(context));
        Consumables_D_Rv.setHasFixedSize(true);
        Consumables_D_Rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

    }

    @SuppressLint("MissingInflatedId")
    private void showAddDetailsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_consumable_details_layout, null);
        Equipment_name_cd = dialogView.findViewById(R.id.equipment_name_alert_spinner);
        Item_cd = dialogView.findViewById(R.id.item_name_alert_spinner);
        getEquipmentsSpinner();
       getItemSpinner();
        Equipment_name_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getEquipmentsSpinnerPopup();
            }
        });
        Item_cd.setOnClickListener(new View.OnClickListener() {
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

        Update_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateConsumables_details();
                dialog.dismiss(); // Close the dialog if needed
            }
        });
        Cancel_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void updateConsumables_details() {
        String equipmentID = Equipment_name_cd.getText().toString();
        String qty = Qty_cb.getText().toString();
        String item_cd = Item_cd.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(context);

        String url = Global.updateDConsumables + "type=" + "U";

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
                        Toast.makeText(context, "Updated successfully !!", Toast.LENGTH_SHORT).show();
                        getConsumables_Details();
                    } else {
                        Toast.makeText(context, response.getString("error"), Toast.LENGTH_SHORT).show();

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
                params.put("item_code", item_cd);
                params.put("equip_code", equipmentID);
                params.put("qty", qty);
                params.put("com_code", Global.sharedPreferences.getString("com_code", "0"));
                params.put("ayear", Global.sharedPreferences.getString("ayear", "0"));
                params.put("sstp1_code", Global.sharedPreferences.getString("sstp1_code", "0"));
                params.put("con1_code", "0");
                return params;
            }


        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(2500), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);

    }

    private void getConsumables_Details() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String consumables_d = Global.Get_Consumables_Details;
        String Consumable_Details_API = consumables_d + "con1_code=" + Global.sharedPreferences.getString("con1_code", "0");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Consumable_Details_API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Global.Consumables_s = new ArrayList<ConsumablesClass>();
                consumables_Class = new ConsumablesClass();
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
                    consumables_Class = new ConsumablesClass();
                    try {
                        consumables_Class.setEquipment_Name(e.getString("equip_name"));
                        consumables_Class.setEquipment_id(e.getString("equip_slno"));
                        consumables_Class.setD_Amount(e.getString("prd_amt"));
                        consumables_Class.setD_item(e.getString("part_no"));
                        consumables_Class.setD_item_name(e.getString("prd_name"));
                        consumables_Class.setD_qty(e.getString("qty"));
                        consumables_Class.setD_unit(e.getString("unit_name"));
                        consumables_Class.setD_rate(e.getString("prch_price"));

                     /*   String equipslno = e.getString("equip_slno");
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.putString("equip_slno",equipslno);
                        // Log.d("YourTag", "incident_code" + incident_code);
                        //Toast.makeText(context, ""+incident_code, Toast.LENGTH_SHORT).show();
                        Global.editor.commit();*/

                        Log.d("YourTag", "Equipment Name: " + consumables_Class.getEquipment_Name());
                        Log.d("YourTag", "Equipment ID: " + consumables_Class.getEquipment_id());
                        Log.d("YourTag", "D Amount: " + consumables_Class.getD_Amount());

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Consumables_s.add(consumables_Class);
                    Consumables_Details_Adapter consumablesDetailsAdapter = new Consumables_Details_Adapter(context, Global.Consumables_s);
                    Consumables_D_Rv.setAdapter(consumablesDetailsAdapter);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Global.customtoast(Consumables_Details_Design_Activity.this,getLayoutInflater(), error.getMessage());
                if (error instanceof TimeoutError) {
                    Toast.makeText(Consumables_Details_Design_Activity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Consumables_Details_Design_Activity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Consumables_Details_Design_Activity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Consumables_Details_Design_Activity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Consumables_Details_Design_Activity.this, "Parse Error", Toast.LENGTH_LONG).show();
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

    private void getEquipmentsSpinner() {

        String Url = Global.Get_Equipment_select + "comcode=" + Global.sharedPreferences.getString("com_code", "0");
        Url = Url + "&sstp1_code=" + Global.sharedPreferences.getString("sstp1_code", "0");

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Global.Consumables_s = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject equipmentJson = response.getJSONObject(i);
                                ConsumablesClass equipment = new ConsumablesClass();

                                equipment.setEquipment_id(equipmentJson.getString("equip_slno"));
                                equipment.setEquipment_Name(equipmentJson.getString("equip_name"));

                               /* Log.d("YourTag", "Name: " + equipmentJson.getString("equip_name"));
                                Log.d("YourTag", "Code: " + equipmentJson.getString("sstp1_code"));*/


                                Global.Consumables_s.add(equipment);

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

        zDialog.setContentView(R.layout.popup_select_equipment);

        ListView lvEqName = zDialog.findViewById(R.id.lvequipment_name);

        if (Global.Consumables_s == null || Global.Consumables_s.size() == 0) {
            Toast.makeText(getBaseContext(), "Equipment list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        final EquipmentSelect_Adapter laStates = new EquipmentSelect_Adapter(Global.Consumables_s);
        lvEqName.setAdapter(laStates);
        zDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        zDialog.show();

        SearchView sveq = zDialog.findViewById(R.id.sveq);

        sveq.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                laStates.getFilter().filter(newText);
                return false;
            }
        });
    }

    private class EquipmentSelect_Adapter extends BaseAdapter implements Filterable {
        private ArrayList<ConsumablesClass> mDataArrayList;

        public EquipmentSelect_Adapter(ArrayList<ConsumablesClass> mDataArrayList) {
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
            View v = getLayoutInflater().inflate(R.layout.popup_equipmentlist, null);
            TextView tvequipmentnameitem = v.findViewById(R.id.tvsingle);
            TextView tvenameitem = v.findViewById(R.id.tvtwo);

            equipment_spinner = mDataArrayList.get(i);

            tvequipmentnameitem.setText(equipment_spinner.getEquipment_Name());
            tvenameitem.setText(equipment_spinner.getEquipment_id());

            tvequipmentnameitem.setOnClickListener(view1 -> {
                equipment_spinner = mDataArrayList.get(i);
                Equipment_name_cd.setText(equipment_spinner.getEquipment_Name());
                zDialog.dismiss();
            });

            return v;
        }


        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    List<ConsumablesClass> mFilteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = Global.Consumables_s;
                    } else {
                        for (ConsumablesClass dataList : Global.Consumables_s) {
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
                    mDataArrayList = (ArrayList<ConsumablesClass>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }


    private void getItemSpinner() {
        String Url = Global.Get_Item_select + "comcode=" + Global.sharedPreferences.getString("com_code", "0");

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Global.Consumables_s = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject equipmentJson = response.getJSONObject(i);
                        ConsumablesClass equipment = new ConsumablesClass();

                        equipment.setD_item(equipmentJson.getString("part_no"));
                        equipment.setD_item_name(equipmentJson.getString("prd_name"));

                        //Log.d("YourTag", "Name: " + equipmentJson.getString("equip_name"));
                       // Log.d("YourTag", "Code: " + equipmentJson.getString("sstp1_code"));

                        Global.Consumables_s.add(equipment);

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(Consumables_Details_Design_Activity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Consumables_Details_Design_Activity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Consumables_Details_Design_Activity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Consumables_Details_Design_Activity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Consumables_Details_Design_Activity.this, "Parse Error", Toast.LENGTH_LONG).show();}

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

        zDialog.setContentView(R.layout.popup_list);

        ListView lvItem= zDialog.findViewById(R.id.lvstates);

        if (Global.Consumables_s == null || Global.Consumables_s.size() == 0) {
            Toast.makeText(getBaseContext(), "Item list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        final ItemSelect_Adapter laItem = new ItemSelect_Adapter(Global.Consumables_s);
        lvItem.setAdapter(laItem);
        zDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        zDialog.show();

        SearchView sveq = zDialog.findViewById(R.id.svstate);

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

        private ArrayList<ConsumablesClass> mDataArrayList;

        public ItemSelect_Adapter(ArrayList<ConsumablesClass> mDataArrayList) {
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

            View v = getLayoutInflater().inflate(R.layout.popup_itemlist_consumable, null);
           TextView tvequipmentnameitem = v.findViewById(R.id.tvitemfirst);
           TextView tvenameitem = v.findViewById(R.id.tvitemtwo);

            Item_spinner = mDataArrayList.get(i);

            tvequipmentnameitem.setText(Item_spinner.getD_item_name());
            tvenameitem.setText(Item_spinner.getD_item());

            tvequipmentnameitem.setOnClickListener(view1 -> {
                Item_spinner = mDataArrayList.get(i);
                Item_cd.setText(Item_spinner.getD_item_name());
                zDialog.dismiss();
            });

            return v;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    List<ConsumablesClass> mFilteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = Global.Consumables_s;
                    } else {
                        for (ConsumablesClass dataList : Global.Consumables_s) {
                            if (dataList.getD_item_name().toLowerCase().contains(charString) ||
                                    dataList.getD_item().toLowerCase().contains(charString)) {
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
                    mDataArrayList = (ArrayList<ConsumablesClass>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }


}