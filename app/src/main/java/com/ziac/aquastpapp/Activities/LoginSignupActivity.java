package com.ziac.aquastpapp.Activities;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.ziac.aquastpapp.R;

public class LoginSignupActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter loginadapter;
    private boolean doubleBackToExitPressedOnce;
    Context context;
    TabLayout.Tab loginTab;
    TabLayout.Tab registerTab;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsignup);

        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setSelectedTabIndicatorHeight(0);

        loginTab = tabLayout.newTab();
        loginTab.setText("LOGIN");
        tabLayout.addTab(loginTab);

        registerTab = tabLayout.newTab();
        registerTab.setText("REGISTER");
        tabLayout.addTab(registerTab);

       // loginTab.view.setBackgroundColor(Color.rgb(1, 163, 163));
        tabLayout.setTabTextColors(Color.rgb(1, 163, 163), Color.WHITE);


        viewPager2 = findViewById(R.id.viewpagerlogin);
        loginadapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(loginadapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Update the tab colors and text colors when a page is selected
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if (tab != null) {
                        if (i == position) {
                            tab.view.setBackgroundColor(Color.rgb(1, 163, 163));
                            tabLayout.setTabTextColors(Color.rgb(1, 163, 163), Color.WHITE);
                        } else {
                            tab.view.setBackgroundColor(Color.TRANSPARENT);
                            tabLayout.setTabTextColors(Color.rgb(1, 163, 163), Color.WHITE); // Update with your desired unselected text color
                        }
                    }
                }
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());

                tab.view.setBackgroundColor(Color.rgb(1, 163, 163));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Reset the indicator color when a tab is unselected
                viewPager2.setCurrentItem(tab.getPosition());
                tab.view.setBackgroundColor(Color.TRANSPARENT);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            moveTaskToBack(true);
            System.exit(1);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

    }
}