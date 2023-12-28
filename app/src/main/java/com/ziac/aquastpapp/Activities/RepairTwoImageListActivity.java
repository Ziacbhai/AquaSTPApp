package com.ziac.aquastpapp.Activities;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Adapters.Repairtwo_image_list_Adapter;
import Models.RepairClass2;


public class RepairTwoImageListActivity extends AppCompatActivity {
    Bitmap imageBitmap;
    RecyclerView Repair_Images_Rv;
    RepairClass2 repairClass2;
    AppCompatButton Repair_image_uploadbtn;
    ProgressDialog progressDialog;
    ImageView RepairImage;
    EditText Remark_repair;
    Context context;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_two_image_list);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);
        context = this;
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //RepairImage = findViewById(R.id.repairtqo_image_list);
        //Remark_repair = findViewById(R.id.repair_image_remark);
         Repair_image_uploadbtn = findViewById(R.id.repair_image_uploadbtn);

        if (!Global.isNetworkAvailable(this)) {
            Global.customtoast(this, getLayoutInflater(), "Internet connection lost !!");
        }
        new InternetCheckTask().execute();

        Repair_image_uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(context, RepairTwo_Upload_Activity.class);
                    context.startActivity(i);
            }
        });
        Repair_Images_Rv = findViewById(R.id.repair_two_imagelist_recyclerview);
        Repair_Images_Rv.setLayoutManager(new LinearLayoutManager(this));
        Repair_Images_Rv.setHasFixedSize(true);
        Repair_Images_Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRepairImages();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            // imageList.add(uri);
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Repair_postselelectedimage();
        }
    }
    private void getRepairImages() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Global.Repair_two_Imagelist + "repair2_code=" + Global.repairClass2.getD_Repairedtwo();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Global.repair2list = new ArrayList<RepairClass2>();
                repairClass2 = new RepairClass2();
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
                    repairClass2 = new RepairClass2();
                    try {
                        repairClass2.setI_Repair_image(e.getString("image_name"));
                        repairClass2.setD_Remark(e.getString("image_remarks"));
                        Toast.makeText(context, (e.getString("image_remarks")), Toast.LENGTH_SHORT).show();
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.repair2list.add(repairClass2);
                    Repairtwo_image_list_Adapter repairtwoImageUploadAdapter = new Repairtwo_image_list_Adapter(Global.repair2list, context);
                    Repair_Images_Rv.setAdapter(repairtwoImageUploadAdapter);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "TimeoutError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "NoConnectionError occurred", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            public Map<String, String> getHeaders() {

                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("incident_code", Global.sharedPreferences.getString("incident_code", "0"));
//                params.put("file_type", "I");
//                return params;
//            }

        };
        queue.add(jsonObjectRequest);
    }


}