package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.urlGetStates;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Models.zList;


public class SignUpFragment extends Fragment {

    private Dialog zDialog;
    EditText Company, CPerson, Mobile, Email, Adminname, RPassword, Cpassword;
    AppCompatButton Registerbtn;
    TextView tvState, tvCity, Site_address, TermsOfUse, Privacy;
    ImageView DDstate, DDcity;
    private zList statename, cityname;
    private boolean passwordvisible = false;
    String citycode;
    ProgressDialog progressDialog;
    private CheckBox CheckBox;

    Context context;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        context = getContext();
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);

        Company = view.findViewById(R.id.company);
        CPerson = view.findViewById(R.id.cperson);
        Mobile = view.findViewById(R.id.mobile);
        Email = view.findViewById(R.id.emailr);
        Adminname = view.findViewById(R.id.auname);
        CheckBox = view.findViewById(R.id.ccheckbox);
        Site_address = view.findViewById(R.id.site_address);
        Registerbtn = view.findViewById(R.id.registerbtn);
        RPassword = view.findViewById(R.id.rpassword);
        Cpassword = view.findViewById(R.id.rcpassword);
        TermsOfUse = view.findViewById(R.id.terms);
        Privacy = view.findViewById(R.id.privacy);

        //////Call Api State And City/////
        tvState = view.findViewById(R.id.tvstate);
        DDstate = view.findViewById(R.id.dd_state);

        DDcity = view.findViewById(R.id.dd_city);
        tvCity = view.findViewById(R.id.tvcity);

        getstates();
        tvState.setOnClickListener(v -> statespopup());
        DDstate.setOnClickListener(v -> statespopup());

        DDcity.setOnClickListener(v -> citiespopup());
        tvCity.setOnClickListener(v -> citiespopup());

        RPassword.setOnTouchListener((v, event) -> {

            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= RPassword.getRight() - RPassword.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = RPassword.getSelectionEnd();
                    if (passwordvisible) {
                        RPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        RPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordvisible = false;

                    } else {
                        RPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        RPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordvisible = true;


                    }
                    RPassword.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        Cpassword.setOnTouchListener((v, event) -> {

            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Cpassword.getRight() - Cpassword.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Cpassword.getSelectionEnd();
                    if (passwordvisible) {
                        Cpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        Cpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordvisible = false;

                    } else {
                        Cpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        Cpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordvisible = true;
                    }
                    Cpassword.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        TermsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://ziacsoft.com/terms.html")));

            }
        });

        Privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://ziacsoft.com/privacy.html")));
            }
        });

        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateNewUser();
                //progressBar.setVisibility(View.VISIBLE);

            }


        });

        return view;

    }

    private void getstates() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        //String baseurl="http://192.168.1.4/AquaSTP/api/List/GetStates";
        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.GET, urlGetStates, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Global.statearraylist = new ArrayList<zList>();
                statename = new zList();
                for (int i = 0; i < response.length(); i++) {
                    final JSONObject e;
                    try {
                        // converting to json object
                        e = response.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    statename = new zList();
                    try {
                        // getting the state name from the object
                        statename.set_name(e.getString("state_name"));
                        statename.set_code(e.getString("state_code"));
                       /* Log.d("YourTag", "State Name: " + statename.get_name());
                        Log.d("YourTag", "State Code: " + statename.get_code());*/

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.statearraylist.add(statename);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayrequest);

    }

    private void statespopup() {

        zDialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);

        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvequipment);

        if (Global.statearraylist == null || Global.statearraylist.size() == 0) {
            // Toast.makeText(getBaseContext(), "States list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        final StateAdapter laStates = new StateAdapter(Global.statearraylist);
        lvStates.setAdapter(laStates);
        zDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        zDialog.show();

        SearchView svstate = zDialog.findViewById(R.id.svequipment);

        svstate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                laStates.getFilter().filter(newText);
                return false;
            }
        });


    }

    private class StateAdapter extends BaseAdapter implements Filterable {
        private ArrayList<zList> mDataArrayList;

        public StateAdapter(ArrayList<zList> mDataArrayList) {
            this.mDataArrayList = mDataArrayList;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    List<zList> mFilteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = Global.statearraylist;
                    } else {
                        for (zList dataList : Global.statearraylist) {
                            if (dataList.get_name().toLowerCase().contains(charString)) {
                                mFilteredList.add(dataList);
                            }
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilteredList;
                    filterResults.count = mFilteredList.size();
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mDataArrayList = (ArrayList<zList>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        @Override
        public int getCount() {
            return mDataArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder") View v = getLayoutInflater().inflate(R.layout.popup_listitems, null);
            final TextView tvstatenameitem = v.findViewById(R.id.tvsingleitem);
            statename = mDataArrayList.get(i);
            tvstatenameitem.setText(statename.get_name());
            tvstatenameitem.setOnClickListener(view1 -> {
                statename = mDataArrayList.get(i);
                tvState.setText(statename.get_name());
                zDialog.dismiss();
                getcity();
            });
            return v;
        }

    }

    private void getcity() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Global.urlGetCities;
        url = url + statename.get_code();

        StringRequest jsonArrayrequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String sresponse) {
                JSONArray response;
                try {
                    response = new JSONArray(sresponse);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Global.cityarraylist = new ArrayList<zList>();
                cityname = new zList();
                tvCity.setText("");
                for (int i = 0; i < response.length(); i++) {
                    final JSONObject e;
                    try {
                        // converting to json object
                        e = response.getJSONObject(i);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    cityname = new zList();
                    try {
                        // getting the state name from the object
                        cityname.set_name(e.getString("city_name"));
                        cityname.set_code(e.getString("city_code"));
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                    Global.cityarraylist.add(cityname);
                }
                try {
                    if (Global.cityarraylist != null && !Global.cityarraylist.isEmpty()) {
                        tvCity.setText(Global.cityarraylist.get(0).get_name());
                        citycode = Global.cityarraylist.get(0).get_code();
                    } else {
                        Global.customtoast(getActivity(), getLayoutInflater(), "No cities found for selected state");

                    }
                } catch (Exception e) {
                    // Handle any potential exceptions here (optional)
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                // params.put("state_code", String.valueOf(statename.get_code()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                Log.d("getHeaders", params.toString());
                return params;
            }
        };
        jsonArrayrequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(2500), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(jsonArrayrequest);

    }

    private void citiespopup() {
        zDialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        //zDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvequipment);

        if (Global.cityarraylist == null || Global.cityarraylist.size() == 0) {
//            Toast.makeText(getBaseContext(), "City list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            Global.customtoast(getActivity(), getLayoutInflater(), "No cities found for selected state");

            return;
        }
        final CityAdapter laStates = new CityAdapter(Global.cityarraylist);
        lvStates.setAdapter(laStates);

        zDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        zDialog.show();

        SearchView svstate = zDialog.findViewById(R.id.svequipment);

        svstate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
                laStates.getFilter().filter(newText);
                return false;
            }
        });
    }

    private class CityAdapter extends BaseAdapter implements Filterable {

        private ArrayList<zList> mDataArrayList;

        public CityAdapter(ArrayList<zList> mDataArrayList) {
            this.mDataArrayList = mDataArrayList;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    List<zList> mFilteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = Global.statearraylist;
                    } else {
                        for (zList dataList : Global.statearraylist) {
                            if (dataList.get_name().toLowerCase().contains(charString)) {
                                mFilteredList.add(dataList);
                            }
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilteredList;
                    filterResults.count = mFilteredList.size();
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    mDataArrayList = (ArrayList<zList>) results.values;
                    notifyDataSetChanged();

                }
            };
        }

        @Override
        public int getCount() {
            return mDataArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View v = getLayoutInflater().inflate(R.layout.popup_listitems, null);
            final TextView tvstatenameitem = v.findViewById(R.id.tvsingleitem);

            cityname = mDataArrayList.get(i);
            tvstatenameitem.setText(cityname.get_name());
            tvstatenameitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cityname = mDataArrayList.get(i);
                    tvCity.setText(cityname.get_name());
                    citycode = cityname.get_code();
                    zDialog.dismiss();
                }
            });
            return v;
        }

    }
    private void CreateNewUser() {


        String company, cpperson, mobile, email, password, cpassword, adminname, state, city;

        company = Company.getText().toString();
        cpperson = CPerson.getText().toString();
        mobile = Mobile.getText().toString();
        email = Email.getText().toString();
        adminname = Adminname.getText().toString();
        state = tvState.getText().toString().trim();
        city = tvCity.getText().toString().trim();
        password = RPassword.getText().toString();
        cpassword = Cpassword.getText().toString();

        //  progressDialog.show();

        if (company.isEmpty()) {
            Company.setError("Company Name should not be empty");
            Company.requestFocus();
            return;
        } /*else if (company.contains(" ")) {
            // Check if the company name contains spaces
            Toast.makeText(requireActivity(), "Company Name should not contain spaces", Toast.LENGTH_SHORT).show();
            return;
        }*/

        if (cpperson.isEmpty()) {
            CPerson.setError("Please enter Contact Name");
            CPerson.requestFocus();

        }
        if (mobile.isEmpty()) {
            Mobile.setError("Mobile number should not be empty!!");
            Mobile.requestFocus();
        }

        if (email.isEmpty()) {
            //  Toast.makeText(getActivity(), "Email should not be empty!!", Toast.LENGTH_SHORT).show();
            Email.setError("Email should not be empty!!");
            Email.requestFocus();
            return;
        }
        if (adminname.isEmpty()) {
            Adminname.setError("Admin name field should not be empty!!");
            Adminname.requestFocus();
            //Toast.makeText(getActivity(), "Contact person field should not be empty!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (state.isEmpty()) {
            tvState.setError("State field should not be empty!!");
            tvState.requestFocus();
            // Toast.makeText(getActivity(), "Mobile number should not be empty!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobile.length() < 10) {
            Toast.makeText(getActivity(), "Mobile number should not be less than 10 digits !!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            RPassword.setError("Password field should not be empty!!");
            RPassword.requestFocus();
           /* Toast toast = Toast.makeText(getActivity(), "Password  field should not be empty!!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();*/
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getActivity(), "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,15}$")) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "password must contain mix of upper and lower case letters as well as digits and one special charecter !!", Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            return;
        }
        if (cpassword.isEmpty()) {
            Cpassword.setError("Confirm password field should not be empty!!");
            Cpassword.requestFocus();
            //Toast.makeText(getActivity(), "Confirm password field should not be empty!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!CheckBox.isChecked()) {
            Toast.makeText(getActivity(), "You are not  agree with the terms and conditions of Aqua to move further  ", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.registration, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String sresponse) {
                        progressDialog.dismiss();
                        JSONObject response = null;
                        try {
                            response = new JSONObject(sresponse);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        //Log.d("Register", sresponse);

                        try {
                            if (response.getBoolean("isSuccess")) {
                                Toast.makeText(getActivity(), "Registration successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), LoginSignupActivity.class));
                                //finish();
                            } else {
                                //textViewError.setText(response.getString("error"));
                                Toast.makeText(getActivity(), response.getString("error"), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                //textViewError.setVisibility(View.VISIBLE);
                            }

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                /*textViewError.setText(error.getLocalizedMessage());
                textViewError.setVisibility(View.VISIBLE);*/

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                Log.d("getHeaders", params.toString());
                return params;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                String emailValue = Email.getText().toString();
                if (emailValue.isEmpty()) {
                    emailValue = "notprovided@gmail.com";
                }
                params.put("com_contact", CPerson.getText().toString());
                params.put("com_contact_mobno", Mobile.getText().toString());
                params.put("com_email", emailValue);
                params.put("username", Adminname.getText().toString());
                params.put("password", RPassword.getText().toString());
                params.put("confirm_password", Cpassword.getText().toString());
                params.put("state_code", String.valueOf(statename.get_code()));
                params.put("city_code", String.valueOf(cityname.get_code()));
                params.put("com_name", Company.getText().toString());

                //Log.d("params", params.toString());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, // timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(stringRequest);

    }
}