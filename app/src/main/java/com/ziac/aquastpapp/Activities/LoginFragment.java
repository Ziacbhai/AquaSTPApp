package com.ziac.aquastpapp.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ziac.aquastpapp.R;


public class LoginFragment extends Fragment {

    EditText log_email, log_pwd;
    Button login_btn;
    TextView TermsOfUse, privacy, forgotpwd, Login;
    private CheckBox mcheckBox;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Context context = getContext();

        log_email = view.findViewById(R.id.email2);
        log_pwd = view.findViewById(R.id.password2);
        login_btn = view.findViewById(R.id.loginbtn);

        mcheckBox = view.findViewById(R.id.lcheckBox);
        TermsOfUse = view.findViewById(R.id.text31);
        privacy = view.findViewById(R.id.text6);
        forgotpwd = view.findViewById(R.id.btnftpass);

        return view;

    }


}