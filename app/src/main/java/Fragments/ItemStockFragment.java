package Fragments;


import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.Activities.Global;
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

import Adapters.ItemStockAdapter;
import Models.ItemStockModel;
public class ItemStockFragment extends Fragment {
    RecyclerView Item_stockRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView Displaydate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itemstock, container, false);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Item_stockRecyclerView = view.findViewById(R.id.itemstock_recyclerview);
        Item_stockRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Item_stockRecyclerView.setHasFixedSize(true);
        Item_stockRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Displaydate = view.findViewById(R.id.displaydate);

        GetItemStockItems();
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        user_topcard(view);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                handler.postDelayed(this, 1000); // Update every 1000 milliseconds (1 second)
            }
        }, 0);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshScreen();
            }
        });

        return view;
    }
    private void updateDateTime() {
        Date currentDate = new Date();
        // Update date
        SimpleDateFormat dateFormat = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            }
        }
        String formattedDate = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && dateFormat != null) {
            formattedDate = dateFormat.format(currentDate);
        }
        Displaydate.setText(formattedDate);

        SimpleDateFormat timeFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String formattedTime = timeFormat.format(currentDate);
            formattedTime = formattedTime.replace("am", "AM").replace("pm", "PM");

            //Displaytime.setText(formattedTime);
        }

    }

    private void user_topcard(View view) {

        TextView txtsitename, txtstpname, txtsiteaddress, txtuseremail, txtusermobile, txtpersonname;

        txtsitename = view.findViewById(R.id.sitename);
        txtstpname = view.findViewById(R.id.stpname);
        // txtsiteaddress = view.findViewById(R.id.siteaddress);
        txtuseremail = view.findViewById(R.id.useremail);
        txtusermobile = view.findViewById(R.id.usermobile);
        txtpersonname = view.findViewById(R.id.personname);

        txtsitename.setText(sharedPreferences.getString("site_name", ""));
        txtstpname.setText(sharedPreferences.getString("stp_name", "") + " " + sharedPreferences.getString("process_name", "") +  " " + sharedPreferences.getString("stp_capacity", ""));
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
                Global.Item_stock.clear();
                ItemStockAdapter itemStockAdapter = new ItemStockAdapter(getContext(), Global.Item_stock);
                Item_stockRecyclerView.setAdapter(itemStockAdapter);
                itemStockAdapter.notifyDataSetChanged();
                GetItemStockItems();
            }
        },2000);

    }

    private void GetItemStockItems() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String urlstock = Global.GetItemStock;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String site_code = Global.sharedPreferences.getString("site_code", "0");
        urlstock = urlstock + "com_code=" + com_code + "&site_code=" + site_code;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlstock, null,
                response -> {
                    try {
                        JSONArray jarray = response.getJSONArray("data");
                        Global.Item_stock = new ArrayList<>();

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject e = jarray.getJSONObject(i);
                            ItemStockModel itemStockModel = new ItemStockModel();
                            itemStockModel.set_Number(e.getString("full_prd_name"));
                            itemStockModel.set_Stock(e.getString("closing_stock"));
                            Global.Item_stock.add(itemStockModel);
                        }

                        ItemStockAdapter itemStockAdapter = new ItemStockAdapter(getContext(), Global.Item_stock);
                        Item_stockRecyclerView.setAdapter(itemStockAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    error.printStackTrace();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }

}