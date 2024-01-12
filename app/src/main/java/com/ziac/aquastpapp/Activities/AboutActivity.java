package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.ziac.aquastpapp.R;

public class AboutActivity extends AppCompatActivity {

    private CardView Website, CallUs, MailUs, ChatUs, LinkedIn, FaceBook, Instagram, X;

    LinearLayout Locate;
    LottieAnimationView Ziac;

    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Website = findViewById(R.id.websiteaqua);
        CallUs = findViewById(R.id.callUs);
        MailUs = findViewById(R.id.mailUs);
        ChatUs = findViewById(R.id.chatUs);
        Locate = findViewById(R.id.locate);

        LinkedIn = findViewById(R.id.linkedIn);
        FaceBook = findViewById(R.id.Facebook);
        Instagram = findViewById(R.id.instagram);
        X = findViewById(R.id.X);

        Ziac = findViewById(R.id.animationView);
        // Declaring the animation view
        LottieAnimationView animationView
                = findViewById(R.id.animationView);
        animationView.addAnimatorUpdateListener(
                (animation) -> {
                    // Global.customtoast(this,getLayoutInflater(), "Working");
                });
        animationView.playAnimation();

        if (animationView.isAnimating()) {
            // Do something.
        }

        Locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = "Ziac Software No. 5, 2nd Cross, CSI compound, Mission Rd, Bengaluru, Karnataka 560027";
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });


        Website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://192.168.1.15/AquaSTP/")));
            }
        });
        CallUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phno = "+91 9845258746";
                Intent i = new Intent(Intent.ACTION_DIAL, (Uri.parse("tel:" + phno)));
                startActivity(i);
            }
        });
        MailUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
                intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
                intent.setData(Uri.parse("mailto:info@ziacsoft.com")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
            }
        });
        ChatUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.putExtra("...Message...", "...default content...");
                smsIntent.setType("vnd.android-dir/mms-sms");
                startActivity(smsIntent);
            }
        });
        Instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://instagram.com/ziacsoftwares?igshid=MzRlODBiNWFlZA==")));
            }
        });
        FaceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/ziacsoft/")));
            }
        });
        LinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://in.linkedin.com/company/ziacsoft")));
            }
        });
        X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/ziac3" +
                                "soft")));
            }
        });

    }

    public void onLocateClicked(View view) {
        String address = "Ziac Software No. 5, 2nd Cross, CSI compound, Mission Rd, Bengaluru, Karnataka 560027";
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}