package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
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
import Models.zList;


public class PumpsFragment extends Fragment {
    private zList company_code ,location_code,stp1_code;
    RecyclerView PumpRecyclerview;
   // private ImageView mImageView;
    private String userimage;
    private TextView Manufacturer,EquipmentName,Specification,EquipmentNumber_Id,Rating_Capacity,
            FormFactor,Phase,CleaningRunningFrequencyHRS;
    CommonModelClass commonModelClassList;
    private ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pump, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //mImageView = view.findViewById(R.id._image1);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (!isNetworkAvailable()) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Internet connection lost !!");
        }
        new InternetCheckTask().execute();

       // String userimage = Global.userImageurl + Global.sharedPreferences.getString("name_plate", "");
      /*  Picasso.Builder builder=new Picasso.Builder(getActivity());
        Picasso picasso=builder.build();
        picasso.load(Uri.parse(userimage))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(mImageView );
*/
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);


        PumpRecyclerview = view.findViewById(R.id.pump_recyclerview);
        PumpRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        PumpRecyclerview.setHasFixedSize(true);
        PumpRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getpump();
        return view;
    }
    private void getpump() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Global.Equipment_Details_com_ ;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String site_code = Global.sharedPreferences.getString("site_code", "0");

        url = url + "com_code=" + com_code + "&sstp1_code=" + sstp1_code + "&site_code=" + site_code ;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.pumpdetails = new ArrayList<CommonModelClass>();
                commonModelClassList = new CommonModelClass();
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
                   commonModelClassList = new CommonModelClass();
                    try {

                        commonModelClassList.setImage(e.getString("name_plate"));
                        commonModelClassList.setEquipmentName(e.getString("equip_name"));
                        commonModelClassList.setRating_Capacity(e.getString("rating"));
                        commonModelClassList.setForm_Factor(e.getString("form_factor"));
                        commonModelClassList.setPhase(e.getString("phase"));
                        commonModelClassList.setManufacturer(e.getString("mfg_name"));
                        commonModelClassList.setEquipmentNumber_Id(e.getString("equip_code"));
                        commonModelClassList.setCleaning_RunningFrequency_HRS(e.getString("cleaning_freq_hrs"));

                        /*
                        Log.d("CommonModelData", "Equipment Name: " + commonModelClassList.getEquipmentName());
                        Log.d("CommonModelData", "Rating Capacity: " + commonModelClassList.getRating_Capacity());
                        Log.d("CommonModelData", "Form Factor: " + commonModelClassList.getForm_Factor());
                        Log.d("CommonModelData", "Phase: " + commonModelClassList.getPhase());
                        Log.d("CommonModelData", "Manufacturer: " + commonModelClassList.getManufacturer());
                        Log.d("CommonModelData", "Equipment Number/ID: " + commonModelClassList.getEquipmentNumber_Id());
                        Log.d("CommonModelData", "Cleaning Running Frequency HRS: " + commonModelClassList.getCleaning_RunningFrequency_HRS());*/

                       // commonModelClassList.setSstp2_code(e.getString("sstp2_code"));

                        ///Name plate
                       // commonModelClassList.setSpecification(e.getString(""));


                // Log.d("CommonModelData", "Cleaning Running Frequency HRS: " + commonModelClassList.getCleaning_RunningFrequency_HRS());



                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.pumpdetails.add(commonModelClassList);
                }

                CommonAdapter commonAdapter = new CommonAdapter(Global.pumpdetails, getContext());
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                params.put("equip_name", EquipmentName.getText().toString());
                params.put("rating", Rating_Capacity.getText().toString());
                params.put("form_factor",FormFactor.getText().toString());
                params.put("phase", Phase.getText().toString());
                params.put("mfg_name", Manufacturer.getText().toString());
               // params.put("state_code", String.valueOf(statename.get_code()));
               // params.put("city_code", String.valueOf(cityname.get_code()));
                params.put("equip_code", EquipmentNumber_Id.getText().toString());
                params.put("cleaning_freq_hrs", Specification.getText().toString());
                return params;
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