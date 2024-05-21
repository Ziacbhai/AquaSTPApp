package com.ziac.aquastpapp.Activities;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Incident_documents_upload_Activity<Payment_upload> extends AppCompatActivity {
    ImageView upload;
    TextView In_doc_uploadbtn;
    private RecyclerView Incident_Documents_Rv;
    private ProgressDialog progressDialog;
    public ProgressDialog pDialog;
    private static final int YOUR_REQUEST_CODE = 1;
    Context context;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_documents_uploud);
        context = this;

        In_doc_uploadbtn = findViewById(R.id.in_doc_uploadbtn);
        upload = findViewById(R.id.in_upload);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);


        Incident_Documents_Rv = findViewById(R.id.incident_Documents_Rv);
        Incident_Documents_Rv.setLayoutManager(new LinearLayoutManager(this));
        Incident_Documents_Rv.setHasFixedSize(true);
        Incident_Documents_Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        In_doc_uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/pdf", "application/msword", "image/*"});
                startActivityForResult(Intent.createChooser(intent, "Select a file"), YOUR_REQUEST_CODE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == YOUR_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                if (fileUri != null) {
                    String base64String = convertFileToBase64(fileUri);
                    if (base64String != null) {
                        uploadFile(base64String);
                    } else {
                        Toast.makeText(this, "Failed to convert file", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String convertFileToBase64(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(inputStream);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void uploadFile(String base64String) {
        String url = Global.Incident_UploadDocuments;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject resp = new JSONObject(response);
                        if (resp.getBoolean("success")) {
                            Global.customtoast(context, getLayoutInflater(), "File uploaded successfully");
                            // getIncidentFiles();
                        } else {
                            if (resp.has("error")) {
                                String errorMessage = resp.getString("error");
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("else", "else");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error response
                    Toast.makeText(context, "Error uploading file", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fileName", base64String);
                params.put("incident_code", Global.incidentsModelClass.getIncident_Code());
                params.put("com_code", Global.sharedPreferences.getString("com_code", ""));
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), // After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }




}




