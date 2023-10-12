package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {

    private TextInputEditText Newpwd, Confirmpwd,Dusrname,Dusrmobile;
    boolean passwordvisible;
    private Button Updatepwd;
    ProgressBar progressBar;
    String username,Newpassword,confirmpassword;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        Updatepwd = findViewById(R.id.updatepwd);
        Newpwd = findViewById(R.id.npassword);
        Confirmpwd = findViewById(R.id.cpassword);

        Newpwd.setOnTouchListener((v, event) -> {

            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Newpwd.getRight() - Newpwd.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Newpwd.getSelectionEnd();
                    if (passwordvisible) {
                        Newpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        Newpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordvisible = false;

                    } else {
                        Newpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        Newpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordvisible = true;


                    }
                    Newpwd.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        Confirmpwd.setOnTouchListener((v, event) -> {

            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Confirmpwd.getRight() - Confirmpwd.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Confirmpwd.getSelectionEnd();
                    if (passwordvisible) {
                        Confirmpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        Confirmpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordvisible = false;

                    } else {
                        Confirmpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        Confirmpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordvisible = true;


                    }
                    Confirmpwd.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        Updatepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Newpassword = Newpwd.getText().toString();
                confirmpassword = Confirmpwd.getText().toString();

                if (Newpassword.length() < 6 ){
                    Global.customtoast(ResetPassword.this, getLayoutInflater(), "Password should not be less than 6 digits !!");
                    return;
                }

                if (confirmpassword.length() < 6 ){
                    Global.customtoast(ResetPassword.this, getLayoutInflater(), "Password should not be less than 6 digits !!");
                    return;
                }


                if(Newpassword.equals(confirmpassword)){
                    postDataUsingVolley();
                }else
                {
                    Global.customtoast(ResetPassword.this, getLayoutInflater(), "Passwords doesn't match !!");

                }

            }
        });
    }

    private void postDataUsingVolley() {

        RequestQueue queue= Volley.newRequestQueue(ResetPassword.this);
        String ResetUrl = Global.resettpasswordurl;

        StringRequest request = new StringRequest(Request.Method.POST,ResetUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();
                try {

                    JSONObject respObj = new JSONObject(response);

                    // below are the strings which we
                    // extract from our json object.
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");
                    //   String token_type = respObj.getString("token_type");
                    // String expires_in = respObj.getString("expires_in");

                    //Toast.makeText(ResetPassword.this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                    if (issuccess.equals("true")) {
                        startActivity(new Intent(ResetPassword.this, MainActivity.class));
                    }


                    // on below line we are setting this string s to our text view.
//                    responseTV.setText("token : " + access_token);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressBar.setVisibility(View.GONE);
                    Global.customtoast(ResetPassword.this, getLayoutInflater(), e.getMessage());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    Global.customtoast(ResetPassword.this, getLayoutInflater(),"Request Time-Out");
                } else if (error instanceof ServerError) {
                    Global.customtoast(ResetPassword.this, getLayoutInflater(),"ServerError");
                }  else if (error instanceof ParseError) {
                    Global.customtoast(ResetPassword.this, getLayoutInflater(),"Parse Error ");
                }  else if (error instanceof AuthFailureError) {
                    Global.customtoast(ResetPassword.this, getLayoutInflater(), "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("MyApp", "ServerError: " + error.getMessage());
                    Global.customtoast(ResetPassword.this, getLayoutInflater(), "ServerError");
                } else if (error instanceof ParseError) {
                    Log.e("MyApp", "ParseError: " + error.getMessage());
                    Global.customtoast(ResetPassword.this, getLayoutInflater(), "Parse Error");
                } else if (error instanceof AuthFailureError) {
                    Log.e("MyApp", "AuthFailureError: " + error.getMessage());
                    Global.customtoast(ResetPassword.this, getLayoutInflater(), "AuthFailureError");
                }
                else {
                    // Log.e("MyApp", "Something else: " + error.getMessage());
                    // Global.customtoast(requireActivity(), getLayoutInflater(), "Something else");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
               // params.put("UserName", username);
                params.put("NewPassword", Newpassword);

                return params;
            }
        };
        queue.add(request);
    }
}