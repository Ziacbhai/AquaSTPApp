package com.ziac.aquastpapp.Activities;

import static com.ziac.aquastpapp.Activities.Global.sharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ziac.aquastpapp.R;

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
    TextView usernameH, usermailH;
    Intent intent;
    PopupWindow popUp;
    private boolean doubleBackToExitPressedOnce;

    boolean click = true;
    private String userimage, mail, Stpname, Sitename, Siteaddress, userref, person_name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if (!isNetworkAvailable()) {showToast("Internet connection lost !!");}

        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        toolbar = findViewById(R.id.toolbar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
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
        userref = sharedPreferences.getString("ref_code", "");
        person_name = Global.sharedPreferences.getString("person_name", "");

        String mail = Global.sharedPreferences.getString("user_email", "");
        Sitename = sharedPreferences.getString("site_name", "");
        Stpname = sharedPreferences.getString("stp_name", "");
        ////
        Siteaddress = sharedPreferences.getString("site_address", "");
        Siteaddress = sharedPreferences.getString("process_name", "");

        Picasso.Builder builder = new Picasso.Builder(getApplication());
        Picasso picasso = builder.build();
        picasso.load(Uri.parse(userimage))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(Profile);
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

                userimage = Global.userImageurl + sharedPreferences.getString("user_image", "");
                Picasso.get().load(userimage).into(Profile);

                popup.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.my_profile) {
                        startActivity(new Intent(MainActivity.this, WelcomeOwner.class));
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
                    return false;
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
                CircleImageView ProfileH;
                TextView personnameH, usermailH, usersiteH, userstpH;
                ProfileH = drawerLayout.findViewById(R.id.profileH);
                layout = findViewById(R.id.headeProfile);

                userimage = Global.userImageurl + sharedPreferences.getString("user_image", "");
                Picasso.get().load(userimage).into(ProfileH);

                personnameH = drawerLayout.findViewById(R.id._profilename);
                usermailH = drawerLayout.findViewById(R.id.headeremail);
                usersiteH = drawerLayout.findViewById(R.id.site_name);
                userstpH = drawerLayout.findViewById(R.id.stp_name);


                personnameH.setText(person_name);
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

        if (itemId == R.id.nav_dashboard) {
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, homeFragment);
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
            return true;
        }
        if (itemId == R.id.nav_itemstock) {
            ItemStockFragment itemStockFragment = new ItemStockFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, itemStockFragment);
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
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
            return true;
        }if (itemId == R.id.nav_repair) {
            RepairFragment repairFragment = new RepairFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, repairFragment);
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
            return true;
        }if (itemId == R.id.nav_lab) {
            LabTestFragment labTestFragment = new LabTestFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, labTestFragment);
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
            return true;
        }if (itemId == R.id.nav_incident) {
            IncidentReportingFragment inFragment = new IncidentReportingFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, inFragment);
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
            return true;
        }
        if (itemId == R.id.nav_consumable) {
            ConsumablesFragment consumables = new ConsumablesFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, consumables);
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
        FiltersFragment homeFragment = new FiltersFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void loadHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, homeFragment);
        fragmentTransaction.commit();
    }




    @Override
    public void onBackPressed() {

    }
}