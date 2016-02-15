package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by waqas on 2/15/16.
 */
public class AddUserFragment extends BaseFragment{

    AQuery aq;
    BaseClass baseClass;

    public AddUserFragment() {
        // Required empty public constructor
    }


    public static AddUserFragment newInstance() {
        AddUserFragment fragment = new AddUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.add_user, container, false);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        aq = new AQuery(getActivity(), view);
        setHasOptionsMenu(true);
        getToolbar().setTitle(getString(R.string.add_user));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
