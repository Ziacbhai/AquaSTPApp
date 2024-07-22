package com.ziac.aquastpapp.Activities;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yalantis.ucrop.UCrop;
import com.ziac.aquastpapp.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import de.hdodenhof.circleimageview.CircleImageView;
public class ProfileActivity extends AppCompatActivity {
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 0;
    private static final int CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_IMAGE_SELECT = 3;
    FloatingActionButton fab;
    CircleImageView circleImageView;
    Bitmap imageBitmap;
    String personname, email, mobile,userimage;
    Picasso.Builder builder;
    Picasso picasso;
    EditText Person_name, Person_number, Person_email;
    TextView Person_ref_code,Updatebutton;
    ImageView Backarrowbtn;
    Context context;
    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;

        fab = findViewById(R.id.floating);
        Backarrowbtn = findViewById(R.id.backarrowbtn);
        Updatebutton = findViewById(R.id.updatebutton);

        Person_name = findViewById(R.id.profilename);
        Person_number = findViewById(R.id.uNumber);
        Person_email = findViewById(R.id.uEmail);
        Person_ref_code = findViewById(R.id.Uref);
        circleImageView = findViewById(R.id.imageVie);

        Updatebutton.setOnClickListener(v -> updateprofile());
        fab.setOnClickListener(v -> {
            showDialog();
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userimage = Global.userImageurl + Global.sharedPreferences.getString("user_image", "");
                showImage(picasso, userimage);
                if (!TextUtils.isEmpty(userimage)) {
                    showImage(picasso, userimage);
                } else {
                    circleImageView.setImageResource(R.drawable.no_image_available_icon); // Replace R.drawable.blank_image with the resource ID of your blank image
                }
            }
        });
        Backarrowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getuserdetails();
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);


        ImageView Captureimage,SelectImages;
        Captureimage = dialog.findViewById(R.id.captureimage);
        SelectImages= dialog.findViewById(R.id.selectimagefromgallery);

        Captureimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraAndStoragePermissions();
                dialog.dismiss();
            }
        });

        SelectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                dialog.dismiss();
            }
        });



        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    private void requestCameraAndStoragePermissions() {
        if (  ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED  ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions already granted, proceed with taking a picture
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with taking a picture
                dispatchTakePictureIntent();
            } else {
                // Permission denied, show a message to the user or handle it accordingly
                Toast.makeText(this, "Permission denied to write to external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    startCrop(getImageUri(this, imageBitmap));
                }
            }
        } else if (requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                Log.d("SelectedImageUri", selectedImage.toString());
                startCrop(selectedImage);
            } else {
                Log.e("SelectedImageUri", "Selected image URI is null");
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri croppedUri = UCrop.getOutput(data);
            if (croppedUri != null) {
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedUri);
                    postselelectedimage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            cropError.printStackTrace();
        }
    }


    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
    private void startCrop(Uri sourceUri) {
        String destinationFileName = "cropped_image";
        UCrop uCrop = UCrop.of(sourceUri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1); // Set cropping aspect ratio as square
        uCrop.withMaxResultSize(1000, 1000); // Set max cropping size
        uCrop.start(this);
    }
    private String imageToString(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    public void showImage(Picasso picasso, String userimage) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // Nothing
            }
        });

        // Calculate display dimensions
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        picasso.load(Uri.parse(userimage)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ImageView imageView = new ImageView(getApplicationContext());
                // Calculate dimensions to fit the image within the screen
                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();
                float aspectRatio = (float) imageWidth / imageHeight;

                int newWidth = screenWidth;
                int newHeight = (int) (screenWidth / aspectRatio);
                if (newHeight > screenHeight) {
                    newHeight = screenHeight;
                    newWidth = (int) (screenHeight * aspectRatio);
                }

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
                imageView.setLayoutParams(layoutParams);

                imageView.setImageBitmap(bitmap);

                builder.addContentView(imageView, layoutParams);
                builder.show();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                // Handle bitmap loading failure
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Prepare bitmap loading
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
    }


    private void postselelectedimage() {

        if (imageBitmap == null) {
            return;
        }
        String image = imageToString(imageBitmap);
        String url = Global.urlUpdateprofileImage;
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
                    Global.customtoast(ProfileActivity.this, getLayoutInflater(), "Image uploaded successfully");
                    getuserdetails();

                } else {
                    if (resp.has("error")) {
                        String errorMessage = resp.getString("error");
                        Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Toast.makeText(ProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
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
                params.put("fileName", image);
                //Log.d("YourTag", "Key: fileName, Value: " + image);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void updateprofile() {

        personname = Person_name.getText().toString();
        mobile = Person_number.getText().toString();
        email = Person_email.getText().toString();

        if (personname.isEmpty() || mobile.isEmpty() || email.isEmpty()) {
            Toast.makeText(ProfileActivity.this, "Complete the information and try again !!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobile.length() < 10) {
            Toast.makeText(ProfileActivity.this, "Mobile number should not be less than 10 digits !!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Person_email.requestFocus();
            Toast.makeText(context, "Please Enter a Valid Email Address !!", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.updateProfile, new Response.Listener<String>() {
            @Override
            public void onResponse(String sresponse) {
                JSONObject response;
                try {
                    response = new JSONObject(sresponse);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("user_name", Person_name.getText().toString());
                Global.editor.putString("user_mobile", Person_number.getText().toString());
                Global.editor.putString("user_email", Person_email.getText().toString());
                Global.editor.putString("ref_code", Person_ref_code.getText().toString());
                Global.editor.commit();

                try {
                    if (response.getBoolean("isSuccess")) {
                        Toast.makeText(ProfileActivity.this, "Updated successfully !!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        finish();
                    } else {
                        //textViewError.setText(response.getString("error"));
                        Toast.makeText(ProfileActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                        //textViewError.setVisibility(View.VISIBLE);
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
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("com_code", Global.sharedPreferences.getString("com_code", "0"));
                params.put("person_name",personname );
                params.put("user_mobile",mobile );
                params.put("user_email", email);
                return params;
                //  String user_image = respObj.getString("user_image");
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    private void getuserdetails() {
        String url = Global.getuserprofileurl;
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject respObj1 = new JSONObject(response);
                JSONObject respObj = new JSONObject(respObj1.getString("data"));
                userimage = Global.userImageurl + Global.sharedPreferences.getString("user_image", "");
                String person_name = respObj.getString("person_name");
                String user_image = respObj.getString("user_image");
                String mobile = respObj.getString("user_mobile");
                String mail = respObj.getString("user_email");
                String refcode = respObj.getString("ref_code");
                //  Log.d("MyTag", "Profile image: " + user_image);*/
                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("user_image", user_image);
                Global.editor.putString("ref_code", refcode);
                Global.editor.putString("person_name", person_name);
                Global.editor.putString("user_email", mail);
                Global.editor.putString("user_mobile", mobile);
                Global.editor.commit();
                userimage = Global.userImageurl + Global.sharedPreferences.getString("user_image", "");

                Person_name.setText(person_name);
                Person_email.setText(mail);
                Person_number.setText(mobile);
                Person_ref_code.setText(refcode);


                builder = new Picasso.Builder(getApplication());
                picasso = builder.build();
                picasso.load(Uri.parse(userimage))
                        .error(R.drawable.no_image_available_icon)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(circleImageView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new com.android.volley.Response.ErrorListener() {
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

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_name", Global.sharedPreferences.getString("user_name", null));
                return params;
            }
        };
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
       startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        super.onBackPressed();
    }
}