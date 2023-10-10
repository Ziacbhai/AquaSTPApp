package com.ziac.aquastpapp.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Models.zList;


public class SignUpFragment extends Fragment {

    TextView TermsOfUse, privacy;
    private Dialog zDialog;
    EditText Company,CPerson,Mobile,Email ,AUname,RPassword ,Cpassword ,Ccode;
    AppCompatButton Registerbtn;

    TextView tvState, tvCity;
    private zList statename;

    ImageView DDstate,DDcity;

    String citycode;

    private CheckBox mcheckBox;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);



        Company = view.findViewById(R.id.company);
        CPerson = view.findViewById(R.id.cperson);
        Mobile = view.findViewById(R.id.mobile);
        Email = view.findViewById(R.id.remail);
        AUname = view.findViewById(R.id.auname);

        Registerbtn = view.findViewById(R.id.registerbtn);

        RPassword = view.findViewById(R.id.rpassword);
        Cpassword = view.findViewById(R.id.rcpassword);
        Ccode = view.findViewById(R.id.rccode);

        TermsOfUse = view.findViewById(R.id.terms);
        privacy = view.findViewById(R.id.privacy);

        tvState = view.findViewById(R.id.tvstate);
        tvCity = view.findViewById(R.id.tvcity);

        getstates();

        tvState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statespopup();
            }
        });


        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citiespopup();
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

        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewUser();
            }
        });

        return view;
    }


    private void getstates() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.GET, Global.urlGetStates, null, new Response.Listener<JSONArray>() {
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
        //zDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        zDialog.setContentView(R.layout.popup_list);

        ListView lvStates = zDialog.findViewById(R.id.lvstates);

        if (Global.statearraylist == null || Global.statearraylist.size() == 0) {
            // Toast.makeText(getBaseContext(), "States list not found !! Please try again !!", Toast.LENGTH_LONG).show();
            return;
        }
        final StateAdapter laStates = new StateAdapter(Global.statearraylist);
        lvStates.setAdapter(laStates);
        zDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        SearchView svstate = zDialog.findViewById(R.id.svstate);

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
    private class StateAdapter extends BaseAdapter implements Filterable{
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

    }

    private void citiespopup() {
    }

    private void CreateNewUser() {
        String company,cpperson,mobile,email,password,cpassword ,state,city;

        company = Company.getText().toString();
        cpperson = CPerson.getText().toString();
        mobile = Mobile.getText().toString();
        email = Email.getText().toString();

        state = Cpassword.getText().toString();
        city = Cpassword.getText().toString();

        password = RPassword.getText().toString();
        cpassword = Cpassword.getText().toString();



        if ( company.isEmpty() || cpperson.isEmpty()  || mobile.isEmpty()  ||  email.isEmpty() || cpassword.isEmpty() || state.isEmpty() || city.isEmpty()
                || password.isEmpty()){

            Toast.makeText(getActivity(),"Complete the information and try again !!",Toast.LENGTH_SHORT).show();
            return;
        }if (mobile.length() < 10 ){
            Toast.makeText(getActivity(),"Mobile number should not be less than 10 digits !!",Toast.LENGTH_SHORT).show();
            return;
        }if (!password.matches( "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,15}$") ){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "password must contain mix of upper and lower case letters as well as digits and one special charecter !!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            return;
        }
    }
}