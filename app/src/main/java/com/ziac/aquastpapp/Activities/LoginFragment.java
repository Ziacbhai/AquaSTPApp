package com.ziac.aquastpapp.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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


public class LoginFragment extends Fragment {

    EditText Log_email,Log_pwd ;

    Button Login_btn;
    TextView TermsOfUse, privacy, forgotpwd, Login;
    private CheckBox mcheckBox;
    boolean passwordVisible;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
//ramesh
    @Override
    public void onStart() {
        super.onStart();
    }
    private ProgressDialog progressDialog;

    String pwd,username;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Context context = getContext();
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(true);

        Log_email = view.findViewById(R.id.email2);
        Log_pwd = view.findViewById(R.id.password2);
        Login_btn = view.findViewById(R.id.loginbtn);

        mcheckBox = view.findViewById(R.id.lcheckBox);
        TermsOfUse = view.findViewById(R.id.terms);
        privacy = view.findViewById(R.id.privacy);
        forgotpwd = view.findViewById(R.id.btnftpass);


        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dologinVolley();
            }
        });

        TermsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://ziacsoft.com/terms.html")));

            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://ziacsoft.com/privacy.html")));
            }
        });

        Log_pwd.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Log_pwd.getRight() - Log_pwd.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Log_pwd.getSelectionEnd();
                    if (passwordVisible) {
                        Log_pwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        Log_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        Log_pwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        Log_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    Log_pwd.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RecoveryPasswordWith.class));
            }
        });
        return view;


    }
    private void dologinVolley() {
//        if (!isNetworkAvailable()) {
//            Global.customtoast(LoginActivity.this, getLayoutInflater(), "Internet connection lost !!");
//            return;
//        }
        username = Log_email.getText().toString();
        pwd = Log_pwd.getText().toString();
        progressDialog.show();
        //new InternetCheckTask().execute();

        String url = Global.tokenurl;
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject respObj = new JSONObject(response);
                String access_token = respObj.getString("access_token");
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("access_token", access_token);
                Global.editor.commit();
                startActivity(new Intent(getActivity(), MainActivity.class));
                //getuserdetails();

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof TimeoutError) {
                     Global.customtoast(requireActivity(), getLayoutInflater(),"Request Time-Out");
                } else if (error instanceof ServerError) {
                    Global.customtoast(getActivity(), getLayoutInflater(),"ServerError");
                }  else if (error instanceof ParseError) {
                     Global.customtoast(requireActivity(), getLayoutInflater(),"Parse Error ");
                }  else if (error instanceof AuthFailureError) {
                     Global.customtoast(requireActivity(), getLayoutInflater(), "AuthFailureError");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", pwd);
                params.put("grant_type", "password");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }
    private void getuserdetails() {

        String url = Global.getuserdetailsurl;
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        //progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {

            try {
                JSONObject respObj1 = new JSONObject(response);
                JSONObject respObj = new JSONObject(respObj1.getString("data"));

                String user_code = respObj.getString("user_code");
                String person_name = respObj.getString("person_name");
                String com_code = respObj.getString("com_code");
                String user_image = respObj.getString("user_image");
                String com_name = respObj.getString("com_name");
                String user_mobile = respObj.getString("user_mobile");
                String alternativemob = respObj.getString("user_mobile1");
                String user_email = respObj.getString("user_email");
                String state_code = respObj.getString("state_code");
                String city_code = respObj.getString("city_code");
                String city_name = respObj.getString("city_name");
                String state_name = respObj.getString("state_name");
                String ref_code = respObj.getString("ref_code");

               // Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("user_code", user_code);
                Global.editor.putString("person_name", person_name);
                Global.editor.putString("com_code", com_code);
                Global.editor.putString("user_image", user_image);
                Global.editor.putString("com_name", com_name);
                Global.editor.putString("user_mobile", user_mobile);
                Global.editor.putString("user_mobile1", alternativemob);
                Global.editor.putString("user_email", user_email);
                Global.editor.putString("statecode", state_code);
                Global.editor.putString("citycode", city_code);
                Global.editor.putString("statename", state_name);
                Global.editor.putString("cityname", city_name);
                Global.editor.putString("ref_code", ref_code);
                Global.editor.putString("user_image", user_image);
                Global.editor.commit();

               /* startActivity(new Intent(getActivity(), PreferenceActivity.class));*/
              //  finish();
                progressDialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

//                if (error instanceof TimeoutError) {
//                    Toast.makeText(LoginActivity.this, "Request Time-Out", Toast.LENGTH_LONG).show();
//                } else if (error instanceof NoConnectionError) {
//                    Toast.makeText(LoginActivity.this, "No Connection Found", Toast.LENGTH_LONG).show();
//                } else if (error instanceof ServerError) {
//                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_LONG).show();
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_LONG).show();
//                } else if (error instanceof ParseError) {
//                    Toast.makeText(LoginActivity.this, "Parse Error", Toast.LENGTH_LONG).show();}
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }

        /*    @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }*/
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                8000, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


        queue.add(request);
    }

}