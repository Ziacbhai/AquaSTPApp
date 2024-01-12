package com.ziac.aquastpapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.Incident_documents_upload_Adapter;
import Models.Document;
import Models.IncidentsClass;

public class Incident_documents_upload_Activity extends AppCompatActivity {
    Bitmap documentBitmap;
    private List<IncidentsClass> fileList;
    ImageView upload;
    Uri pdfUri ;
    ProgressDialog dialog;
    private Incident_documents_upload_Adapter documentAdapter;
    IncidentsClass incidentsClass;
    TextView In_doc_uploadbtn;
    Uri documentUri;
    private RecyclerView Incident_Documents_Rv;

    private ProgressDialog progressDialog;
    private static final int REQUEST_PICK_DOCUMENT = 1;
    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_documents_uploud);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        context = this;
        In_doc_uploadbtn = findViewById(R.id.in_doc_uploadbtn);
        //upload = findViewById(R.id.in_upload);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (Global.isNetworkAvailable(context)) {
        } else {
            Global.customtoast(Incident_documents_upload_Activity.this, getLayoutInflater(), "Internet connection lost !!");
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);


        In_doc_uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDocumentPicker();
            }
        });

      /*  upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("application/pdf");
                startActivityForResult(galleryIntent, 1);
            }
        });*/

        Incident_Documents_Rv = findViewById(R.id.incident_Documents_Rv);
        Incident_Documents_Rv.setLayoutManager(new LinearLayoutManager(this));
        Incident_Documents_Rv.setHasFixedSize(true);
        Incident_Documents_Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getIncidentDocuments();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading");
            dialog.show();
            pdfUri = data.getData();
            uploadToDatabase(Uri.parse(String.valueOf(pdfUri)));
        }
    }

    private void showDocumentPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes = {"application/pdf", "text/plain", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, REQUEST_PICK_DOCUMENT);
    }


    private void uploadToDatabase(Uri pdfUri) {
        String url = Global.Incident_UploadDocuments; // Replace with your server URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle the response from the server
                    dialog.dismiss();
                    Toast.makeText(Incident_documents_upload_Activity.this, "PDF uploaded to database", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(Incident_documents_upload_Activity.this, "Error uploading to database", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fileName", pdfUri.toString());
                params.put("incident_code", Global.sharedPreferences.getString("incident_code", ""));
                params.put("com_code", Global.sharedPreferences.getString("com_code", ""));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 10 && resultCode == RESULT_OK) {
//            Uri uri = data.getData();
//            // imageList.add(uri);
//            try {
//                documentBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            Incident_postselectedDocument();
//        }
//    }
   /* private void Incident_postselectedDocument() {
        if (documentUri == null) {
            return;
        }
        String url = Global.Incident_UploadDocuments;
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
                    Global.customtoast(Incident_documents_upload_Activity.this, getLayoutInflater(), "Request Time-Out");
                } else if (error instanceof ServerError) {
                    Global.customtoast(Incident_documents_upload_Activity.this, getLayoutInflater(), "Invalid Username or Password");
                } else if (error instanceof ParseError) {
                    Global.customtoast(Incident_documents_upload_Activity.this, getLayoutInflater(), "Parse Error ");
                } else if (error instanceof AuthFailureError) {
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
                try {
                    String document = documentToString(documentUri);
                    params.put("fileName", document);
                    params.put("incident_code", Global.sharedPreferences.getString("incident_code", ""));
                    params.put("com_code", Global.sharedPreferences.getString("com_code", ""));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }*/

    /* private String documentToString(Uri documentUri) throws IOException {
         InputStream inputStream = getContentResolver().openInputStream(documentUri);
         byte[] bytes = new byte[inputStream.available()];
         inputStream.read(bytes);
         return Base64.encodeToString(bytes, Base64.DEFAULT);
     }*/
    private void getIncidentDocuments() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Global.Get_Incidents_Details + "incident_code=" + Global.sharedPreferences.getString("incident_code",
                "0") + "&file_type=" + "D";
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

                        // Toast.makeText(context, ""+ incidentsClass.getImageList(), Toast.LENGTH_SHORT).show();


                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.Incident_Class.add(incidentsClass);
                    Incident_documents_upload_Adapter Incident_documents_upload_Adapter = new Incident_documents_upload_Adapter(context, Global.Incident_Class);
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