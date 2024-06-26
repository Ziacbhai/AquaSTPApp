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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import Models.FiltersModel;
public class FiltersDailyLogImageUploadActivity extends AppCompatActivity {

    LinearLayout FilterImageUpload;
    Bitmap imageBitmap;
    ImageView filter_image_back;
    FiltersModel filtersModel;
    Context context;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters_daily_log_image_upload);
        context = this;
        FilterImageUpload = findViewById(R.id.filters_image_upload);
        filter_image_back = findViewById(R.id.filter_image_back);
        filter_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FiltersDailyLogImageUploadActivity.this,FiltersDailyLogActivity.class));
                finish();
            }
        });
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
        }
    }
    private void Filter_image_postselelectedimage() {
        if (imageBitmap == null) {
            return;
        }
        String url = Global.GetDailyLogFilterImageUpload;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String sresponse) {
            try {
                JSONObject  jsonObject = new JSONObject(sresponse);
                boolean success = jsonObject.getBoolean("success");
                String error = jsonObject.getString("error");

                if (success) {
                    startActivity(new Intent(FiltersDailyLogImageUploadActivity.this,FiltersDailyLogActivity.class));
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
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
                params.put("tstp4_code", Global.filtersModel.getTstp4_code());
                params.put("com_code", Global.sharedPreferences.getString("com_code", ""));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0),0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private String imageToString(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }
}