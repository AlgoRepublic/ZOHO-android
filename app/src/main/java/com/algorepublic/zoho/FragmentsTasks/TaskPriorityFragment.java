package com.algorepublic.zoho.FragmentsTasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskPriority;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskPriorityFragment extends BaseFragment {

    static TaskPriorityFragment fragment;
    AdapterTaskPriority adapter;
    AQuery aq;
    BaseClass baseClass;
    public static int position;
    public static ListView listView;
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
        // Inflate the layout for this fragment_forums
        View view =  inflater.inflate(R.layout.fragment_task_priority, container, false);
        listView = (ListView) view.findViewById(R.id.listview_priority);
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(getString(R.string.none));
        arrayList.add(getString(R.string.low));
        arrayList.add(getString(R.string.medium));
        arrayList.add(getString(R.string.high));
        adapter = new AdapterTaskPriority(getActivity(),listView, arrayList);
        adapter.setSelectedIndex(baseClass.db.getInt("Priority"));
        listView.setAdapter(adapter);
        return view;
    }
}
