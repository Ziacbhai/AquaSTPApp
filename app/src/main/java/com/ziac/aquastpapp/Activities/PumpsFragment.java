package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.PumpDetailsAdapter;
import Models.CommonModelClass;
import Models.zList;


public class PumpsFragment extends Fragment {

    RecyclerView PumpRecyclerview;
    CommonModelClass commonModelClassList;
    Context context;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pump, container, false);

        context = getContext();
        user_topcard(view);


        PumpRecyclerview = view.findViewById(R.id.pump_recyclerview);
        PumpRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        PumpRecyclerview.setHasFixedSize(true);
        PumpRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getpump();
        return view;
    }

    private void user_topcard(View view) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        String personname, useremail, stpname, sitename, siteaddress, processname, usermobile,stpcapacity;
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        siteaddress = sharedPreferences.getString("site_address", "");
        processname = sharedPreferences.getString("process_name", "");
        useremail = sharedPreferences.getString("user_email", "");
        usermobile = sharedPreferences.getString("user_mobile", "");
        personname = sharedPreferences.getString("user_name", "");
        stpcapacity = sharedPreferences.getString("stp_capacity", "");

        TextView txtsitename, txtstpname, txtsiteaddress, txtuseremail, txtusermobile, txtpersonname;

        txtsitename = view.findViewById(R.id.sitename);
        txtstpname = view.findViewById(R.id.stpname);
        txtsiteaddress = view.findViewById(R.id.siteaddress);
        txtuseremail = view.findViewById(R.id.useremail);
        txtusermobile = view.findViewById(R.id.usermobile);
        txtpersonname = view.findViewById(R.id.personname);

        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " / " + processname +  " / " + stpcapacity);
        txtsiteaddress.setText(siteaddress);
        txtuseremail.setText(useremail);
        txtusermobile.setText(usermobile);
        txtpersonname.setText(personname);
    }

    private void getpump() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Global.Equipment_Details_com_pumps;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String site_code = Global.sharedPreferences.getString("site_code", "0");

        url = url + "com_code=" + com_code + "&sstp1_code=" + sstp1_code + "&site_code=" + site_code;

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
                        commonModelClassList.setEquipmentNumber_Id(e.getString("equip_slno"));
                        commonModelClassList.setRating_Capacity(e.getString("rating"));
                        commonModelClassList.setForm_Factor(e.getString("form_factor"));
                        commonModelClassList.setPhase(e.getString("phase"));
                        commonModelClassList.setSpecification(e.getString("equip_specs"));
                        commonModelClassList.setManufacturer(e.getString("mfg_name"));
                        commonModelClassList.setCleaning_RunningFrequency_HRS(e.getString("cleaning_freq_hrs"));


                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.pumpdetails.add(commonModelClassList);
                }

                PumpDetailsAdapter pumpDetailsAdapter = new PumpDetailsAdapter(Global.pumpdetails, getContext());
                PumpRecyclerview.setAdapter(pumpDetailsAdapter);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
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
                /*params.put("form_factor",FormFactor.getText().toString());*/
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

}