package com.ziac.aquastpapp.Activities;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.google.android.material.textfield.TextInputEditText;
import com.ziac.aquastpapp.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyEmailOTPActivity extends AppCompatActivity {
    String otp, Newpassword, email_show;

    PinView Enter_pinnumber;
    TextView EmailVerify, Email_show,Resendotp;
    ProgressBar progressBar;
    ImageView back_btn;
    private boolean passwordvisible = false;
    TextInputEditText EnterNewpwd;
    Context context;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifiy_email_otp);
        context = this;
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Enter_pinnumber = findViewById(R.id.pinview);
        EmailVerify = findViewById(R.id.userverifyotp);
        Email_show = findViewById(R.id.showemail);
        progressBar = findViewById(R.id.progressbr);
        EnterNewpwd = findViewById(R.id.enternewpassword);
        Resendotp = findViewById(R.id.resendEotp);

        email_show = Global.sharedPreferences.getString("pfemail", "");
        Email_show.setText(email_show);


        Resendotp.setOnClickListener(v -> startActivity(new Intent(VerifyEmailOTPActivity.this, ResetPasswordEmailActivity.class)));
        EnterNewpwd.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= EnterNewpwd.getRight() - EnterNewpwd.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = EnterNewpwd.getSelectionEnd();
                    if (passwordvisible) {
                        EnterNewpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_on, 0);
                        EnterNewpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordvisible = false;
                    } else {
                        EnterNewpwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off, 0);
                        EnterNewpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordvisible = true;
                    }
                    EnterNewpwd.setSelection(selection);
                    return true;
                }
            }
            return false;
        });
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EmailVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting the PinView data
                Newpassword = EnterNewpwd.getText().toString();
                otp = Enter_pinnumber.getText().toString();

                if (!TextUtils.isEmpty(otp)) {
                    // Check if other required fields are not empty
                    if (!TextUtils.isEmpty(Newpassword)) {
                        EmailDataUsingVolley(otp);
                    } else {
                        Toast.makeText(VerifyEmailOTPActivity.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyEmailOTPActivity.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void EmailDataUsingVolley(String otp) {

        String Eurl = Global.validateotpurl;
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, Eurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    String issuccess = respObj.getString("isSuccess");
                    String error = respObj.getString("error");
                    progressBar.setVisibility(View.GONE);

                    switch (issuccess) {
                        case "true":
                            startActivity(new Intent(VerifyEmailOTPActivity.this, LoginSignupActivity.class));
                            finish();
                            break;
                        default:
                            if (error.equals("PasswordError")) {
                                showAlertDialog("Wrong Password", "The entered password is incorrect. Please try again.");
                            } else {
                                //showAlertDialog("General Error", "An error occurred. Please try again.");
                                Global.customtoast(VerifyEmailOTPActivity.this, getLayoutInflater(), error);
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Global.customtoast(VerifyEmailOTPActivity.this, getLayoutInflater(), e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", otp);
                params.put("Mobile", "");
                params.put("FPType", "E");
                params.put("UserName", "");
                params.put("NewPassword", Newpassword);
                params.put("user_email", Global.sharedPreferences.getString("user_email", ""));
               // Log.d("params", params.toString());
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(0), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // You can perform any action here or leave it empty
                    }
                })
                .show();
    }
}