package com.algorepublic.zoho.FragmentsTasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;

/**
 * Created by android on 12/17/15.
 */
public class TaskEditTitleFragment extends BaseFragment {

    static TaskEditTitleFragment fragment;



    public TaskEditTitleFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskEditTitleFragment newInstance() {
        if (fragment==null) {
            fragment = new TaskEditTitleFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_title_tasks, container, false);

        return view;
    }
}
