package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.LabTestAdapter;
import Adapters.RepairAdapter;
import Models.CommonModelClass;
import Models.LabTestClass;
import Models.RepairsClass;
import Models.zList;


public class LabTestFragment extends Fragment {

    RecyclerView LabTestRecyclerview;
    LabTestClass labTestClass;
    AppCompatButton Update_A;
    TextView usersiteH,userstpH,usersiteaddressH ,Mailid,Mobno,personnameH;
    private String Personname,mail,Stpname ,Sitename ,SiteAddress,Process ,Mobile;
    LabTestAdapter labTestAdapter;
    private ProgressDialog progressDialog;
    Context context ;
    ImageView IMV;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_lab_test, container, false);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (!Global.isNetworkAvailable(getActivity())) {
            Global.customtoast(requireActivity(), getLayoutInflater(), "Internet connection lost !!");
        }
        new InternetCheckTask().execute();

        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        Sitename = sharedPreferences.getString("site_name", "");
        Stpname = sharedPreferences.getString("stp_name", "");
        SiteAddress = sharedPreferences.getString("site_address", "");
        Process = sharedPreferences.getString("process_name", "");
        mail = sharedPreferences.getString("user_email", "");
        Mobile = sharedPreferences.getString("user_mobile", "");
        Personname = sharedPreferences.getString("person_name", "");

        usersiteH = view.findViewById(R.id.site_name);
        userstpH = view.findViewById(R.id.stp_name);
        usersiteaddressH = view.findViewById(R.id.site_address);

        Mailid = view.findViewById(R.id.email);
        Mobno = view.findViewById(R.id._mobile);
        personnameH = view.findViewById(R.id.person_name);

        usersiteH.setText(Sitename);
        userstpH.setText(Stpname + " / " + Process);
        usersiteaddressH.setText(SiteAddress);

        Mailid.setText(mail);
        Mobno.setText(Mobile);
        personnameH.setText(Personname);
        FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        LabTestRecyclerview = view.findViewById(R.id.labTest_Recyclerview);
        LabTestRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        LabTestRecyclerview.setHasFixedSize(true);
        LabTestRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getLabTestReports();
        return view;
    }
    private void getLabTestReports() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String labTest = Global.GetLab_Test_Items;

        String comcode = Global.sharedPreferences.getString("com_code", "");
        String ayear = Global.sharedPreferences.getString("ayear", "2023");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "");
        labTest = labTest + "comcode=" +  comcode  + "&ayear=" + ayear  + "&sstp1_code=" + sstp1_code ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, labTest, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Labtest_s = new ArrayList<LabTestClass>();
                labTestClass = new LabTestClass();
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
                    labTestClass = new LabTestClass();
                    try {
                        labTestClass.setTRno(e.getString("test_code"));
                        labTestClass.setLabDate(e.getString("rcp_date"));
                        labTestClass.setCustomerRef(e.getString("cus_ref"));
                        labTestClass.setRefno(e.getString("ref_no"));
                        labTestClass.setLabRefDate(e.getString("ref_date"));
                        labTestClass.setSample_Received_Date(e.getString("rcp_date"));
                        labTestClass.setTest_Start_Date(e.getString("start_date"));
                        labTestClass.setTest_Completion_Date(e.getString("end_date"));
                        labTestClass.setSample_Received_By(e.getString("sample_receivedby"));
                        labTestClass.setSample_Particular(e.getString("sample_desc"));
                        labTestClass.setStatus(e.getString("test_status"));

                        String test_code= labTestClass.getTRno();
                        // System.out.println(repair_code);
                        Global.editor = Global.sharedPreferences.edit();
                        Global.editor.putString("test_code",test_code);
                        Global.editor.commit();

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Labtest_s.add(labTestClass);
                    labTestAdapter = new LabTestAdapter(Global.Labtest_s, getContext());
                    LabTestRecyclerview.setAdapter(labTestAdapter);
                }
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

                return params;
            }
        };
        queue.add(jsonObjectRequest);

    }
}