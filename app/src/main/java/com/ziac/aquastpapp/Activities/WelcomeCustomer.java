package com.ziac.aquastpapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class WelcomeCustomer extends AppCompatActivity {
    TextView Oname, Ownermail, Owanarmobile,Company, ClickHere;
    CircleImageView ImageView;
    ImageView Ownerexit;
    AppCompatButton oContinue;

    Context context;
    FloatingActionButton fab;
    Bitmap imageBitmap;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_customer);
        context = this;
        ImageView = findViewById(R.id.imageView);
        Oname = findViewById(R.id.wname);
        oContinue = findViewById(R.id.oContinue);
        Ownerexit = findViewById(R.id.ownerexit);
        Ownerexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeCustomer.this,LoginSignupActivity.class);
                startActivity(intent);
            }
        });
        Company = findViewById(R.id.company);
        Ownermail = findViewById(R.id.wemail);
        Owanarmobile = findViewById(R.id.wph);

        String username = Global.sharedPreferences.getString("person_nameu", "");
        String mail = Global.sharedPreferences.getString("user_email", "");
        String mobile = Global.sharedPreferences.getString("user_mobile", "");
        String com_name = Global.sharedPreferences.getString("com_name", "");
        String userimage = Global.userImageurl + Global.sharedPreferences.getString("user_image", "");


        Oname.setText(username);
        Ownermail.setText(mail);
        Owanarmobile.setText(mobile);
        Company.setText(com_name);

        Picasso.Builder builder = new Picasso.Builder(getApplication());
        Picasso picasso = builder.build();
        picasso.load(Uri.parse(userimage))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.no_image_available_icon)
                .into(ImageView);

        oContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Global.StpList.isEmpty()) {
                    startActivity(new Intent(WelcomeCustomer.this, GenerateSTPdetails.class));
                } else {
                    startActivity(new Intent(WelcomeCustomer.this, SelectSTPLocationActivity.class));
                }

            }
        });
    }

    public void showImage(@NonNull Picasso picasso, String userimage) {
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

        ImagePicker.with(WelcomeCustomer.this)
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

        if (imageBitmap == null) {
            return;
        }

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
                    Global.customtoast(WelcomeCustomer.this, getLayoutInflater(), "Image uploaded successfully");
                    getuserdetails();

                } else {
                    if (resp.has("error")) {

                        String errorMessage = resp.getString("error");
                        Toast.makeText(WelcomeCustomer.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Toast.makeText(WelcomeCustomer.this, "Image upload failed", Toast.LENGTH_SHORT).show();


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
                params.put("username", Global.sharedPreferences.getString("username", null));
                // params.put("fileName",image);
                // Log.d("YourTag", "Key: fileName, Value: " + image);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void getuserdetails() {

        String url = Global.getuserprofileurl;
        RequestQueue queue = Volley.newRequestQueue(WelcomeCustomer.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {

            try {
                JSONObject respObj1 = new JSONObject(response);
                JSONObject respObj = new JSONObject(respObj1.getString("data"));

                String user_image = respObj.getString("user_image");
                Log.d("MyTag", "Profile image: " + user_image);

                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("user_image", user_image);
                Global.editor.commit();
                String userimage = Global.userImageurl + Global.sharedPreferences.getString("user_image", "");
                Picasso.get().load(userimage).into(ImageView);

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

                params.put("username", Global.sharedPreferences.getString("username", null));
                return params;
            }
        };
        queue.add(request);
    }

    private String imageToString(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {

    }
}