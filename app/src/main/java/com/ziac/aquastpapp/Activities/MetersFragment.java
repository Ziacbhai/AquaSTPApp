package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.MetersDetailsAdapter;
import Adapters.PumpDetailsAdapter;
import Models.CommonModelClass;


public class MetersFragment extends Fragment {

    RecyclerView MetersRecyclerview;
    CommonModelClass commonModelClassList;
    private ProgressDialog progressDialog;
    TextView usersiteH, userstpH, usersiteaddressH, Mailid, Mobno, personnameH;
    private String Personname, Stpname, Sitename, SiteAddress, Process;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meters, container, false);


        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (Global.isNetworkAvailable(getActivity())) {
        } else {
            Global.customtoast(getActivity(), getLayoutInflater(), "Internet connection lost !!");
        }

        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);


        Sitename = sharedPreferences.getString("site_name", "");
        Stpname = sharedPreferences.getString("stp_name", "");
        SiteAddress = sharedPreferences.getString("site_address", "");
        Process = sharedPreferences.getString("process_name", "");
        String mail = Global.sharedPreferences.getString("user_email", "");
        String mobile = Global.sharedPreferences.getString("user_mobile", "");
        Personname = sharedPreferences.getString("person_nameu", "");

        usersiteH = view.findViewById(R.id.site_name);
        userstpH = view.findViewById(R.id.stp_name);
        usersiteaddressH = view.findViewById(R.id.site_address);
        //uProcess = view.findViewById(R.id.processname_);
        Mailid = view.findViewById(R.id.email);
        Mobno = view.findViewById(R.id._mobile);
        personnameH = view.findViewById(R.id.person_name);

        usersiteH.setText(Sitename);
        userstpH.setText(Stpname + " / " + Process);
        usersiteaddressH.setText(SiteAddress);
        //uProcess.setText(Process);
        Mailid.setText(mail);
        Mobno.setText(mobile);
        personnameH.setText(Personname);


        MetersRecyclerview = view.findViewById(R.id.meters_recyclerview);
        MetersRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        MetersRecyclerview.setHasFixedSize(true);
        MetersRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getmeters();
        return view;
    }

    private void getmeters() {

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = Global.Equipment_Details_com_meters;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String site_code = Global.sharedPreferences.getString("site_code", "0");

        url = url + "com_code=" + com_code + "&sstp1_code=" + sstp1_code + "&site_code=" + site_code;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.metersdetails = new ArrayList<CommonModelClass>();
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
                        commonModelClassList.setSpecification(e.getString("equip_specs"));
                        commonModelClassList.setEquipmentNumber_Id(e.getString("equip_slno"));
                        commonModelClassList.setCleaning_RunningFrequency_HRS(e.getString("cleaning_freq_hrs"));


                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.metersdetails.add(commonModelClassList);
                }
                MetersDetailsAdapter metersDetailsAdapter = new MetersDetailsAdapter(Global.metersdetails, getContext());
                MetersRecyclerview.setAdapter(metersDetailsAdapter);
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
                /*params.put("equip_name", EquipmentName.getText().toString());
                params.put("site_address", Global.sharedPreferences.getString("site_address", null).toString());
                params.put("rating", Rating_Capacity.getText().toString());
                params.put("form_factor",FormFactor.getText().toString());
                params.put("phase", Phase.getText().toString());
                params.put("equip_specs", Specification.getText().toString());
                params.put("mfg_name", Manufacturer.getText().toString());
                params.put("equip_slno", EquipmentNumber_Id.getText().toString());
                params.put("cleaning_freq_hrs", CleaningRunningFrequencyHRS.getText().toString());*/
                return params;
            }
        };
        queue.add(jsonObjectRequest);


    }

}