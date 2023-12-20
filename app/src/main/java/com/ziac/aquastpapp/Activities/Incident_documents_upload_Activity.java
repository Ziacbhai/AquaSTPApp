package com.ziac.aquastpapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.Incident_documents_upload_Adapter;
import Models.IncidentsClass;

public class Incident_documents_upload_Activity extends AppCompatActivity {
    Bitmap imageBitmap;
    private List<IncidentsClass> fileList;
    private static final int PICK_FILE_REQUEST = 1;
    private static final int REQUEST_PERMISSION = 100;
    private Incident_documents_upload_Adapter documentAdapter;
    IncidentsClass incidentsClass;
    TextView In_doc_uploadbtn;

    Uri uri;
    private RecyclerView Incident_Documents_Rv;

    private static final int FILE_SELECT_CODE = 1;
    private ProgressDialog progressDialog;
    private static final int REQUEST_PICK_DOCUMENT = 1;
    Context context;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_documents_uploud);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        In_doc_uploadbtn = findViewById(R.id.in_doc_uploadbtn);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        context = this;
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!Global.isNetworkAvailable(this)) {
            Global.customtoast(this, getLayoutInflater(), "Internet connection lost !!");
        }
        new InternetCheckTask().execute();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);


        In_doc_uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDocumentPicker();
            }
        });

        Incident_Documents_Rv = findViewById(R.id.incident_Documents_Rv);
        Incident_Documents_Rv.setLayoutManager(new LinearLayoutManager(this));
        Incident_Documents_Rv.setHasFixedSize(true);
        Incident_Documents_Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getIncidentDocuments();


    }

    private void showDocumentPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes = {"application/pdf", "text/plain", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, REQUEST_PICK_DOCUMENT);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_DOCUMENT && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedFileUri = data.getData();
                // Handle the selected file URI (e.g., display the file name)
                String fileName = getFileName(selectedFileUri);
                Toast.makeText(this, "Selected file: " + fileName, Toast.LENGTH_SHORT).show();
                // Call your method with the documentUri
                Incident_postselelecteddocuments(selectedFileUri);
            }
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private void Incident_postselelecteddocuments(Uri documentUri) {
        String url = Global.In_UploadDoc;
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
                    Global.customtoast(Incident_documents_upload_Activity.this, getLayoutInflater(), "Document uploaded successfully");
                    getIncidentDocuments();
                } else {
                    if (resp.has("error")) {
                        String errorMessage = resp.getString("error");
                        Toast.makeText(Incident_documents_upload_Activity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Toast.makeText(Incident_documents_upload_Activity.this, "Document upload failed", Toast.LENGTH_SHORT).show();
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
                    Global.customtoast(Incident_documents_upload_Activity.this, getLayoutInflater(),"Request Time-Out");
                } else if (error instanceof ServerError) {
                    Global.customtoast(Incident_documents_upload_Activity.this, getLayoutInflater(),"Invalid Username or Password");
                }  else if (error instanceof ParseError) {
                    Global.customtoast(Incident_documents_upload_Activity.this, getLayoutInflater(),"Parse Error ");
                }  else if (error instanceof AuthFailureError) {
                    Global.customtoast(Incident_documents_upload_Activity.this, getLayoutInflater(), "AuthFailureError");
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
                // Convert the document file to Base64 string
                String docBase64 = documentToBase64(documentUri);
                params.put("fileName", docBase64);
                params.put("incident_code", Global.sharedPreferences.getString("incident_code", ""));
                params.put("com_code", Global.sharedPreferences.getString("com_code", ""));
                Log.d("YourTag", "Key: document, Value: " + docBase64);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    private String documentToBase64(Uri documentUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(documentUri);
            if (inputStream != null) {
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String base64String = Base64.encodeToString(buffer, Base64.DEFAULT);
                Log.d("YourTag", "Base64 Document: " + base64String);
                return base64String;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void getIncidentDocuments() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Global.Get_Incidents_Details + "incident_code=" + Global.sharedPreferences.getString("incident_code",
                "0") + "&file_type=" + "D";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Global.Incident_s = new ArrayList<IncidentsClass>();
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

                        // Toast.makeText(context, ""+ incidentsClass.getImageList(), Toast.LENGTH_SHORT).show();


                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Incident_s.add(incidentsClass);
                    Incident_documents_upload_Adapter Incident_documents_upload_Adapter = new Incident_documents_upload_Adapter(context, Global.Incident_s);
                    Incident_Documents_Rv.setAdapter(Incident_documents_upload_Adapter);
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

           protected Map<String, String> getParams() {
               Map<String, String> params = new HashMap<>();
                /*params.put("incident_code", Global.sharedPreferences.getString("incident_code", "0"));
                params.put("file_type", "I");*/
                return params;
            }

        };
        queue.add(jsonObjectRequest);
    }

}