package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    LinearLayout layout;
    FloatingActionButton fab;
    NavigationView navigationView;
    CircleImageView Profile;
    ActionBarDrawerToggle toggle;
    TextView usernameH,usermailH;
    Intent intent;
    PopupWindow popUp;
    private boolean doubleBackToExitPressedOnce;

    boolean click = true;
    private String personname,userimage,mail,Stpname ,Sitename;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if (!isNetworkAvailable()) {showToast("Internet connection lost !!");}

       // getSupportActionBar().setDisplayShowTitleEnabled(false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        toolbar = findViewById(R.id.toolbar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        Profile = findViewById(R.id.profileIcon);
        drawerLayout = findViewById(R.id.drawerlayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /*Menu menu = navigationView.getMenu();
        MenuItem RefItem = menu.findItem(R.id.refaral_code);
        View RefView = RefItem.getActionView();
        TextView refTitle = RefView.findViewById(R.id.design_menu_item_text);
        refTitle.setBackgroundColor(getResources().getColor(R.color.your_color_for_profile_title));*/

        openFragment(new HomeFragment());

        userimage = Global.userImageurl + sharedPreferences.getString("user_image", "");
        personname = sharedPreferences.getString("person_name", "");
        mail = sharedPreferences.getString("user_email", "");
        Sitename = sharedPreferences.getString("site_name", "");
        Stpname = sharedPreferences.getString("stp_name", "");



        Picasso.Builder builder=new Picasso.Builder(getApplication());
        Picasso picasso=builder.build();
        picasso.load(Uri.parse(userimage))
               .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(Profile );


      Profile.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
            popup.getMenuInflater().inflate(R.menu.profile_pop_up, popup.getMenu());

            // Retrieve data from SharedPreferences
            Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String profileName = Global.sharedPreferences.getString("ref_code", "");
            MenuItem profileMenuItem = popup.getMenu().findItem(R.id.refaral_code);
            profileMenuItem.setTitle("Code: " + profileName);


            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.my_profile) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    return true;
                }if (itemId == R.id.nav_logout) {
                    startActivity(new Intent(MainActivity.this, LoginSignupActivity.class));
                    return true;
                }if (itemId == R.id.changepwd) {
                    startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
                    return true;
                }
                else {

                }
                return false;
            });
            popup.show();
          }
      });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                CircleImageView ProfileH;
                TextView personnameH,usermailH ,usersiteH,userstpH ;
                ProfileH=drawerLayout.findViewById(R.id.profileH);
                layout = findViewById(R.id.headeProfile);

                userimage = Global.userImageurl + sharedPreferences.getString("user_image", "");
                Picasso.get().load(userimage).into(ProfileH);

                personnameH =drawerLayout. findViewById(R.id.headerusername);
                usermailH = drawerLayout.findViewById(R.id.headeremail);
                usersiteH = drawerLayout.findViewById(R.id.site_name);
                userstpH = drawerLayout.findViewById(R.id.stp_name);

                personnameH.setText(personname);
                usermailH.setText(mail);
                usersiteH.setText(Sitename);
                userstpH.setText(Stpname);

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    }
                });
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                //Toast.makeText(NewMainActivity.this, "On onDrawerClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                //Toast.makeText(NewMainActivity.this, "On onDrawerStateChanged", Toast.LENGTH_SHORT).show();

            }
        });

       /* Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userimage = Global.userImageurl + Global.sharedPreferences.getString("user_image", "");
                showImage(picasso,userimage);

            }
        });*/

       // bottomNavigationView=findViewById(R.id.bottomNavigationView);
       // bottomNavigationView.setBackground(null);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_aboutus) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            return true;
        }
        if (itemId == R.id.nav_share) {
            shareContent();
            return true;
        }
        if (itemId == R.id.nav_logout) {
            startActivity(new Intent(MainActivity.this, LoginSignupActivity.class));
            return true;
        }if (itemId == R.id.nav_selectStp) {
            startActivity(new Intent(MainActivity.this, SelectLocationActivity.class));
            return true;
        }
        // Close the drawer after an item is selected (optional)
        drawerLayout.closeDrawers();
        return true;
    }
    private void shareContent() {
        // Create an intent to share content
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String sAux = "\n Smart Operations & Monitoring Solution Click the link to download the app from the Google Play Store\n\n";
        sAux = sAux + "https://play.google.com/store/apps/details?id=com.ziac.aquastpapp\n\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT,sAux);
        // Launch the sharing dialog
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
    private void openFragment(Fragment fragment){
        HomeFragment homeFragment=new HomeFragment();
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

   /* public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            moveTaskToBack(true);
            System.exit(1);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }*/

    @Override
    public void onBackPressed() {

    }
}