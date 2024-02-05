package com.ziac.aquastpapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Handover_Remarks_Fragment extends Fragment {
    TextView Remark_submit;
    EditText Remark_edit;

    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_handover__remarks, container, false);
        getContext();
        Remark_edit = view.findViewById(R.id.remark_edit);
        Remark_submit = view.findViewById(R.id.remark_submit);
        Remark_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveRemark();
            }
        });
        return view;
    }

    private void SaveRemark() {
        String remark;
        remark = Remark_edit.getText().toString();
        if (remark.isEmpty()) {
            Toast.makeText(getActivity(), "Remark should not be empty !!", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Global.DailyLogUpdateHandOverRemark;

        String handover_remarks =remark;
        String com_code = Global.sharedPreferences.getString("com_code", null);
        String ayear = Global.sharedPreferences.getString("ayear", null);
        String tstp1_code = Global.sharedPreferences.getString("tstp1_code", null);

        url = url + "comcode="+com_code+"&ayear="+ayear +"&tstp1_code="+tstp1_code+"&handover_remarks="+handover_remarks;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String error = jsonObject.getString("error");
                    if (success) {
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        HomeFragment homeFragment = new HomeFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, homeFragment); // R.id.fragment_container is the ID of the container in your activity layout
                        transaction.addToBackStack("homeFragment");
                        transaction.commit();
                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("SaveRemark", "JSONException: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(getActivity(), "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getActivity(), "Parse Error", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
               /* params.put("handover_remarks",         String handover_remarks =
);
                params.put("com_code", Global.sharedPreferences.getString("com_code", null));
                params.put("ayear", Global.sharedPreferences.getString("ayear", null));
                params.put("tstp1_code", Global.sharedPreferences.getString("tstp1_code",null));
*/
                return params;
            }

        };
        queue.add(stringRequest);

    }
}