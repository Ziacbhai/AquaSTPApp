package com.ziac.aquastpapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.ziac.aquastpapp.R;

public class SliderScreenActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private static final String TAG = "SliderActivity";
    private TextView Next, Skip;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_screen);

        context = this;

        Next = findViewById(R.id.next);
        Skip = findViewById(R.id.skip);

        viewPager = findViewById(R.id.views);

        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

        // page layout

        int layouts[] = {

                R.layout.slider1,
                R.layout.slider2,
                R.layout.slider3
        };

        viewPager.setAdapter(new PagerAdapter() {
            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(layouts[position], container, false);
                container.addView(view);
                return view;
            }

            @Override
            public int getCount() {
                return layouts.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                View view = (View) object;
                container.removeView(view);
            }
        });

        ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onPageSelected(int position) {

                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts.length - 1) {
                    // last page. make button text to GOT IT
                    Next.setText("Got it");
                    Skip.setVisibility(View.GONE);
                } else {
                    // still pages are left
                    Next.setText("Next");
                    Skip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        };

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotosignin();

            }
        });

        Next.setOnClickListener(v -> {
            // checking for last page
            // if last page home screen will be launched
            int current = getItem(+1);
            if (current < layouts.length) {
                // move to next screen
                viewPager.setCurrentItem(current);
            } else {

                gotosignin();
            }
        });
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void gotosignin() {
        startActivity(new Intent(SliderScreenActivity.this, LoginSignupActivity.class));
        finish();

    }
}