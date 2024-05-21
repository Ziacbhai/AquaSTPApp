package Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import Adapters.ItemStockAdapter;
import Models.ItemStockClass;
public class ItemStockFragment extends Fragment {
    RecyclerView consumableRecyclerView;
    ItemStockClass itemStockClass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        View view = inflater.inflate(R.layout.fragment_itemstock, container, false);

        consumableRecyclerView = view.findViewById(R.id.consumable_recyclerview);
        consumableRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        consumableRecyclerView.setHasFixedSize(true);
        consumableRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        GetItemStockItems();
        return view;
    }

    private void GetItemStockItems() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String urlstock = Global.GetItemStock;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String site_code = Global.sharedPreferences.getString("site_code", "0");
        urlstock = urlstock + "com_code=" + com_code + "&site_code=" + site_code;

        Log.d("SITECODE", "Site code" + site_code);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlstock, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Item_stock = new ArrayList<ItemStockClass>();
                itemStockClass = new ItemStockClass();
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
                    itemStockClass = new ItemStockClass();
                    try {
                        itemStockClass.set_Number(e.getString("full_prd_name"));
                        itemStockClass.set_Stock(e.getString("closing_stock"));
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Item_stock.add(itemStockClass);
                }
                ItemStockAdapter itemStockAdapter = new ItemStockAdapter(getContext(), Global.Item_stock);
                consumableRecyclerView.setAdapter(itemStockAdapter);
                //progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
}