package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.R;

/**
 * Created by android on 12/15/15.
 */
public class TasksFragment extends BaseFragment {

    static TasksFragment fragment;



    public TasksFragment() {
    }

    @SuppressWarnings("unused")
    public static TasksFragment newInstance() {
        if (fragment==null) {
            fragment = new TasksFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasks_fragment, container, false);

        return view;
    }
}
