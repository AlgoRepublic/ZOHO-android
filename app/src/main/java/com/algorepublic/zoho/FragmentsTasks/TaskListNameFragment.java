package com.algorepublic.zoho.FragmentsTasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskListName;
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
        View view =  inflater.inflate(R.layout.fragment_tasklistame, container, false);
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        aq.id(R.id.listview_tasklistname).itemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                baseClass.db.putInt("TaskListNameID", TasksListFragment.taskListName.get(position).getTaskListID());
                adapter.setSelectedIndex(position);
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new AdapterTaskListName(getActivity());
        adapter.setSelectedIndex(baseClass.db.getInt("TaskListNameID"));
        aq.id(R.id.listview_tasklistname).adapter(adapter);
        return view;
    }
}
