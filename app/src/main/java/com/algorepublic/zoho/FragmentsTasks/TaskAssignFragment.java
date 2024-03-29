package com.algorepublic.zoho.FragmentsTasks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.TaskUserModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskAssignee;
import com.algorepublic.zoho.adapters.TasksList;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.fragments.TaskAddUpdateFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskAssignFragment extends BaseFragment {

    static TaskAssignFragment fragment;
    AQuery aq;
    TaskListService service;
    BaseClass baseClass;
    public static ListView listView;
    static TasksList tasksList;
    public TaskAssignFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskAssignFragment newInstance(TasksList tasksLists) {
        tasksList = tasksLists;
        if (fragment==null) {
            fragment = new TaskAssignFragment();
        }
        return fragment;
    }
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
        // Inflate the layout for this fragment_forums
       View view =  inflater.inflate(R.layout.fragment_task_assign, container, false);
        listView = (ListView) view.findViewById(R.id.listview_employees);
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());

        service = new TaskListService(getActivity());
      if(tasksList == null) {
          service.getTaskAssignee(Integer.parseInt(baseClass.getSelectedProject()), false,
                  new CallBack(TaskAssignFragment.this, "TaskAssignee"));
      }else{
          service.getTaskAssignee(tasksList.getProjectID(), true,
                  new CallBack(TaskAssignFragment.this, "TaskAssignee"));
      }
            return view;
    }
    public void TaskAssignee(Object caller, Object model) {
        TaskUserModel.getInstance().setList((TaskUserModel) model);
        if (TaskUserModel.getInstance().responseCode == 100) {
            aq.id(R.id.alertMessage).text("No Users");
            if(TaskUserModel.getInstance().responseObject.size() ==0){
                aq.id(R.id.response_alert).visibility(View.VISIBLE);
            }else{
                aq.id(R.id.response_alert).visibility(View.GONE);
            }
            listView.setAdapter(new AdapterTaskAssignee(getActivity()));
        }
        else
        {
            Toast.makeText(getActivity(),getActivity().getString(R.string.response_error),Toast.LENGTH_SHORT).show();
         }
    }
}
