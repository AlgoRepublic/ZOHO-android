package com.algorepublic.zoho.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.algorepublic.zoho.ActivityTask;
import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.Models.TasksListModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTasksList;
import com.algorepublic.zoho.adapters.TaskListAssignee;
import com.algorepublic.zoho.adapters.TasksList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by android on 12/15/15.
 */
public class TasksListFragment extends BaseFragment {

    static TasksListFragment fragment;
    TaskListService taskListService;
    StickyListHeadersAdapter adapterTasksList;
    AQuery aq;View view;
    RadioGroup radioGroup;
    public static ArrayList<TasksList> allTaskList = new ArrayList<>();
    public static ArrayList<TasksList> generalList = new ArrayList<>();
    BaseClass baseClass;
    StickyListHeadersListView listView;

    public TasksListFragment() {
    }

    @SuppressWarnings("unused")
    public static TasksListFragment newInstance() {
        if (fragment==null) {
            fragment = new TasksListFragment();
        }
        return fragment;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_taskslist, container, false);
        MainActivity.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setToolbar();
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        listView = (StickyListHeadersListView) view.findViewById(R.id.list_taskslist);
        aq = new AQuery(view);

        baseClass = ((BaseClass) getActivity().getApplicationContext());
        setHasOptionsMenu(true);
        taskListService = new TaskListService(getActivity());
        if(allTaskList.size()==0) {
            taskListService.getTasksList(true, new CallBack(TasksListFragment.this, "TasksList"));
        }else{
            GetGeneralList();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callFragmentWithBackStack(R.id.container, TaskDetailFragment.newInstance(position),"TaskDetail");
            }
        });
        aq.id(R.id.add_task).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivityTask.class));
            }
        });
        aq.id(R.id.sort).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callForTaskSorting();
            }
        });
        aq.id(R.id.all).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoaded()) {
                    if(allTaskList.size()>0) {
                      GetGeneralList();
                    }
                }
            }
        });
        aq.id(R.id.up_coming).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoaded())
                UpComing();
            }
        });
        aq.id(R.id.over_due).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoaded())
                OverDueDate();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e("id","/"+radioGroup.indexOfChild(view.findViewById(checkedId)));
                switch (radioGroup.indexOfChild(view.findViewById(checkedId))) {
                    case 1:
                        aq.id(R.id.all).textColor(getResources().getColor(R.color.colorBaseHeader));
                        aq.id(R.id.up_coming).textColor(getResources().getColor(android.R.color.white));
                        aq.id(R.id.over_due).textColor(getResources().getColor(android.R.color.white));
                        break;
                    case 3:
                        aq.id(R.id.up_coming).textColor(getResources().getColor(R.color.colorBaseHeader));
                        aq.id(R.id.all).textColor(getResources().getColor(android.R.color.white));
                        aq.id(R.id.over_due).textColor(getResources().getColor(android.R.color.white));
                        break;
                    case 5:
                        aq.id(R.id.over_due).textColor(getResources().getColor(R.color.colorBaseHeader));
                        aq.id(R.id.all).textColor(getResources().getColor(android.R.color.white));
                        aq.id(R.id.up_coming).textColor(getResources().getColor(android.R.color.white));
                        break;
                }
            }
        });
        return view;
    }

    public void callForTaskSorting(){
        String[] menuItems = {"Due Date","Priority","Alphabetically","Task List"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(),menuItems, getView());
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (isLoaded())
                    if (position == 0) {
                        baseClass.setTaskSortType("DueDate");
                    }
                if (position == 1) {
                    baseClass.setTaskSortType("Priority");
                }
                if (position == 2) {
                    baseClass.setTaskSortType("Alphabetically");
                }
                if (position == 3) {
                    baseClass.setTaskSortType("TaskList");
                }
                SortList();
            }
        });
    }
    public void SortList(){
        if(baseClass.getTaskSortType().equalsIgnoreCase("DueDate")){
            Collections.sort(generalList, Date);
        }
        if(baseClass.getTaskSortType().equalsIgnoreCase("Priority")){
            Collections.sort(generalList, byPriority);
        }
        if(baseClass.getTaskSortType().equalsIgnoreCase("Alphabetically")){
            Collections.sort(generalList);
        }
        if(baseClass.getTaskSortType().equalsIgnoreCase("TaskList")){
            Collections.sort(generalList, byTaskList);
        }
        SetAdapterList();
    }
    public void SetAdapterList(){
        if (TasksListModel.getInstance().responseCode == 100) {
            adapterTasksList = new AdapterTasksList(getActivity());
            listView.setAreHeadersSticky(true);
            listView.setAdapter(adapterTasksList);
        }
    }
    public void TasksList(Object caller, Object model) {
        TasksListModel.getInstance().setList((TasksListModel) model);
        if (TasksListModel.getInstance().responseCode == 100) {
            AddAllTasks();
            GetGeneralList();
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
    public void GetGeneralList(){
        generalList.clear();
        generalList.addAll(allTaskList);
        SortList();
    }
    public void AddAllTasks(){
        allTaskList.clear();
        for (int loop = 0; loop < TasksListModel.getInstance().responseObject.size(); loop++) {
            TasksList tasksList = new TasksList();
            if(TasksListModel.getInstance().responseObject.get(loop).title== null){
                tasksList.setTaskName("-");
            }else {
                tasksList.setTaskName(TasksListModel.getInstance().responseObject.get(loop).title);
            }
            tasksList.setTaskID(TasksListModel.getInstance().responseObject.get(loop).taskID);
            tasksList.setEndMilli(DateMilli(TasksListModel.getInstance().responseObject.get(loop).endDate));
            tasksList.setStartMilli(DateMilli(TasksListModel.getInstance().responseObject.get(loop).startDate));
            tasksList.setProjectName(TasksListModel.getInstance().responseObject.get(loop).projectName);
            tasksList.setStartDate(DateFormatter(TasksListModel.getInstance().responseObject.get(loop).startDate));
            tasksList.setEndDate(DateFormatter(TasksListModel.getInstance().responseObject.get(loop).endDate));
            tasksList.setHeader(DateFormatter(TasksListModel.getInstance().responseObject.get(loop).endDate));
            tasksList.setPriority(TasksListModel.getInstance().responseObject.get(loop).priority);
            tasksList.setProgress(TasksListModel.getInstance().responseObject.get(loop).progress);
            tasksList.setTaskListName(TasksListModel.getInstance().responseObject.get(loop).taskListName);
            tasksList.setCharToAscii(CharToASCII(TasksListModel.getInstance().responseObject.get(loop).taskListName));
            //************** Assignee List ************//
            ArrayList<TaskListAssignee> listAssignees = new ArrayList<>();
            for(int loop1=0;loop1<TasksListModel.getInstance().responseObject.get(loop).userObject.size();loop1++){
                TaskListAssignee assignee = new TaskListAssignee();
                assignee.setUserID(TasksListModel.getInstance().responseObject.get(loop).userObject.get(loop1).responsibleID);
                assignee.setFirstName(TasksListModel.getInstance().responseObject.get(loop).userObject.get(loop1).firstName);
                assignee.setLastName(TasksListModel.getInstance().responseObject.get(loop).userObject.get(loop1).lastName);
                listAssignees.add(assignee);
            }
            tasksList.setListAssignees(listAssignees);
            //************** ******* ************//
            allTaskList.add(tasksList);
        }
    }
    public void UpComing(){
        generalList.clear();
        for (int loop = 0; loop < allTaskList.size(); loop++) {
            if(Long.parseLong(allTaskList.get(loop).getStartMilli()) > System.currentTimeMillis()) {
                generalList.add(allTaskList.get(loop));
            }
        }
        Collections.sort(generalList,byUpComingDate);
        SortList();
    }
    public void OverDueDate(){
        generalList.clear();
        for (int loop = 0; loop < allTaskList.size(); loop++) {
            if(Long.parseLong(allTaskList.get(loop).getEndMilli()) < System.currentTimeMillis()) {
                generalList.add(allTaskList.get(loop));
            }
        }
        Collections.sort(generalList,byOverDate);
        SortList();
    }
    Comparator<TasksList> byPriority = new Comparator<TasksList>() {
        public int compare(TasksList lhs, TasksList rhs) {
            return (Double.valueOf(rhs.getPriority()).compareTo(Double.valueOf(lhs.getPriority())));
        }
    };
    Comparator<TasksList> byTaskList = new Comparator<TasksList>() {
        public int compare(TasksList lhs, TasksList rhs) {
            return (Double.valueOf(lhs.getCharToAscii()).compareTo(Double.valueOf(rhs.getCharToAscii())));
        }
    };
    Comparator<TasksList> Date = new Comparator<TasksList>() {
        public int compare(TasksList lhs, TasksList rhs) {
            return (Double.valueOf(lhs.getEndMilli()).compareTo(Double.valueOf(rhs.getEndMilli())));
        }
    };
    Comparator<TasksList> byUpComingDate = new Comparator<TasksList>() {

        public int compare(TasksList ord1, TasksList ord2) {
            return (Double.valueOf(ord1.getStartMilli()).compareTo(Double.valueOf(String.valueOf(System.currentTimeMillis()))));     //descending
        }
    };
    Comparator<TasksList> byOverDate = new Comparator<TasksList>() {

        public int compare(TasksList ord1, TasksList ord2) {
            return (Double.valueOf(ord1.getEndMilli()).compareTo(Double.valueOf(String.valueOf(System.currentTimeMillis()))));     //descending
        }
    };
    public boolean isLoaded(){
        Boolean isloading=false;
        if(allTaskList.size()==0)
            isloading = false;
        else
            isloading= true;

        return isloading;
    }
}
