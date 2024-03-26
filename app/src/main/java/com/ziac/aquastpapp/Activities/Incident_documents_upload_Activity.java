package com.ziac.aquastpapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.Incident_documents_upload_Adapter;
import Models.IncidentsClass;
import Models.VolleyMultipartRequest;

public class Incident_documents_upload_Activity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private static final int REQUEST_CODE_FILE = 1;
    private static final int REQUEST_CODE_CAMERA = 2;

    List<IncidentsClass> FileList;
    InputStream inputStream;
    Context context;
    Bitmap imageBitmap;
    List<File> pdffiles;
    Bitmap documentBitmap;

    ImageView upload;
    Uri pdfUri;
    ProgressDialog dialog;
    private Incident_documents_upload_Adapter documentAdapter;
    IncidentsClass incidentsClass;
    TextView In_doc_uploadbtn;
    Uri documentUri;
    private RecyclerView Incident_Documents_Rv;

    private ProgressDialog progressDialog;
    private static final int REQUEST_PICK_DOCUMENT = 1;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_documents_uploud);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        In_doc_uploadbtn = findViewById(R.id.in_doc_uploadbtn);
        upload = findViewById(R.id.in_upload);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);

        In_doc_uploadbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Incident_documents_upload_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Incident_documents_upload_Activity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE);
                }
                openFileChooser();


            }
        });


        Incident_Documents_Rv = findViewById(R.id.incident_Documents_Rv);
        Incident_Documents_Rv.setLayoutManager(new LinearLayoutManager(this));
        Incident_Documents_Rv.setHasFixedSize(true);
        Incident_Documents_Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        runtimepermission();
        getdocumentslist();

    }
    private void runtimepermission() {
        Dexter.withContext(context).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }

        }).check();
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/pdf,application/*");
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                Uri selectedFileUri = data.getData();
                uploadDocs(selectedFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringPdf (Uri filepath){
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream =  getContentResolver().openInputStream(filepath);

            byte[] buffer = new byte[1024];
            byteArrayOutputStream = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        byte[] pdfByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
    }

    private void uploadDocs(Uri fileUri) throws IOException {
        String apiUrl = Global.Incident_UploadDocuments;


        //String documentToString = documentToString(documentBitmap);


        // Ensure fileUri is not null
        if (fileUri == null) {
            Toast.makeText(this, "Invalid fileUri", Toast.LENGTH_SHORT).show();
            return;
        }

        String documentToString = getStringPdf(fileUri);


        // Get the file path from the URI
        String filePath = getPathFromUri(fileUri);
        // Check if the file path is valid
        if (filePath == null) {
            Toast.makeText(this, "Invalid file path", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(filePath);
        if (file.exists()) {
            inputStream = getContentResolver().openInputStream(fileUri);
        } else {
            Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();

        }


        //if (inputStream != null) {
            Map<String, String> params = new HashMap<>();

            params.put("fileName", documentToString);
            params.put("incident_code", Global.incidentsClass.getIncident_Code());
            params.put("com_code", Global.sharedPreferences.getString("com_code", ""));



            RequestQueue requestQueue = Volley.newRequestQueue(this);

            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(apiUrl, file, inputStream,params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Handle the server response
                            Log.d("Response", response);
                            JSONObject resp;
                            try {
                                resp = new JSONObject(response);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            try {
                                if (resp.getBoolean("success")) {
                                    String Message = resp.getString("success");
                                    Global.customtoast(Incident_documents_upload_Activity.this, getLayoutInflater(), "File uploaded successfully");
                                    getdocumentslist();
                                    // Global.customtoast(ProfileActivity.this, getLayoutInflater(),Message);


                                } else {
                                    if (resp.has("error")) {

                                        String errorMessage = resp.getString("error");
                                        // Toast.makeText(UploadFileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(Incident_documents_upload_Activity.this, "File upload failed", Toast.LENGTH_SHORT).show();


                                    } else {
                                    }
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle errors
                            Log.e("Error", error.toString());
                        }
                    });

            multipartRequest.setTag("uploadRequest");

            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0, // timeout in milliseconds
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            requestQueue.add(multipartRequest);

        /*} else {
            // Handle the case where the InputStream is null
            Log.e("UploadDocs", "Failed to open InputStream from fileUri");
        }*/
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        } else {
            // Handle the case where the cursor is null
            return null;
        }
    }
    private String documentToString(Bitmap documentFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(documentFile.toString());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            byte[] documentBytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(documentBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    private void getdocumentslist () {
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
                        incidentsClass.setIn_doc_list(e.getString("file_name"));
                        incidentsClass.setInc_doc_name(e.getString("original_file_name"));
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

            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // params.put("incident_code", Global.incidentsClass.getIncident_No());
                String documentToString = documentToString(documentBitmap);
                params.put("fileName", documentToString);
                params.put("incident_code", Global.incidentsClass.getIncident_Code());
                params.put("com_code", Global.sharedPreferences.getString("com_code", ""));


                return params;
            }


        };
        queue.add(jsonObjectRequest);
    }

    @SuppressLint("Range")
    private File createTempFileFromInputStream(InputStream inputStream) throws IOException {
        // Create a temporary file
        File tempFile = File.createTempFile("temp_file", null);

        // Write the InputStream content to the temporary file
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
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
        } else if (uri.getScheme().equals("file")) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        finish();
    }
}