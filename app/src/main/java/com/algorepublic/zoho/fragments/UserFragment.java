package com.algorepublic.zoho.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.ActivityTask;
import com.algorepublic.zoho.R;
import com.androidquery.AQuery;

/**
 * Created by waqas on 2/8/16.
 */
public class UserFragment extends BaseFragment {


    AQuery aq;


    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_forums
        View view  = inflater.inflate(R.layout.fragment_calendar, container, false);
//        MainActivity.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        setToolbar();
        aq = new AQuery(getActivity(), view);
        setHasOptionsMenu(true);
        getToolbar().setTitle(getString(R.string.user));
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_project:
                startActivity(new Intent(getActivity(), ActivityTask.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
