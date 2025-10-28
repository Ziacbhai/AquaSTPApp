package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ziac.aquastpapp.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import Fragments.ConsumptionFragment;
import Fragments.EquipmentsFragment;
import Fragments.HomeFragment;
import Fragments.IncidentReportingFragment;
import Fragments.ItemStockFragment;
import Fragments.RepairFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    ConstraintLayout layout;
    NavigationView navigationView;
    CircleImageView Profile;
    ActionBarDrawerToggle toggle;
    Context context;
    boolean click = true;
    String userimage, usermail, stpname, sitename, siteaddress, userref, personname, processname, stpcapacity;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }

        user_topcard();
        toolbar = findViewById(R.id.toolbar);
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        Profile = findViewById(R.id.profileIcon);
        drawerLayout = findViewById(R.id.drawerlayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

        getuserdetails();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        openFragment(new HomeFragment());

        userimage = Global.userImageurl + sharedPreferences.getString("user_image", "");
        Picasso.Builder builder = new Picasso.Builder(getApplication());
        Picasso picasso = builder.build();
        Picasso.get().setIndicatorsEnabled(true);
        Picasso.get().setLoggingEnabled(true);
        picasso.load(Uri.parse(userimage))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.no_image_available_icon)
                .into(Profile);


        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                popup.getMenuInflater().inflate(R.menu.profile_pop_up, popup.getMenu());
                // Retrieve data from SharedPreferences

                String profileName = Global.sharedPreferences.getString("ref_code", "");
                MenuItem profileMenuItem = popup.getMenu().findItem(R.id.refaral_code);
                profileMenuItem.setTitle("Code: " + profileName);

                userimage = Global.userImageurl + sharedPreferences.getString("user_image", "");
                Picasso.get().load(userimage)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .error(R.drawable.no_image_available_icon)
                        .into(Profile);

                popup.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.my_profile) {
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        return true;
                    }
                    if (itemId == R.id.nav_logout) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Logout Confirmation");
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked "Yes", perform logout action
                                startActivity(new Intent(MainActivity.this, LoginSignupActivity.class));
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked "No", dismiss the dialog
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        return true;

                    }
                    if (itemId == R.id.changepwd) {
                        startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
                        return true;
                    } else {

                    }
                    return true;
                });
                popup.show();
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                ImageView ProfileHeader;
                TextView person_name, user_mail, user_site, user_stp;
                ProfileHeader = drawerLayout.findViewById(R.id.header_profile);
                layout = findViewById(R.id.headeProfile);

                userimage = Global.userImageurl + sharedPreferences.getString("user_image", "");
                Picasso.get().load(userimage)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .error(R.drawable.no_image_available_icon)
                        .into(ProfileHeader);

                person_name = drawerLayout.findViewById(R.id.profilename);
                user_mail = drawerLayout.findViewById(R.id.headeremail);
                user_site = drawerLayout.findViewById(R.id.site_name);
                user_stp = drawerLayout.findViewById(R.id.stp_name);


                person_name.setText(personname);
                user_mail.setText(usermail);
                user_site.setText(sitename);
                user_stp.setText(stpname);


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

    }



    private void user_topcard() {
        usermail = sharedPreferences.getString("user_email", "");
        userref = sharedPreferences.getString("ref_code", "");
        sitename = sharedPreferences.getString("site_name", "");
        stpname = sharedPreferences.getString("stp_name", "");
        siteaddress = sharedPreferences.getString("site_address", "");
        processname = sharedPreferences.getString("process_name", "");
        personname = sharedPreferences.getString("user_name", "");
        stpcapacity = sharedPreferences.getString("stp_capacity", "");

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_dashboard) {
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, homeFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
            return true;
        }
        if (itemId == R.id.nav_itemstock) {
            ItemStockFragment itemStockFragment = new ItemStockFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, itemStockFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
            return true;
        }

        if (itemId == R.id.nav_aboutus) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            return true;
        }
        if (itemId == R.id.nav_equipment) {
            EquipmentsFragment equipmentsFragment = new EquipmentsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, equipmentsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
            return true;
        }
        if (itemId == R.id.nav_repair) {
            RepairFragment repairFragment = new RepairFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, repairFragment);
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
            return true;
        }
        /*if (itemId == R.id.nav_lab) {
            LabTestFragment labTestFragment = new LabTestFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, labTestFragment);
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
            return true;
        }*/
        if (itemId == R.id.nav_incident) {
            //startActivity(new Intent(MainActivity.this, IncidentReportingFragment.class));
            IncidentReportingFragment reportingFragment = new IncidentReportingFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, reportingFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
            return true;
        }
        if (itemId == R.id.nav_consumption) {
            ConsumptionFragment consumables = new ConsumptionFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, consumables);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
            return true;
        }
        if (itemId == R.id.nav_share) {
            shareContent();
            return true;
        }
        if (itemId == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Logout Confirmation");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User clicked "Yes", perform logout action
                    startActivity(new Intent(MainActivity.this, LoginSignupActivity.class));
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User clicked "No", dismiss the dialog
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return true;

        }

        if (itemId == R.id.nav_selectStp) {
            startActivity(new Intent(MainActivity.this, SelectSTPLocationActivity.class));
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
        // Get the referral code from SharedPreferences
        String refCode = sharedPreferences.getString("ref_code", "");
        // Build the shared content
        String shareMessage = "Referral Code: " + refCode +
                "\n\nSmart Operations & Monitoring Solution. Click the link to download the app from the Google Play Store:\n" +
                "https://play.google.com/store/apps/details?id=com.ziac.aquastpapp\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        // Launch the sharing dialog
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    private void openFragment(Fragment fragment) {
        HomeFragment fragment1 = new HomeFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        switch (count) {
            case 0:
                break;
            case 1:
                super.onBackPressed();
                break;
            case 2:
                getSupportFragmentManager().popBackStack();
                break;
            case 3:
                getSupportFragmentManager().popBackStack();
                break;
            case 4:
                getSupportFragmentManager().popBackStack();
                break;
            case 5:
                getSupportFragmentManager().popBackStack();
                break;
            case 6:
                getSupportFragmentManager().popBackStack();
                break;
            case 7:
                getSupportFragmentManager().popBackStack();
                break;
            default:
                getSupportFragmentManager().popBackStack();
                break;
        }
    }

    private void getuserdetails() {
        String url = Global.getuserprofileurl;
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject respObj1 = new JSONObject(response);
                JSONObject respObj = new JSONObject(respObj1.getString("data"));
                userimage = Global.userImageurl + Global.sharedPreferences.getString("user_image", "");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String accesstoken = Global.sharedPreferences.getString("access_token", null);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_name", Global.sharedPreferences.getString("user_name", null));
                return params;
            }
        };
        queue.add(request);
    }


}