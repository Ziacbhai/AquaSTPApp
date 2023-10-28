package com.ziac.aquastpapp.Activities;

import static com.google.android.gms.cast.framework.media.ImagePicker.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    FloatingActionButton fab;
    AppCompatButton Updatebutton;
    CircleImageView circleImageView;
    Bitmap imageBitmap;
    private String username;
    TextView pName,pNum,pEmail;
    private static final int CAMERA_REQUEST = 0;

    ImageView Backarrowbtn;

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fab=findViewById(R.id.floating);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
       Backarrowbtn = findViewById(R.id.backarrowbtn);
        pName = findViewById(R.id.uName);
        pNum = findViewById(R.id.uNumber);
        pEmail = findViewById(R.id.uEmail);
        circleImageView = findViewById(R.id.imageVie);


        Updatebutton = findViewById(R.id.updatebutton);
        Updatebutton.setOnClickListener(v -> updateprofile());


        username = Global.sharedPreferences.getString("username", "");

        String dealername = Global.sharedPreferences.getString("person_name", "");
        String mail = Global.sharedPreferences.getString("user_email", "");
        String mobile = Global.sharedPreferences.getString("user_mobile", "");
        String userimage = Global.userImageurl + Global.sharedPreferences.getString("user_image", "");


        Picasso.Builder builder=new Picasso.Builder(getApplication());
        Picasso picasso=builder.build();
        picasso.load(Uri.parse(userimage)).into(circleImageView );


        fab.setOnClickListener(v -> {
            opencamera();
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userimage = Global.userImageurl + Global.sharedPreferences.getString("user_image", "");
                showImage(picasso,userimage);

            }
        });


        Backarrowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    public void showImage(Picasso picasso, String userimage) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
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

        // Load the image using Picasso
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
    }


    private void opencamera() {

        com.github.dhaval2404.imagepicker.ImagePicker.with(ProfileActivity.this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start(10);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            //imageList.add(uri);
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            postselelectedimage();
        }
    }

    private void postselelectedimage() {

        if (imageBitmap == null) {return;}

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
                   // getuserdetails();

                } else {
                    if (resp.has("error")) {

                        String errorMessage = resp.getString("error");
                        Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Toast.makeText(ProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();


                    } else {
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
                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String image = imageToString(imageBitmap);

                params.put("fileName",image);

                Log.d("YourTag", "Key: fileName, Value: " + image);

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


    private void updateprofile() {
        String personname,email,mobile;

        personname = pName.getText().toString();
        mobile = pNum.getText().toString();
        email = pEmail.getText().toString();

        if (personname.isEmpty() || mobile.isEmpty() || email.isEmpty()) {

            Toast.makeText(ProfileActivity.this, "Complete the information and try again !!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobile.length() < 10) {
            Toast.makeText(ProfileActivity.this, "Mobile number should not be less than 10 digits !!", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.updateProfile, new Response.Listener<String>() {
            @Override
            public void onResponse(String sresponse) {
                JSONObject response = null;
                try {
                    response = new JSONObject(sresponse);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("person_name", pName.getText().toString());
                Global.editor.putString("user_mobile", pNum.getText().toString());
                Global.editor.putString("user_email", pEmail.getText().toString());
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
        }){

            @Override
            public Map<String, String> getHeaders() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("com_code", Global.sharedPreferences.getString("com_code", null).toString());
                params.put("person_name", pName.getText().toString());
                params.put("user_mobile", pNum.getText().toString());
                params.put("user_email", pEmail.getText().toString());
                return params;

                //  String user_image = respObj.getString("user_image");

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(2500), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
       // Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
    }
}