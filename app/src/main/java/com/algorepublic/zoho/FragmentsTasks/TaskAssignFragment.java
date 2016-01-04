package com.algorepublic.zoho.FragmentsTasks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.TaskAssigneeModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapteTaskAssignee;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.androidquery.AQuery;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskAssignFragment extends BaseFragment {

    static TaskAssignFragment fragment;
    AQuery aq;
    TaskListService service;

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
        service = new TaskListService(getActivity());
        service.getTaskAssignee(4, true, new CallBack(TaskAssignFragment.this, "TaskAssignee"));
        return  view;
    }
    public void TaskAssignee(Object caller, Object model) {
        TaskAssigneeModel.getInstance().setList((TaskAssigneeModel) model);
        if (TaskAssigneeModel.getInstance().responseCode == 100) {
            aq.id(R.id.listview_employees).adapter(new AdapteTaskAssignee(getActivity()));
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
}
