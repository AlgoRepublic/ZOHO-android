package com.algorepublic.zoho.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.algorepublic.zoho.ActivityTask;
import com.algorepublic.zoho.Models.TasksListModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTasksList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.androidquery.AQuery;

/**
 * Created by android on 12/15/15.
 */
public class FragmentTasksList extends BaseFragment {

    static FragmentTasksList fragment;
    TaskListService taskListService;
    AdapterTasksList adapterTasksList;
    AQuery aq;

    public FragmentTasksList() {
    }

    @SuppressWarnings("unused")
    public static FragmentTasksList newInstance() {
        if (fragment==null) {
            fragment = new FragmentTasksList();
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
        View view = inflater.inflate(R.layout.fragment_taskslist, container, false);
        aq = new AQuery(view);
        taskListService = new TaskListService(getActivity());
        taskListService.getTasksList(true, new CallBack(FragmentTasksList.this, "TasksList"));
        aq.id(R.id.list_taskslist).itemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),ActivityTask.class);
                intent.putExtra("pos",position);
                startActivity(intent);
            }
        });
        return view;
    }
    public void TasksList(Object caller, Object model) {
        TasksListModel.getInstance().setList((TasksListModel) model);
        if (TasksListModel.getInstance().responseCode == 0) {
            adapterTasksList = new AdapterTasksList(getActivity());
            aq.id(R.id.list_taskslist).adapter(adapterTasksList);
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
}
