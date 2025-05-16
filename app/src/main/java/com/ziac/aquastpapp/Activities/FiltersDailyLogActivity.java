package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ziac.aquastpapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Models.FiltersModel;

public class FiltersDailyLogActivity extends AppCompatActivity {
    ImageView backbtn;
    TextView Displaydate, Displaytime, hidefilter1;
    RecyclerView Filters_recyclerview;
    FiltersModel filtersModel;
    View hidefilter;
    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY = 200;
    private Uri imageUri;
    Bitmap imageBitmap;

    Context context;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        context = this;
        user_topcard();
        backbtn = findViewById(R.id.back_btn);
        Displaydate = findViewById(R.id.displaydate);
        Displaytime = findViewById(R.id.displaytime);
        hidefilter = findViewById(R.id.hidefilterview);
        hidefilter1 = findViewById(R.id.hidefilter1);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        String usertype = Global.sharedPreferences.getString("user_type", "");
        if (usertype.equals("C")) {
            hideViews();
        } else {
            showViews();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                handler.postDelayed(this, 1000); // Update every 1000 milliseconds (1 second)
            }
        }, 0);

        FiltersDailyLog();
        Filters_recyclerview = findViewById(R.id.filter_recyclerview);
        Filters_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        Filters_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
    private void showViews() {
        if (hidefilter != null) {
            hidefilter.setVisibility(View.VISIBLE);
            hidefilter1.setVisibility(View.VISIBLE);
        }
    }
    private void hideViews() {
        if (hidefilter != null) {
            hidefilter1.setVisibility(View.GONE);
            hidefilter.setVisibility(View.GONE);
        }
    }
    private void updateDateTime() {
        Date currentDate = new Date();
        // Update date
        SimpleDateFormat dateFormat = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        }
        String formattedDate = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && dateFormat != null) {
            formattedDate = dateFormat.format(currentDate);
        }
        Displaydate.setText(formattedDate);

        SimpleDateFormat timeFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String formattedTime = timeFormat.format(currentDate);
            formattedTime = formattedTime.replace("am", "AM").replace("pm", "PM");

            Displaytime.setText(formattedTime);
        }

    }
    private void user_topcard() {
        String personname, useremail, stpname, sitename, siteaddress, processname, usermobile, stpcapacity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        siteaddress = sharedPreferences.getString("site_address", "");
        processname = sharedPreferences.getString("process_name", "");
        useremail = sharedPreferences.getString("user_email", "");
        usermobile = sharedPreferences.getString("user_mobile", "");
        personname = sharedPreferences.getString("user_name", "");
        stpcapacity = sharedPreferences.getString("stp_capacity", "");

        TextView txtsitename, txtstpname, txtsiteaddress, txtuseremail, txtusermobile, txtpersonname;

        txtsitename = findViewById(R.id.sitename);
        txtstpname = findViewById(R.id.stpname);
       // txtsiteaddress = findViewById(R.id.siteaddress);
        txtuseremail = findViewById(R.id.useremail);
        txtusermobile = findViewById(R.id.usermobile);
        txtpersonname = findViewById(R.id.personname);

        txtsitename.setText(sitename);
        txtstpname.setText(stpname + " " + processname + " " + stpcapacity);
        //txtsiteaddress.setText(siteaddress);
        txtuseremail.setText(useremail);
        txtusermobile.setText(usermobile);
        txtpersonname.setText(personname);
    }
    private void FiltersDailyLog() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String dailylogfilter = Global.GetDailyLogFilters;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String dlog_date = Global.sharedPreferences.getString("dlogdate", "0");

        dailylogfilter = dailylogfilter + "comcode=" + com_code + "&sstp1_code=" + sstp1_code + "&dlog_date=" + dlog_date;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, dailylogfilter, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Filter_LogClass = new ArrayList<FiltersModel>();
                filtersModel = new FiltersModel();
                JSONArray jarray;

                try {
                    jarray = response.getJSONArray("filters");
                    for (int i = 0; i < jarray.length(); i++) {
                        final JSONObject e;
                        try {
                            e = jarray.getJSONObject(i);
                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        filtersModel = new FiltersModel();
                        try {
                            filtersModel.setEquip_name(e.getString("equip_name"));
                            filtersModel.setReading_time(e.getString("readingtime"));
                            filtersModel.setFilter_image(e.getString("image_path"));
                            filtersModel.setFilter_status(e.getString("status"));
                            filtersModel.setTstp4_code(e.getString("tstp4_code"));


                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        Global.Filter_LogClass.add(filtersModel);
                        FiltersDailyLogAdapter filtersDailyLogAdapter = new FiltersDailyLogAdapter(context, (List<FiltersModel>) Global.Filter_LogClass);
                        Filters_recyclerview.setAdapter(filtersDailyLogAdapter);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time-Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "No Connection Found", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error", Toast.LENGTH_LONG).show();
                }

            }
        }) {
            public Map<String, String> getHeaders() {
                // Set the Authorization header with the access token
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // If you have any parameters to send in the request body, you can set them here
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }
    @Override
    protected void onPause() {
        FiltersDailyLog();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }




    public class FiltersDailyLogAdapter extends RecyclerView.Adapter<FiltersDailyLogAdapter.Viewholder> {

        Context context;
        private List<FiltersModel> filtersModelList;

        public FiltersDailyLogAdapter(Context context, List<FiltersModel> filtersModelList) {
            this.context = context;
            this.filtersModelList = filtersModelList;
        }

        @NonNull
        @Override
        public FiltersDailyLogAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_daily_log_details, parent, false);
            return new FiltersDailyLogAdapter.Viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FiltersDailyLogAdapter.Viewholder holder, int position) {
            holder.Filter_equip_name.setText(filtersModelList.get(position).getEquip_name());
            holder.Filter_reading.setText(filtersModelList.get(position).getReading_time());

            Picasso.Builder builder = new Picasso.Builder(context);
            Picasso picasso = builder.build();
            String originalImageUrl = filtersModelList.get(position).getFilter_image();
            String trimmedImageUrl = originalImageUrl.replace('~', ' ').trim();
            picasso.load(Uri.parse(Global.baseurl + trimmedImageUrl))
                    .error(R.drawable.no_image_available_icon)
                    .into(holder.Filter_image);
            holder.Filter_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showImage(filtersModelList.get(position).getFilter_image());
                }
            });


            if (filtersModelList.size() > position && filtersModelList.get(position).getFilter_status().equals("S")) {
                //holder.Filter_image_upload.setVisibility(View.GONE);
            } else {
                holder.Filter_image_upload.setVisibility(View.VISIBLE);
            }


            /*holder.Filter_image_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.filtersModel = filtersModelList.get(position);
                    Intent intent = new Intent(context, FiltersDailyLogImageUploadActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            });*/


            holder.Filter_image_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.filtersModel = filtersModelList.get(position);
                    openCamera();
                }
            });

        }


        private void openCamera() {
            try {
                if (context instanceof Activity) {
                    com.github.dhaval2404.imagepicker.ImagePicker.with((Activity) context)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start(10);
                } else {
                    // Log.e("Camera", "Context is not an instance of Activity");
                    // Handle the case where context is not an instance of Activity.
                }
            } catch (Exception e) {
                e.printStackTrace();
                //Log.e("Camera", "Error opening camera: " + e.getMessage());
                // Handle the exception as needed.
            }
        }

        private void showImage(String filterImage) {
            Dialog builder = new Dialog(context);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // Calculate display dimensions
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;
            String trimmedImageUrl = filterImage.replace('~', ' ').trim();
            Picasso.get().load(Uri.parse(Global.baseurl + trimmedImageUrl)).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    ImageView imageView = new ImageView(context);
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

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

        }


        @Override
        public int getItemCount() {
            return filtersModelList.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {

            TextView Filter_equip_name, Filter_reading;
            ImageView Filter_image, Filter_image_upload;
            ImageButton layout;
            Guideline filterview;

            public Viewholder(@NonNull View itemView) {
                super(itemView);
                Filter_equip_name = itemView.findViewById(R.id.filter_equip_name);
                Filter_image = itemView.findViewById(R.id.filter_image);
                Filter_reading = itemView.findViewById(R.id.filter_reading);
                Filter_image_upload = itemView.findViewById(R.id.filter_image_upload);
                layout = itemView.findViewById(R.id.filter_image_upload);
                //  filterview = itemView.findViewById(R.id.guideline3);


                String usertype = Global.sharedPreferences.getString("user_type", "");

                if (usertype.equals("C")) {
                    layout.setVisibility(View.GONE);
                    //  filterview.setVisibility(View.GONE);
                } else {
                    layout.setVisibility(View.VISIBLE);
                    //  filterview.setVisibility(View.VISIBLE);
                }

            }

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            // imageList.add(uri);
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Filter_image_postselelectedimage();
        }
    }
    private void Filter_image_postselelectedimage() {
        if (imageBitmap == null) {
            return;
        }
        String url = Global.GetDailyLogFilterImageUpload;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String sresponse) {
                try {
                    JSONObject  jsonObject = new JSONObject(sresponse);
                    boolean success = jsonObject.getBoolean("success");
                    String error = jsonObject.getString("error");

                    if (success) {
                        startActivity(new Intent(context,FiltersDailyLogActivity.class));
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
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
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String image = imageToString(imageBitmap);
                params.put("fileName", image);
                params.put("tstp4_code", Global.filtersModel.getTstp4_code());
                params.put("com_code", Global.sharedPreferences.getString("com_code", ""));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0),0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private String imageToString(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }


}