package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Models.DailyLogClass;

public class Handover_Remarks_Activity extends AppCompatActivity {
    ImageView backbtn;
    TextView Remark_submit;
    EditText Remark_edit;
    String handover_remark;
    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handover_remarks);
        backbtn = findViewById(R.id.back_btn);
        Remark_edit = findViewById(R.id.remark_edit);
        Remark_submit = findViewById(R.id.remark_submit);
        context = this;

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Remark_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveRemark();
            }
        });
    }

    private void SaveRemark() {

        handover_remark = Remark_edit.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Global.DailyLogUpdateHandOverRemark;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String error = jsonObject.getString("error");
                    if (success) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                params.put("com_code", Global.sharedPreferences.getString("com_code", null));
                params.put("ayear", Global.sharedPreferences.getString("ayear", null));
               //params.put("tstp1_code",Global.dailyLogClass.getTstp1_code());
               params.put("tstp1_code","80274");
                params.put("handover_remarks",handover_remark);
                System.out.println(params);
                return params;
            }

        };
        queue.add(stringRequest);

    }
}