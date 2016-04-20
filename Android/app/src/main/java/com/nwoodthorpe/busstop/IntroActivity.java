package com.nwoodthorpe.busstop;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

public class IntroActivity extends FragmentActivity {

        static final int ITEMS = 4;
        MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());
        ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TutorialFragment fragment = (TutorialFragment) mAdapter.instantiateItem(mPager, position);
                fragment.startAnimation();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return ITEMS;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return TutorialFragment.init(position);
        }
    }

    private static class CustomOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position){

        }
    }
}
