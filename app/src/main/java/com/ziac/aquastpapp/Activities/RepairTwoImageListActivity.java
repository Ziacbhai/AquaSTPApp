package com.ziac.aquastpapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.Repairtwo_image_list_Adapter;
import Models.RepairsClass;

public class RepairTwoImageListActivity extends AppCompatActivity {
    Bitmap imageBitmap;
    RecyclerView Repair_Images_Rv;
    RepairsClass repairsClass;
    AppCompatButton Repair_image_uploadbtn;
    private ProgressDialog progressDialog;

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
                if (context != null) {
                    openCamera();
                } else {
                    Log.e("Camera", "Context is null");

                }

            }
        });

        Repair_Images_Rv = findViewById(R.id.repair_two_imagelist_recyclerview);
        Repair_Images_Rv.setLayoutManager(new LinearLayoutManager(this));
        Repair_Images_Rv.setHasFixedSize(true);
        Repair_Images_Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRepairImages();
    }

    private void openCamera() {
        try {
            if (context instanceof Activity) {
                com.github.dhaval2404.imagepicker.ImagePicker.with((Activity) context)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(10);
            } else {
                // Log.e("Camera", "Context is not an instance of Activity");
                // Handle the case where context is not an instance of Activity.
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e("Camera", "Error opening camera: " + e.getMessage());
            // Handle the exception as needed.
        }
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
            Repair_postselelectedimage();
        }
    }

    private void Repair_postselelectedimage() {
        if (imageBitmap == null) {
            return;
        }
        String url = Global.Repair_UploadImage;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            JSONObject resp;
            try {
                resp = new JSONObject(response);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                if (resp.getBoolean("success")) {
                    Global.customtoast(RepairTwoImageListActivity.this, getLayoutInflater(), "Image uploaded successfully");
                    // getRepairImages();

                } else {
                    if (resp.has("error")) {
                        String errorMessage = resp.getString("error");
                        //Toast.makeText(RepairTwoImageUploadActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Toast.makeText(RepairTwoImageListActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("else", "else");
                    }
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    Toast.makeText(RepairTwoImageListActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(RepairTwoImageListActivity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(RepairTwoImageListActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(RepairTwoImageListActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(RepairTwoImageListActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String image = imageToString(imageBitmap);
                // String remark = imageToString(imageBitmap);
                params.put("fileName", image);
                params.put("repaired_remarks", String.valueOf(Remark_repair));
                params.put("repair2_code", Global.sharedPreferences.getString("repair2_code", ""));
                params.put("com_code", Global.sharedPreferences.getString("com_code", ""));

                Log.d("YourTag", "Key: fileName, Value: " + image);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void getRepairImages() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Global.Repair_two_Imagelist + "repair2_code=" + Global.sharedPreferences.getString("repair2_code", "0");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Global.Repair_s = new ArrayList<RepairsClass>();
                repairsClass = new RepairsClass();
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
                    repairsClass = new RepairsClass();
                    try {
                        repairsClass.setRepairtwo_image(e.getString("image_name"));
                        repairsClass.setD_Remark(e.getString("image_remarks"));
                      //  Toast.makeText(context, "image_name" + repairsClass.getR_ImageList(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Repair_s.add(repairsClass);
                    Repairtwo_image_list_Adapter repairtwoImageUploadAdapter = new Repairtwo_image_list_Adapter(Global.Repair_s, context);
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

    private String imageToString(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }
}