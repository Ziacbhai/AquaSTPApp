package com.ziac.aquastpapp.Activities;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    private static final int REQUEST_FILE = 1;
    private static final int PICK_FILE_REQUEST_CODE = 1;
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

        In_doc_uploadbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                openFileManager();
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);


        Incident_Documents_Rv = findViewById(R.id.incident_Documents_Rv);
        Incident_Documents_Rv.setLayoutManager(new LinearLayoutManager(this));
        Incident_Documents_Rv.setHasFixedSize(true);
        Incident_Documents_Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    private void openFileManager() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_FILE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                if (fileUri != null) {
                    String fileType = getFileType(fileUri);
                    if (fileType != null) {
                        uploadFile(fileType, fileUri);
                    } else {
                        Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "File URI is null", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Intent data is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFile(String fileType, Uri fileUri) {
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
        };



        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    private String getFileType(Uri fileUri) {
        String mimeType = getContentResolver().getType(fileUri);
        if (mimeType != null) {
            if (mimeType.contains("pdf")) {
                return "pdf";
            } else if (mimeType.contains("doc") || mimeType.contains("docx")) {
                return "doc";
            } else {
                return null; // Unsupported file type
            }
        } else {
            // If MIME type is null, try to determine file type based on file extension
            String extension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());
            if (extension != null) {
                if (extension.equals("pdf")) {
                    return "pdf";
                } else if (extension.equals("doc") || extension.equals("docx")) {
                    return "doc";
                } else {
                    return null; // Unsupported file type
                }
            } else {
                return null; // Unable to determine file type
            }
        }
    }


}




