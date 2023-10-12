package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordUserName extends AppCompatActivity {

   // EditText Username ;
    EditText etusername;
    AppCompatButton Getotp1;
    ProgressBar progressBar;
    String username1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_user_name);

        Getotp1 = findViewById(R.id.submitotp);
        etusername = findViewById(R.id.fgusername);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String usernames = sharedPreferences.getString("username", "");

        etusername.setText(usernames);

        Getotp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username1 = etusername.getText().toString();
               if (usernames.isEmpty()) {
                   etusername.setError("Please Enter User Name");
                   etusername.requestFocus();
                    return;
                }
                postDataUsingVolley();
            }
        });
    }

    private void postDataUsingVolley() {

        String urlUsername = Global.forgotpasswordurl;
        //progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue= Volley.newRequestQueue(ResetPasswordUserName.this);
        StringRequest request = new StringRequest(Request.Method.POST, urlUsername, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);

                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");

                    Global.editor = sharedPreferences.edit();
                    Global.editor.putString("username", username1);
                    Global.editor.commit();

                    if (issuccess.equals("true")) {
                        startActivity(new Intent(ResetPasswordUserName.this, VerifyUserNameOTP.class));
                    } else {
                        // Show a toast message for wrong username or password
                        Global.customtoast(ResetPasswordUserName.this, getLayoutInflater(),"Provided username is invalid ");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressBar.setVisibility(View.GONE);
                    // Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", username1);
                return params;
            }
        };
        queue.add(request);


    }
}