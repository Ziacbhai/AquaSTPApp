package Fragments;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Adapters.ConsumptionAdapter;
import Adapters.ItemStockAdapter;
import Models.ItemStockModel;
public class ItemStockFragment extends Fragment {
    RecyclerView consumableRecyclerView;
    ItemStockModel itemStockModel;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        View view = inflater.inflate(R.layout.fragment_itemstock, container, false);

        consumableRecyclerView = view.findViewById(R.id.consumable_recyclerview);
        consumableRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        consumableRecyclerView.setHasFixedSize(true);
        consumableRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        GetItemStockItems();
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshScreen();
            }
        });

        return view;
    }

    private void refreshScreen() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                Global.Item_stock.clear();
                ItemStockAdapter itemStockAdapter = new ItemStockAdapter(getContext(), Global.Item_stock);
                consumableRecyclerView.setAdapter(itemStockAdapter);
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
                        consumableRecyclerView.setAdapter(itemStockAdapter);

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