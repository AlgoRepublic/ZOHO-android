package com.algorepublic.zoho.FragmentsTasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskListName;
import com.algorepublic.zoho.adapters.AdapterTaskPriority;
import com.algorepublic.zoho.adapters.AdapterTasksList;
import com.algorepublic.zoho.adapters.TaskListName;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.fragments.TasksListFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 2/9/16.
 */
public class TaskListNameFragment extends BaseFragment {

    static TaskListNameFragment fragment;
    AdapterTaskListName adapter;
    AQuery aq;
    BaseClass baseClass;
    public static int position;

    public TaskListNameFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskListNameFragment newInstance(int pos) {
        position = pos;
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
        View view =  inflater.inflate(R.layout.fragment_task_priority, container, false);
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        ArrayList<TaskListName> arrayList = new ArrayList<>();
        Log.e("Name","/"+TasksListFragment.taskListName.size());
        arrayList.addAll(TasksListFragment.taskListName);
        aq.id(R.id.listview_priority).itemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedIndex(position);
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new AdapterTaskListName(getActivity(), arrayList);
        aq.id(R.id.listview_priority).adapter(adapter);
        return view;
    }
}
