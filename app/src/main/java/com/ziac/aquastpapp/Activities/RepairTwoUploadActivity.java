package com.ziac.aquastpapp.Activities;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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

public class RepairTwoUploadActivity extends AppCompatActivity {

    ImageView RtwoImage;
    EditText Repair_two_Remark;
    Bitmap imageBitmap;
    ImageView Repair_back_btn;
    AppCompatButton Repair_two_image_upload_btn, Repairtwo_image_cancel_btn;
    Context context;
    private static final int PICK_IMAGE_REQUEST_CODE = 10;
    private static final int CAMERA_REQUEST_CODE = 1;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_two_upload);
        context = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(true);
        RtwoImage = findViewById(R.id.repairtwo_image_upload);
        Repair_two_Remark = findViewById(R.id.repair_two_Remark);
        Repair_two_image_upload_btn = findViewById(R.id.repairtwo_image_upload_btn);
        Repairtwo_image_cancel_btn = findViewById(R.id.repairtwo_image_cancel_btn);
        Repair_back_btn = findViewById(R.id.repair_back_btn);



        Repair_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RtwoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        Repair_two_image_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRepairImage();
            }
        });
        Repairtwo_image_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RepairTwoUploadActivity.this, RepairTwoImageListActivity.class));
            }
        });
    }

    private void updateRepairImage() {
        if (imageBitmap == null) {
            return;
        }
       // String image = imageToString(imageBitmap);
        String repair_remark = Repair_two_Remark.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Global.Repair_UploadImage;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            JSONObject resp;
            try {
                resp = new JSONObject(response);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                if (resp.getBoolean("success")) {
                    Global.customtoast(RepairTwoUploadActivity.this, getLayoutInflater(), "Image uploaded successfully");
                    startActivity(new Intent(RepairTwoUploadActivity.this, RepairTwoImageListActivity.class));
                    finish();
                } else {
                    if (resp.has("error")) {
                        //Toast.makeText(RepairTwo_Upload_Activity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        Toast.makeText(RepairTwoUploadActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RepairTwoUploadActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(RepairTwoUploadActivity.this, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(RepairTwoUploadActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(RepairTwoUploadActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(RepairTwoUploadActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String image = imageToString(imageBitmap);
                params.put("fileName", image);
                params.put("image_remarks", repair_remark);
                params.put("com_code", Global.sharedPreferences.getString("com_code", "0"));
                params.put("repair2_code", Global.repairModel2.getD_Repairedtwo());
                System.out.println(params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0),0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }



    private String imageToString(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    private void openCamera() {
        try {
            // Use image picker library
            com.github.dhaval2404.imagepicker.ImagePicker.with(RepairTwoUploadActivity.this)
                    .crop()
                    .compress(800)
                    .maxResultSize(1080, 1080)
                    .start(10);
        } catch (Exception e) {
            // If image picker library is not available, try launching the camera
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } else {
                // Handle the case where both image picker library and camera are not available
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                // Handle image picked from gallery
                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        // Do something with the selected imageBitmap
                        RtwoImage.setImageBitmap(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                // Handle image captured from camera
                if (data != null && data.getExtras() != null) {
                    Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                    // Do something with the captured cameraBitmap
                    RtwoImage.setImageBitmap(cameraBitmap);
                }
            }
        }
    }


}