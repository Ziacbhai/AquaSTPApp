package com.ziac.aquastpapp.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.ConsumablesAdapter;
import Models.ConsumableClass;


public class ConsumablesFragment extends Fragment {
    RecyclerView consumableRecyclerView;
    ConsumableClass consumableClass;
    TextView Consumables_Code ,Consumables_Name,Consumables_Stock,Consumables_Units;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        View view =  inflater.inflate(R.layout.fragment_consumables, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        consumableRecyclerView = view.findViewById(R.id.consumable_recyclerview);
        consumableRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        consumableRecyclerView.setHasFixedSize(true);
        consumableRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        GetItemStockItems();
        return view;
    }

    private void GetItemStockItems() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String urlstock = Global.GetItemStock ;

        String com_code = Global.sharedPreferences.getString("com_code", "0");

        String site_code = Global.sharedPreferences.getString("site_code", "0");

        urlstock = urlstock + "com_code=" + com_code  + "&site_code=" + site_code ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlstock, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.consumables_stock = new ArrayList<ConsumableClass>();
                consumableClass = new ConsumableClass();
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
                    consumableClass = new ConsumableClass();
                    try {

                        consumableClass.set_Code(e.getString("part_no"));
                        consumableClass.set_Name(e.getString("prd_name"));
                        consumableClass.set_Stock(e.getString("stk_ycb"));
                        consumableClass.set_Units(e.getString("unit_name"));

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.consumables_stock.add(consumableClass);
                }

                ConsumablesAdapter consumablesAdapter = new ConsumablesAdapter(getContext(), Global.consumables_stock);
                consumableRecyclerView.setAdapter(consumablesAdapter);
                //progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            public Map<String, String> getHeaders() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("part_no", Consumables_Code.getText().toString());
                params.put("prd_name", Consumables_Name.getText().toString());
                params.put("stk_ycb",Consumables_Stock.getText().toString());
                params.put("unit_name", Consumables_Units.getText().toString());

                return params;
            }
        };
        queue.add(jsonObjectRequest);




    }
}