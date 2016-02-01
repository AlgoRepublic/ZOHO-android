package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by waqas on 2/1/16.
 */
public class ForumsFragment extends BaseFragment {

    private AQuery aq;
    private BaseClass baseClass;

    public static ForumsFragment newInstance() {
        ForumsFragment fragment = new ForumsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_forums, container, false);
        aq = new AQuery(getActivity(), view);
        MainActivity.toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        return view;
    }

    public void setToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(BaseActivity.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), BaseActivity.drawer, BaseActivity.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        BaseActivity.drawer.setDrawerListener(toggle);
        toggle.syncState();
    }
}
