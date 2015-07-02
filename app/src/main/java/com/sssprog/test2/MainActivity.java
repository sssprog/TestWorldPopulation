package com.sssprog.test2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    public Toolbar toolbar;
    @InjectView(R.id.tabLayout)
    TabLayout tabLayout;
    @InjectView(R.id.pager)
    ViewPager pager;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initToolbar();
        if (Config.hasLollipop()) {
            toolbar.setElevation(0);
        }

        pager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(pager);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        initToolbarShadow();
    }

    private void initToolbarShadow() {
        if (Config.hasLollipop()) {
            return;
        }
        final ViewGroup shadowContainer = (ViewGroup) findViewById(android.R.id.content);
        final View shadow = getLayoutInflater().inflate(R.layout.toolbar_shadow, shadowContainer, false);
        shadowContainer.addView(shadow);
        final View aboveView = tabLayout;
        aboveView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            int bottom = 0;
            int[] location = new int[2];

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int b, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                aboveView.getLocationOnScreen(location);
                int newBottom = location[1] + aboveView.getHeight() - getContentViewTop();
                if (newBottom != bottom) {
                    bottom = newBottom;
                    ((ViewGroup.MarginLayoutParams) shadow.getLayoutParams()).topMargin = bottom;
                    shadow.requestLayout();
                }
            }

            private int getContentViewTop() {
                shadowContainer.getLocationOnScreen(location);
                return location[1];
            }
        });
    }

    public void onEventMainThread(GoToPage1Event event) {
        pager.setCurrentItem(0, true);
    }

    class HomePagerAdapter extends FragmentPagerAdapter {

        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? new Page1Fragment() : new Page2Fragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + (position + 1);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
