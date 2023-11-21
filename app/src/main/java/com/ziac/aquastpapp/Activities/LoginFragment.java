package com.ziac.aquastpapp.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.BuildConfig;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.SiteLocationAdapter;
import Models.StpModelClass;


public class LoginFragment extends Fragment {

    EditText Log_User,Log_pwd ;
    private CheckBox RememberMe;
    Button Login_btn;
    TextView TermsOfUse, privacy, forgotpwd;
    boolean passwordVisible;
    String username, pwd;

    RecyclerView siteLocationRecyclerView;

    private SiteLocationAdapter siteLocationAdapter;


    StpModelClass stpModelClass;
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

    //SharedPreferences sharedPreferences;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Context context = getContext();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(true);
        Log_User = view.findViewById(R.id.Luser);
        Log_pwd = view.findViewById(R.id.password2);
        Login_btn = view.findViewById(R.id.loginbtn);
        TermsOfUse = view.findViewById(R.id.terms);
        privacy = view.findViewById(R.id.privacy);
        forgotpwd = view.findViewById(R.id.btnftpass);
        RememberMe = view.findViewById(R.id.RcheckBox);



       /* siteLocationRecyclerView = view.findViewById(R.id.stp_recyclerview);
        siteLocationRecyclerView.setHasFixedSize(true);
        siteLocationRecyclerView.setAdapter(siteLocationAdapter);
        siteLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL , false));*/


//      COMMENTED BELOW LINE FOR TESTING
//      siteLocationAdapter = new SiteLocationAdapter(Global.StpList, getContext());


        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        TextView versionName = view.findViewById(R.id.version);
        versionName.setText("v1.0.0" + BuildConfig.VERSION_NAME);


        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = Log_User.getText().toString();
                pwd = Log_pwd.getText().toString();
                username = Log_User.getText().toString();
                pwd = Log_pwd.getText().toString();
                if (username.isEmpty()) {
                    Log_User.setError("Please enter the User name");
                    Log_User.requestFocus();
                    return;
                } else if (pwd.isEmpty()) {
                    Log_pwd.setError("Please enter the Password");
                    Log_pwd.requestFocus();
                    return;
                }

                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                Global.editor = Global.sharedPreferences.edit();
                if (RememberMe.isChecked()){
                    Global.editor.putString("username", username);
                    Global.editor.putString("password", pwd);
                }else {
                    Global.editor.putString("username", "");
                    Global.editor.putString("password", "");
                }
                Global.editor.commit();
                dologinVolley();
            }
        });


        RememberMe.setChecked(false);
        try {
            Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            username = Global.sharedPreferences.getString("username", "");
            pwd = Global.sharedPreferences.getString("password", "");
            Log_User.setText(username);
            Log_pwd.setText(pwd);
            if (username.length() == 0) {
                RememberMe.setChecked(false);
            } else {
                RememberMe.setChecked(true);
            }

        } catch (Exception e) {
            RememberMe.setChecked(false);
        }

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
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://ziacsoft.com/privacy.html")));
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

        username = Log_User.getText().toString();
        pwd = Log_pwd.getText().toString();
        progressDialog.show();
        //new InternetCheckTask().execute();

        String url = Global.tokenurl;
        RequestQueue queue= Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject respObj = new JSONObject(response);
                    String access_token = respObj.getString("access_token");
                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putString("access_token", access_token);
                    Global.editor.commit();
                    //startActivity(new Intent(getActivity(), MainActivity.class));
                    getuserprofile();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Global.customtoast(getActivity(), getLayoutInflater(),"Invalid username / password error");
                progressDialog.dismiss();
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
                3000, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }


    private void getuserprofile() {

        String url = Global.getuserprofileurl;
        RequestQueue queue= Volley.newRequestQueue(getActivity());
       // progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            progressDialog.dismiss();

            try {
                JSONObject respObj1 = new JSONObject(response);
                JSONObject respObj = new JSONObject(respObj1.getString("data"));
                String user_code = respObj.getString("user_code");
                String person_name = respObj.getString("person_name");
                String com_code = respObj.getString("com_code");
                String user_image = respObj.getString("user_image");
                String user_type = respObj.getString("user_type");
                String user_mobile = respObj.getString("user_mobile");
                String user_email = respObj.getString("user_email");
                String ref_code = respObj.getString("ref_code");


                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putString("user_code", user_code);
                Global.editor.putString("person_name", person_name);
                Global.editor.putString("com_code", com_code);
                Global.editor.putString("user_image", user_image);
                Global.editor.putString("user_type", user_type);
                Global.editor.putString("user_mobile", user_mobile);
                Global.editor.putString("user_email", user_email);
                Global.editor.putString("ref_code", ref_code);
                Global.editor.putString("user_image", user_image);
               // preferences.edit().remove("text").commit();
                Global.editor.commit();

                JSONArray liststp = new JSONArray(respObj1.getString("data2"));
                int i;
                Global.StpList = new ArrayList<>();
                for (i = 0;i<liststp.length();i++){
                    final JSONObject e;
                    try {
                        e = liststp.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    //listofstp = new JSONObject(liststp[i]("site_code"));
                    stpModelClass = new StpModelClass();

                    stpModelClass.setSucode(e.getString("su_code"));
                    stpModelClass.setComcode(e.getString("com_code"));
                    stpModelClass.setUsercode(e.getString("user_code"));
                    stpModelClass.setPersonname(e.getString("person_name"));
                    stpModelClass.setUsername(e.getString("username"));
                    stpModelClass.setSstp1code(e.getString("sstp1_code"));
                    stpModelClass.setStpname(e.getString("stp_name"));
                    stpModelClass.setSitecode(e.getString("site_code"));
                    stpModelClass.setSitename(e.getString("site_name"));
                    stpModelClass.setStpactive(e.getString("stp_active"));

                    stpModelClass.setSite_address(e.getString("site_address"));
                    stpModelClass.setProcess__type(e.getString("process_name"));

                    Global.StpList.add(stpModelClass);
                }

                progressDialog.dismiss();

                switch (user_type) {
                    case "O":
                        Intent o = new Intent(getActivity(), WelcomeOwner.class);
                        o.setType(Settings.ACTION_SYNC_SETTINGS);
                        getActivity().startActivity(o);
                        break;
                    case "C":
                        Intent c = (new Intent(getActivity(), WelcomeCustomer.class));
                        c.setType(Settings.ACTION_SYNC_SETTINGS);
                        getActivity().startActivity(c);
                        break;
                    case "S":
                        Intent s = (new Intent(getActivity(), WelcomeSupervisor.class));
                        s.setType(Settings.ACTION_SYNC_SETTINGS);
                        getActivity().startActivity(s);
                        break;
                    case "M":
                        Intent m =(new Intent(getActivity(), WelcomeManager.class));
                        m.setType(Settings.ACTION_SYNC_SETTINGS);
                        getActivity().startActivity(m);
                        break;
                    case "U":
                        Intent u =(new Intent(getActivity(), WelcomeUser.class));
                        u.setType(Settings.ACTION_SYNC_SETTINGS);
                        getActivity().startActivity(u);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // progressDialog.dismiss();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
                headers.put("Authorization", "Bearer " + accesstoken);

                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                8000, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        queue.add(request);
    }


}