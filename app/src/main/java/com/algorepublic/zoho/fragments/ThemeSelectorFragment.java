package com.algorepublic.zoho.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by android on 4/1/16.
 */
public class ThemeSelectorFragment extends BaseFragment {

    public static ThemeSelectorFragment newInstance() {
        return new ThemeSelectorFragment();
    }
    BaseClass baseClass; AQuery aq;
    public ThemeSelectorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_theme_selector, container, false);
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        if (baseClass.getThemePreference() == R.style.AppThemeBlue){
            aq.id(R.id.blue_theme).checked(true);
        }else{
            aq.id(R.id.black_theme).checked(true);
        }
        ((RelativeLayout) view.findViewById(R.id.black_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aq.id(R.id.blue_theme).checked(false);
                aq.id(R.id.blue_layout).checked(true);
                baseClass.setThemePreference(R.style.AppTheme);
                StartActivity();
            }
        });
        ((RelativeLayout) view.findViewById(R.id.blue_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aq.id(R.id.blue_theme).checked(true);
                aq.id(R.id.blue_layout).checked(false);
                baseClass.setThemePreference(R.style.AppThemeBlue);
                StartActivity();
            }
        });
        return view;
    }
    private void StartActivity(){
        getActivity().finish();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
