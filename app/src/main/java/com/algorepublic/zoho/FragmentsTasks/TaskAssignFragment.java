package com.algorepublic.zoho.FragmentsTasks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskAssignFragment extends BaseFragment {

    static TaskAssignFragment fragment;



    public TaskAssignFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskAssignFragment newInstance() {
        if (fragment==null) {
            fragment = new TaskAssignFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_assign, container, false);
    }

}
