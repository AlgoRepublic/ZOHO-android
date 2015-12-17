package com.algorepublic.zoho.FragmentsTasks;

import android.content.Context;
import android.net.Uri;
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
public class TaskPriorityFragment extends BaseFragment {

    static TaskPriorityFragment fragment;



    public TaskPriorityFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskPriorityFragment newInstance() {
        if (fragment==null) {
            fragment = new TaskPriorityFragment();
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
        return inflater.inflate(R.layout.fragment_task_priority, container, false);
    }

}
