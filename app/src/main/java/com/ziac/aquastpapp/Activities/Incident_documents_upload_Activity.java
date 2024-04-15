package com.ziac.aquastpapp.Activities;


import static com.yalantis.ucrop.util.FileUtils.getPath;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Adapters.Incident_documents_upload_Adapter;
import Models.IncidentsClass;

public class Incident_documents_upload_Activity<Payment_upload> extends AppCompatActivity  {

    Intent myFileIntent;
    ImageView upload;
    ProgressDialog dialog;
    Incident_documents_upload_Adapter documentAdapter;
    IncidentsClass incidentsClass;
    TextView In_doc_uploadbtn;
    private RecyclerView Incident_Documents_Rv;
    private ProgressDialog progressDialog;

    public ProgressDialog pDialog;
    private static final int REQUEST_CODE = 1;
    private File selectedFile;

    Context context;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CODE_DOC = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_documents_uploud);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        context = this;

        In_doc_uploadbtn = findViewById(R.id.in_doc_uploadbtn);
        upload = findViewById(R.id.in_upload);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        In_doc_uploadbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE);
                }
                openFileChooser();
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

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/pdf");
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void upload() {
        Intent intent = new Intent();
        intent.setType("application/pdf,application/msword");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DOC && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String path = getPath(Incident_documents_upload_Activity.this, uri);
                if (path != null) {
                    File file = new File(path);
                    uploadFile(file);
                }
            }
        }
    }

    private void uploadFile(File file) {
        String url = Global.Incident_UploadDocuments;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                // Handle response here
                try {
                    JSONObject resp = new JSONObject(resultResponse);

                    if (resp.getBoolean("success")) {
                        Global.customtoast(Incident_documents_upload_Activity.this, getLayoutInflater(), "Image uploaded successfully");
                        // getIncidentImages();

                    } else {
                        if (resp.has("error")) {
                            String errorMessage = resp.getString("error");
                            Toast.makeText(Incident_documents_upload_Activity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            Toast.makeText(Incident_documents_upload_Activity.this, "Image upload failed", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("else", "else");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error here
                    // Throw RuntimeException to propagate the exception if needed
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Unknown error";
                if (error instanceof TimeoutError) {
                    errorMessage = "Request timeout";
                } else if (error instanceof NoConnectionError) {
                    errorMessage = "Failed to connect server";
                } else if (error instanceof AuthFailureError) {
                    errorMessage = "Auth Failure Error";
                }
                Log.e("Error", errorMessage);
                error.printStackTrace();
            }
        }) {

            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<>();
                String fileString = fileToString(file);
                parameters.put("fileName", fileString);
                parameters.put("incident_code", Global.incidentsClass.getIncident_Code());
                parameters.put("com_code", Global.sharedPreferences.getString("com_code", ""));
                return parameters;
            }

            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // Add your file here
                params.put("file", new DataPart(file.getName(), getFileDataFromUri(file), "application/pdf"));
                return params;
            }
        };

        requestQueue.add(multipartRequest);
    }
    private String fileToString(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private byte[] getFileDataFromUri(File file) {
        byte[] byteArray = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byteArray = byteArrayOutputStream.toByteArray();
            fileInputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}




