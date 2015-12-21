package com.algorepublic.zoho.FragmentsTasks;


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
public class TaskAssignFragment extends BaseFragment {

    static TaskAssignFragment fragment;
    AQuery aq;


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
       View view =  inflater.inflate(R.layout.fragment_task_assign, container, false);
        aq = new AQuery(view);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("General");
        arrayList.add("External");
        arrayList.add("Normal");
        aq.id(R.id.listview_employees).adapter(new AdapteAssignEmployees(getActivity(),arrayList));
        return  view;
    }

}
