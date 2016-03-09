package com.algorepublic.zoho.FragmentsTasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskListName;
import com.algorepublic.zoho.adapters.TasksList;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.fragments.TasksListFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by android on 2/9/16.
 */
public class TaskListNameFragment extends BaseFragment {

    static TaskListNameFragment fragment;
    AdapterTaskListName adapter;
    AQuery aq;
    public static ListView listView;
    BaseClass baseClass;
    public TaskListNameFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskListNameFragment newInstance() {
        if (fragment==null) {
            fragment = new TaskListNameFragment();
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
        View view =  inflater.inflate(R.layout.fragment_tasklistame, container, false);
        listView = (ListView) view.findViewById(R.id.listview_tasklistname);
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        adapter = new AdapterTaskListName(getActivity());
        for(int loop=0;loop<TasksListFragment.taskListName.size();loop++) {
            if (baseClass.db.getInt("TaskListNameID") ==
                    TasksListFragment.taskListName.get(loop).getTaskListID()) {
                adapter.setSelectedIndex(loop);
            }
        }
       listView.setAdapter(adapter);
        return view;
    }
}
