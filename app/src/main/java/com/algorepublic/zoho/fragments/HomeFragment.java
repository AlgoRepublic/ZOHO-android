package com.algorepublic.zoho.fragments;

import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;


/**
 * Created by android on 3/14/16.
 */
public class HomeFragment extends BaseFragment {

    private BaseClass baseClass;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        MyPagerAdapter adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        applyLightBackground(tabs, baseClass);
        pager.setCurrentItem(1);
        getToolbar().setTitle(getString(R.string.dashboard));

        // uncomment below code for toolbar title text change on pager item change.
   /*     pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0)
                    getToolbar().setTitle(getResources().getString(R.string.dashboard));
                if(position==1)
                    getToolbar().setTitle(getResources().getString(R.string.feeds));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        return view;
    }


    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private final String[] TITLES = { getString(R.string.dashboard),
                getString(R.string.feeds)};

        public MyPagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position].toLowerCase();
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return DashboardFragment.newInstance();
                case 1:
                    return FeedsFragment.newInstance();
            }
            return null;
        }

    }
}
