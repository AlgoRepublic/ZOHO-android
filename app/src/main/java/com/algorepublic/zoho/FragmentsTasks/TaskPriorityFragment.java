package com.algorepublic.zoho.FragmentsTasks;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapteAssignEmployees;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskPriorityFragment extends BaseFragment {

    static TaskPriorityFragment fragment;
    AQuery aq;


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
        View view =  inflater.inflate(R.layout.fragment_task_priority, container, false);
        aq = new AQuery(view);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("None");
        arrayList.add("Low");
        arrayList.add("Medium");
        arrayList.add("High");
        aq.id(R.id.listview_priority).adapter(new AdapteAssignEmployees(getActivity(),arrayList));
        return view;
    }

}
