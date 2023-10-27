package com.ziac.aquastpapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.CommonAdapter;
import Models.CommonModelClass;


public class PumpsFragment extends Fragment {

    RecyclerView PumpRecyclerview;
    CommonModelClass commonModelClassList;
    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (!isNetworkAvailable()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Internet connection lost !!");
        }
        new InternetCheckTask().execute();

        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        View view = inflater.inflate(R.layout.fragment_pump, container, false);
        PumpRecyclerview = view.findViewById(R.id.pump_recyclerview);
        PumpRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        PumpRecyclerview.setHasFixedSize(true);
        PumpRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getpump();
        return view;
    }

    private void getpump() {

        if (!isNetworkAvailable()) {

            Global.customtoast(requireActivity(), getLayoutInflater(), "Internet connection lost !!");
        } else {
            progressDialog.show();
        }

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Global.urlmystock ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.equipmentNameslist = new ArrayList<CommonModelClass>();
                commonModelClassList = new CommonModelClass();
                JSONArray jarray;

                try {
                    jarray = response.getJSONArray("mvh_vehmas_vu");
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
                   commonModelClassList = new CommonModelClass();
                    try {

                        commonModelClassList.setEquipmentName(e.getString("vehmas_code"));
                        commonModelClassList.setManufacturer(e.getString("vmodel_code").toUpperCase());
                        commonModelClassList.setEquipmentNumber_Id(e.getString("veh_image1"));
                        commonModelClassList.setForm_Factor(e.getString("model_name"));
                        commonModelClassList.setRating_Capacity(e.getString("year_of_mfg"));
                        commonModelClassList.setSpecification(e.getString("year_of_mfg"));
                        commonModelClassList.setRating_Capacity(e.getString("year_of_mfg"));
                        commonModelClassList.setPhase(e.getString("year_of_mfg"));
                        commonModelClassList.setCleaning_RunningFrequency_HRS(e.getString("transmission_type_name").toUpperCase());


                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.equipmentNameslist.add(commonModelClassList);
                }

                CommonAdapter commonAdapter = new CommonAdapter(Global.equipmentNameslist, getContext());
                PumpRecyclerview.setAdapter(commonAdapter);
                progressDialog.dismiss();
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
        };
        queue.add(jsonObjectRequest);
    }

    private boolean isNetworkAvailable() {
        Context context = requireContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private class InternetCheckTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(3000);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}