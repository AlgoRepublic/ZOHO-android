package com.algorepublic.zoho.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.R;
import com.androidquery.AQuery;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTaskDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTaskDetail extends BaseFragment {

    AQuery aq;
    static FragmentTaskDetail fragment;
    static int position;
    public FragmentTaskDetail() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentTaskDetail newInstance(int pos) {
        position =pos;
        if(fragment==null){
            fragment = new FragmentTaskDetail();
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
        View view =  inflater.inflate(R.layout.fragment_task_detail, container, false);
        aq = new AQuery(view);
        setPriority();
        aq.id(R.id.task_name).text(FragmentTasksList.generalList.get(position).getTaskName());
        aq.id(R.id.task_desc).text(FragmentTasksList.generalList.get(position).getTaskListName());

        return view;
    }

    public void setPriority(){
        aq.id(R.id.textView).backgroundColor(
                getPriorityWiseColor(FragmentTasksList.generalList.get(position).getPriority()));
        if(FragmentTasksList.generalList.get(position).getPriority()==0)
            aq.id(R.id.textView).text("None");
        if(FragmentTasksList.generalList.get(position).getPriority()==1)
            aq.id(R.id.textView).text("Low");
        if(FragmentTasksList.generalList.get(position).getPriority()==2)
            aq.id(R.id.textView).text("Medium");
        if(FragmentTasksList.generalList.get(position).getPriority()==3)
            aq.id(R.id.textView).text("High");
    }
}
