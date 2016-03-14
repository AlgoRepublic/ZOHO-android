package com.algorepublic.zoho.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.astuetz.PagerSlidingTabStrip;

import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by android on 3/14/16.
 */
public class HomeFragment extends BaseFragment {

    AQuery aq;
    View view;

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    BaseClass baseClass;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        pager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        tabs.setIndicatorHeight(150);
        //tabs.setIndicatorColor(getResources().getColor(R.color.col));
        //tabs.setTextColor(getResources().getColor(R.color.myTextPrimaryColor));

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
                    return DashboardFragment.newInstance();
            }
            return null;
        }

    }
}
