package Fragments;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import Adapters.MetersDetailsAdapter;
import Models.CommonModelClass;


public class MetersFragment extends Fragment {

    RecyclerView MetersRecyclerview;
    CommonModelClass commonModelClassList;
    private ProgressDialog progressDialog;
    Context context;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meters, container, false);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        context = getContext();
        user_topcard(view);

        MetersRecyclerview = view.findViewById(R.id.meters_recyclerview);
        MetersRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        MetersRecyclerview.setHasFixedSize(true);
        MetersRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getmeters();
        return view;
    }

    private void user_topcard(View view) {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        String personname, useremail, stpname, sitename, siteaddress, processname, usermobile, stpcapacity;

        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        processname = sharedPreferences.getString("process_name", "");
        siteaddress = sharedPreferences.getString("site_address", "");
        useremail = sharedPreferences.getString("user_email", "");
        usermobile = sharedPreferences.getString("user_mobile", "");
        personname = sharedPreferences.getString("user_name", "");
        stpcapacity = sharedPreferences.getString("stp_capacity", "");

        TextView txtsitename, txtstpname, txtsiteaddress, txtuseremail, txtusermobile, txtpersonname;

        txtsitename = view.findViewById(R.id.sitename);
        txtstpname = view.findViewById(R.id.stpname);
       // txtsiteaddress = view.findViewById(R.id.siteaddress);
        txtuseremail = view.findViewById(R.id.useremail);
        txtusermobile = view.findViewById(R.id.usermobile);
        txtpersonname = view.findViewById(R.id.personname);

        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " " + processname + " " + stpcapacity);
       // txtsiteaddress.setText(siteaddress);
        txtuseremail.setText(useremail);
        txtusermobile.setText(usermobile);
        txtpersonname.setText(personname);
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
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);


    }

}