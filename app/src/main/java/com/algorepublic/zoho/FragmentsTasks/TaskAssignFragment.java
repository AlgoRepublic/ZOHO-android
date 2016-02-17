package com.algorepublic.zoho.FragmentsTasks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.fragments.TaskAddUpdateFragment;
import com.algorepublic.zoho.Models.TaskAssigneeModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskAssignee;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.fragments.TasksListFragment;
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
    public static int position;
    BaseClass baseClass;
    public TaskAssignFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskAssignFragment newInstance(int pos) {
        position = pos;
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
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
     if(TaskAddUpdateFragment.assigneeList.size()==0){
         try {
             for (int loop = 0;
                  loop < TasksListFragment.generalList.get(position).getListAssignees().size(); loop++) {
                 if (TasksListFragment.generalList.get(position).getListAssignees().get(loop).getUserID() != -1) {
                     TaskAddUpdateFragment.assigneeList.add(
                             TasksListFragment.generalList.get(position).getListAssignees().get(loop).getUserID());
                 } else
                     TaskAddUpdateFragment.assigneeList.add(-1);
             }
         }catch (IndexOutOfBoundsException e){}
        }

        service = new TaskListService(getActivity());
      if(position == -1) {
          service.getTaskAssignee(Integer.parseInt(baseClass.getSelectedProject()), true, new CallBack(TaskAssignFragment.this, "TaskAssignee"));
      }else{
          service.getTaskAssignee(TasksListFragment.generalList.get(position).getProjectID(), true, new CallBack(TaskAssignFragment.this, "TaskAssignee"));
      }
            return  view;
    }
    public void TaskAssignee(Object caller, Object model) {
        TaskAssigneeModel.getInstance().setList((TaskAssigneeModel) model);
        if (TaskAssigneeModel.getInstance().responseCode == 100) {
            aq.id(R.id.listview_employees).adapter(new AdapterTaskAssignee(getActivity()));
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
}
