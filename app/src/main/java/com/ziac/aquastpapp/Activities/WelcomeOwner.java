package com.ziac.aquastpapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ziac.aquastpapp.R;

public class WelcomeOwner extends AppCompatActivity {

   private TextView Wname,Mailid,Mobno,ClickHere;
    ImageView circularImageView;
    AppCompatButton wContinue;
    private String username;
    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_owner);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        circularImageView = findViewById(R.id.imageurl);
        Wname = findViewById(R.id.wname);
        ClickHere = findViewById(R.id.Clickhere);
        wContinue = findViewById(R.id.WContinue);

        Mailid = findViewById(R.id.wemail);
        Mobno = findViewById(R.id.wph);

        String usrname = Global.sharedPreferences.getString("username", "");
        String mail = Global.sharedPreferences.getString("user_email", "");
        String mobile = Global.sharedPreferences.getString("user_mobile", "");
        String userimage = Global.userimageurl + Global.sharedPreferences.getString("user_image", "");

        Wname.setText(usrname);
        Mailid.setText(mail);
        Mobno.setText(mobile);

        Picasso.Builder builder=new Picasso.Builder(getApplication());
        Picasso picasso=builder.build();
        picasso.load(Uri.parse(userimage)).into(circularImageView );

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userimage = Global.userimageurl + Global.sharedPreferences.getString("user_image", "");
                showImage(picasso,userimage);

            }

            public void showImage(Picasso picasso, String userimage) {
                Dialog builder = new Dialog(context);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        // Nothing
                    }
                });

                // Calculate display dimensions
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                int screenWidth = displayMetrics.widthPixels;
                int screenHeight = displayMetrics.heightPixels;

                // Load the image using Picasso
                picasso.load(Uri.parse(userimage)).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        ImageView imageView = new ImageView(getApplicationContext());

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
                        // Handle bitmap loading failure
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        // Prepare bitmap loading
                    }
                });
            }
        });


        ClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeOwner.this,ProfileActivity.class);
                startActivity(i);

            }
        });

        wContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(WelcomeOwner.this,ProfileActivity.class);
                startActivity(in);
            }
        });


    }
}