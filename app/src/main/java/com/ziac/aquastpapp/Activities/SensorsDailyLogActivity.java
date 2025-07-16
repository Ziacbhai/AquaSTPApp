package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.button.MaterialButton;
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

import Models.SensorsModelClass;

public class SensorsDailyLogActivity extends AppCompatActivity {
    ImageView backbtn;
    TextView Displaydate, Displaytime, Total_sensor_header;
    SensorsModelClass sensorsModelClass;
    LinearLayout Sensor_header;
    RecyclerView sensor_recyclerView;
    RecyclerView sensor_recyclerView2;
    Context context;
    View viewhide;
    Bitmap imageBitmap;
    SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        context = this;
        user_topcard();
        backbtn = findViewById(R.id.back_btn);
        viewhide = findViewById(R.id.viewhide3);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        Displaydate = findViewById(R.id.displaydate);
        Displaytime = findViewById(R.id.displaytime);
        Sensor_header = findViewById(R.id.sensor_header);
        sensor_recyclerView = findViewById(R.id.sensor_edit_recyclerview);
        sensor_recyclerView2 = findViewById(R.id.sensors_recyclerview);
        Total_sensor_header = findViewById(R.id.total_sensor_header);

        String usertype = Global.sharedPreferences.getString("user_type", "");
        if (usertype.equals("C")) {
            hideViews();
        } else {
            showViews();
        }

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                handler.postDelayed(this, 1000); // Update every 1000 milliseconds (1 second)
            }
        }, 0);

        DailyLogSensorsEdit();
        DailyLogSensors();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            DailyLogSensorsEdit(); // or DailyLogSensors() depending on which one you want to refresh
            DailyLogSensors();
        });

        if (sensor_recyclerView != null) {
            sensor_recyclerView.setLayoutManager(new LinearLayoutManager(this));
            sensor_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }

        if (sensor_recyclerView2 != null) {
            sensor_recyclerView2.setLayoutManager(new LinearLayoutManager(this));
            sensor_recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }

    private void showViews() {
        if (Sensor_header != null) {
            Sensor_header.setVisibility(View.VISIBLE);

        }
        if (sensor_recyclerView != null) {
            sensor_recyclerView.setVisibility(View.VISIBLE);

        }
    }

    private void hideViews() {
        if (Sensor_header != null) {
            Sensor_header.setVisibility(View.GONE);


        }
        if (sensor_recyclerView != null) {
            sensor_recyclerView.setVisibility(View.GONE);


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


    private void DailyLogSensorsEdit() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String dailylogsensors = Global.GetDailyLogSensors;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String dlog_date = Global.sharedPreferences.getString("dlogdate", "0");

        dailylogsensors = dailylogsensors + "comcode=" + com_code + "&sstp1_code=" + sstp1_code + "&dlog_date=" + dlog_date;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, dailylogsensors, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Sensors_Class = new ArrayList<SensorsModelClass>();
                sensorsModelClass = new SensorsModelClass();
                JSONArray jarray;
                try {
                    jarray = response.getJSONArray("sensors1");
                    for (int i = 0; i < jarray.length(); i++) {
                        final JSONObject e;
                        try {
                            e = jarray.getJSONObject(i);
                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        sensorsModelClass = new SensorsModelClass();
                        try {
                            sensorsModelClass.setEquip_name(e.getString("equip_name"));
                            sensorsModelClass.setSensor_tstp6_code(e.getString("tstp6_code"));
                            sensorsModelClass.setSensor_status(e.getString("status"));

                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        Global.Sensors_Class.add(sensorsModelClass);
                        SensorDailyLogEditAdapter sensorDailyLogEditAdapter = new SensorDailyLogEditAdapter((List<SensorsModelClass>) Global.Sensors_Class, context);
                        sensor_recyclerView.setAdapter(sensorDailyLogEditAdapter);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                // Dismiss the refresh indicator
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
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

                // Dismiss the refresh indicator on error too
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                // Set the Authorization header with the access token
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", "");
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }

    private void DailyLogSensors() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String dailylogsensors = Global.GetDailyLogSensors;

        String com_code = Global.sharedPreferences.getString("com_code", "0");
        String sstp1_code = Global.sharedPreferences.getString("sstp1_code", "0");
        String dlog_date = Global.sharedPreferences.getString("dlogdate", "0");

        dailylogsensors = dailylogsensors + "comcode=" + com_code + "&sstp1_code=" + sstp1_code + "&dlog_date=" + dlog_date;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, dailylogsensors, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Global.Sensors_Class = new ArrayList<SensorsModelClass>();
                sensorsModelClass = new SensorsModelClass();
                JSONArray jarray;
                try {
                    jarray = response.getJSONArray("sensors2");
                    for (int i = 0; i < jarray.length(); i++) {
                        final JSONObject e;
                        try {
                            e = jarray.getJSONObject(i);
                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        sensorsModelClass = new SensorsModelClass();
                        try {
                            sensorsModelClass.setEquip_name(e.getString("equip_name"));
                            sensorsModelClass.setReading(e.getString("reading_value"));
                            sensorsModelClass.setReading_time(e.getString("readingtime"));
                            sensorsModelClass.setSensor_total(e.getString("final_value"));
                            sensorsModelClass.setSensor_image(e.getString("image_path"));
                            sensorsModelClass.setSensor_tstp6_code(e.getString("tstp6_code"));

                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                        Global.Sensors_Class.add(sensorsModelClass);
                        SensorDailyLogAdapter sensorDailyLogAdapter = new SensorDailyLogAdapter((List<SensorsModelClass>) Global.Sensors_Class, context);
                        sensor_recyclerView2.setAdapter(sensorDailyLogAdapter);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                // Dismiss the refresh indicator
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Dismiss the refresh indicator on error
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }) {
            @Override
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

        queue.add(jsonObjectRequest);
    }


    public class SensorDailyLogEditAdapter extends RecyclerView.Adapter<SensorDailyLogEditAdapter.Viewholder> {

        List<SensorsModelClass> sensorsModelClasses;
        Context context;
        String enteredValue;

        public SensorDailyLogEditAdapter(List<SensorsModelClass> sensorsModelClasses, Context context) {
            this.sensorsModelClasses = sensorsModelClasses;
            this.context = context;
        }

        @NonNull
        @Override
        public SensorDailyLogEditAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensors_daily_log_edit, parent, false);
            return new SensorDailyLogEditAdapter.Viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorDailyLogEditAdapter.Viewholder holder, int position) {

            holder.Sensor_equip_name.setText(sensorsModelClasses.get(position).getEquip_name());
            holder.Sensor_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enteredValue = holder.Sensor_reading_edit.getText().toString();
                    if (enteredValue.isEmpty()) {
                        Toast.makeText(context, "Please enter the reading value", Toast.LENGTH_LONG).show();
                    } else {
                        DailyLogSensors(enteredValue, position);
                    }
                }
            });

        }

        private void DailyLogSensors(String enteredValue, int position) {
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Global.DailyLogUpdateSensorsReadings;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        String error = jsonObject.getString("error");

                        if (success) {
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, SensorsDailyLogActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).finish();
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
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    String accesstoken = Global.sharedPreferences.getString("access_token", "");
                    headers.put("Authorization", "Bearer " + accesstoken);
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("com_code", Global.sharedPreferences.getString("com_code", null));
                    params.put("ayear", Global.sharedPreferences.getString("ayear", null));
                    params.put("tstp6_code", sensorsModelClasses.get(position).getSensor_tstp6_code());
                    params.put("reading_value", enteredValue);
                    System.out.println(params);
                    return params;
                }

            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(stringRequest);

        }

        @Override
        public int getItemCount() {
            return sensorsModelClasses.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {

            TextView Sensor_equip_name;
            MaterialButton Sensor_save;
            EditText Sensor_reading_edit;

            public Viewholder(@NonNull View itemView) {
                super(itemView);

                Sensor_equip_name = itemView.findViewById(R.id.sensor_name);
                Sensor_reading_edit = itemView.findViewById(R.id.sensor_reading_edit);
                Sensor_save = itemView.findViewById(R.id.sensor_save);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public class SensorDailyLogAdapter extends RecyclerView.Adapter<SensorDailyLogAdapter.Viewholder> {


        private List<SensorsModelClass> sensorsModelClassList;
        Context context;

        public SensorDailyLogAdapter(List<SensorsModelClass> sensorsModelClassList, Context context) {
            this.sensorsModelClassList = sensorsModelClassList;
            this.context = context;
        }

        @NonNull
        @Override
        public SensorDailyLogAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensors_daily_log_deatils, parent, false);
            return new SensorDailyLogAdapter.Viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorDailyLogAdapter.Viewholder holder, int position) {
            holder.Sensor_equip_name.setText(sensorsModelClassList.get(position).getEquip_name());
            holder.Sensor_reading.setText(sensorsModelClassList.get(position).getReading());
            holder.Sensor_reading_time.setText(sensorsModelClassList.get(position).getReading_time());
            holder.Sensor_total.setText(sensorsModelClassList.get(position).getSensor_total());

            Picasso.Builder builder = new Picasso.Builder(context);
            Picasso picasso = builder.build();
            String originalImageUrl = sensorsModelClassList.get(position).getSensor_image();
            String trimmedImageUrl = originalImageUrl.replace('~', ' ').trim();
            picasso.load(Uri.parse(Global.baseurl + trimmedImageUrl))
                    .error(R.drawable.no_image_available_icon)
                    .into(holder.Sensor_image);

            double rawValue = Double.parseDouble(sensorsModelClassList.get(position).getReading());
            String formattedValue = removeTrailingZeros(rawValue);
            holder.Sensor_reading.setText(formattedValue);


            holder.Sensor_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImage(sensorsModelClassList.get(position).getSensor_image());
                }
            });

            holder.Sensor_image_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.sensorclass = sensorsModelClassList.get(position);

                    openCamera();

                    /*Intent intent = new Intent(context, SensorsDailyLogImageUploadActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();*/
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

        private String removeTrailingZeros(double value) {
            String stringValue = String.valueOf(value);
            stringValue = stringValue.replaceAll("0*$", "").replaceAll("\\.$", "");

            return stringValue;
        }

        private void showImage(String sensorImage) {
            Dialog builder = new Dialog(context);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // Calculate display dimensions
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;
            String trimmedImageUrl = sensorImage.replace('~', ' ').trim();
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
            return sensorsModelClassList.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {

            ImageView Sensor_image, Sensor_image_upload;

            TextView Sensor_equip_name, Sensor_reading, Sensor_reading_time, Sensor_total;

            View viewhide2, viewhide;

            public Viewholder(@NonNull View itemView) {
                super(itemView);

                Sensor_equip_name = itemView.findViewById(R.id.sensor_name);
                Sensor_reading = itemView.findViewById(R.id.sensor_reading);
                Sensor_reading_time = itemView.findViewById(R.id.sensor_reading_time);
                Sensor_total = itemView.findViewById(R.id.sensor_total);
                Sensor_image = itemView.findViewById(R.id.sensor_image);
                Sensor_image_upload = itemView.findViewById(R.id.sensor_image_upload_btn);
                //   viewhide = itemView.findViewById(R.id.viewhide);
                // viewhide2 = itemView.findViewById(R.id.viewhide2);

                String usertype = Global.sharedPreferences.getString("user_type", "");
                if (usertype.equals("C")) {
                    Sensor_image_upload.setVisibility(View.GONE);
                    //   viewhide.setVisibility(View.GONE);
                } else {
                    Sensor_image_upload.setVisibility(View.VISIBLE);
                    Sensor_equip_name.setVisibility(View.VISIBLE);
                    Sensor_reading_time.setVisibility(View.VISIBLE);
                    Sensor_reading.setVisibility(View.VISIBLE);
                    // viewhide.setVisibility(View.VISIBLE);

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
            Sensor_image_postselelectedimage();
        }
    }

    private void Sensor_image_postselelectedimage() {

        if (imageBitmap == null) {
            return;
        }
        String url = Global.DailyLogSensorsImageUpload;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String sresponse) {
                try {
                    JSONObject jsonObject = new JSONObject(sresponse);
                    boolean success = jsonObject.getBoolean("success");
                    String error = jsonObject.getString("error");

                    if (success) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(context, SensorsDailyLogActivity.class));
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
                params.put("ayear", Global.sharedPreferences.getString("ayear", ""));
                params.put("tstp6_code", Global.sensorclass.getSensor_tstp6_code());
                params.put("com_code", Global.sharedPreferences.getString("com_code", ""));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), 0,
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