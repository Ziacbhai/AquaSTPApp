package com.ziac.aquastpapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

import Adapters.Incident_image_upload_Adapter;
import Models.FiltersClass;
import Models.IncidentsClass;

public class FiltersDailyLog_Image_Upload_Activity extends AppCompatActivity {

    LinearLayout FilterImageUpload;
    Bitmap imageBitmap;

    FiltersClass filtersClass;
    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters_daily_log_image_upload);

        context = this;

        FilterImageUpload = findViewById(R.id.filters_image_upload);


        FilterImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
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
            Filter_image_postselelectedimage();
            finish();
        }
    }

    private void Filter_image_postselelectedimage() {
        if (imageBitmap == null) {
            return;
        }
        String url = Global.GetDailyLogFilterImageUpload;
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
                    Global.customtoast(FiltersDailyLog_Image_Upload_Activity.this, getLayoutInflater(), "Image uploaded successfully");
                    Intent intent = new Intent(FiltersDailyLog_Image_Upload_Activity.this,FiltersDailyLogActivity.class);
                    startActivity(intent);
                    finish();

                    //getFiltersImages();

                } else {
                    if (resp.has("error")) {
                        String errorMessage = resp.getString("error");
                        Toast.makeText(FiltersDailyLog_Image_Upload_Activity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Toast.makeText(FiltersDailyLog_Image_Upload_Activity.this, "Image upload failed", Toast.LENGTH_SHORT).show();

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
                params.put("fileName", image);
                params.put("tstp4_code", Global.filtersClass.getTstp4_code());
                params.put("com_code", Global.sharedPreferences.getString("com_code", ""));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private String imageToString(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {

    }
}