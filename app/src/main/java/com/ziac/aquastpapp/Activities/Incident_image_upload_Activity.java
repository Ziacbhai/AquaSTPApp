package com.ziac.aquastpapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.List;
import java.util.Map;

import Adapters.Incident_image_upload_Adapter;
import Models.IncidentsClass;

public class Incident_image_upload_Activity extends AppCompatActivity {
    Bitmap imageBitmap;
    RecyclerView Incident_Images_Rv;
    IncidentsClass incidentsClass;
    ImageView In_image_uploadbtn;
    Context context;
    ImageView back_btn;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_incident_image_uploud);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        back_btn = findViewById(R.id.repair_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);
        In_image_uploadbtn = findViewById(R.id.incident_image_upload);
        In_image_uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        Incident_Images_Rv = findViewById(R.id.incident_Images_Rv);
        Incident_Images_Rv.setLayoutManager(new LinearLayoutManager(this));
        Incident_Images_Rv.setHasFixedSize(true);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(fadeInAnimation);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        Incident_Images_Rv.setLayoutAnimation(layoutAnimationController);
        Incident_Images_Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getIncidentImages();
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
            Incident_postselelectedimage();
        }
    }


    private void Incident_postselelectedimage() {
        if (imageBitmap == null) {
            return;
        }
        String url = Global.incident_UploadImage;
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
                    Global.customtoast(Incident_image_upload_Activity.this, getLayoutInflater(), "Image uploaded successfully");
                    getIncidentImages();

                } else {
                    if (resp.has("error")) {
                        String errorMessage = resp.getString("error");
                        Toast.makeText(Incident_image_upload_Activity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Toast.makeText(Incident_image_upload_Activity.this, "Image upload failed", Toast.LENGTH_SHORT).show();

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
                //params.put("incident_code", Global.incidentsClass.getIncident_No());
                params.put("incident_code", Global.incidentsClass.getIncident_Code());
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

    private void getIncidentImages() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Global.Get_Incidents_Details + "incident_code=" + Global.incidentsClass.getIncident_Code() + "&file_type=" + "I";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Global.Incident_Class = new ArrayList<IncidentsClass>();
                incidentsClass = new IncidentsClass();
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
                    incidentsClass = new IncidentsClass();
                    try {
                        incidentsClass.setImageList(e.getString("file_name"));
                        incidentsClass.setIn_image_name(e.getString("original_file_name"));
                        incidentsClass.setDelete_Incident_code2(e.getString("incident_code2"));

                        // Toast.makeText(context, ""+ incidentsClass.getImageList(), Toast.LENGTH_SHORT).show();


                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Incident_Class.add(incidentsClass);
                    Incident_image_upload_Adapter incidentImageUploadAdapter = new Incident_image_upload_Adapter(Global.Incident_Class, context);
                    Incident_Images_Rv.setAdapter(incidentImageUploadAdapter);
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