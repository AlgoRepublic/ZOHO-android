package com.algorepublic.zoho.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.TaskListBySubTaskModel;
import com.algorepublic.zoho.Models.TasksListByOwnerModel;
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
 * Created by android on 2/10/16.
 */
@SuppressLint("ValidFragment")
public class TaskListBySubTasksFragment extends BaseFragment {

    static TaskListBySubTasksFragment fragment;
    TaskListService taskListService;
    StickyListHeadersAdapter adapterTasksList;
    AQuery aq;View view;
    RadioGroup radioGroup;
    public static ArrayList<TasksList> allTaskList = new ArrayList<>();
    public static ArrayList<TasksList> generalList = new ArrayList<>();
    BaseClass baseClass;
    StickyListHeadersListView listView;
    static TasksList tasksList;
    @SuppressLint("ValidFragment")
    public TaskListBySubTasksFragment (TasksList list) {
        tasksList = list;
    }
//    @SuppressWarnings("unused")
//    public static TaskListBySubTasksFragment newInstance(int Id) {
//        ID = Id;
//        fragment = new TaskListBySubTasksFragment();
//        return fragment;
//    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setRetainInstance(true);
        getToolbar().setTitle(getString(R.string.sub_tasks));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tasklist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_project:
                baseClass.hideKeyPad(getView());
                if(baseClass.db.getInt("ProjectID") == 0){
                    Snackbar.make(getView(),getString(R.string.select_project),Snackbar.LENGTH_SHORT).show();
                }else {
                    callFragmentWithBackStack(R.id.container, TaskAddUpdateFragment.newInstance(tasksList.getTaskID()),"TaskAddUpdateFragment");
                }
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_taskslist, container, false);
        setHasOptionsMenu(true);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        listView = (StickyListHeadersListView) view.findViewById(R.id.list_taskslist);
        aq = new AQuery(view);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconifiedByDefault(true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
            }
        });
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        taskListService = new TaskListService(getActivity());
        taskListService.getTasksListBySubTasks(tasksList.getTaskID(), true, new CallBack(TaskListBySubTasksFragment.this, "TaskListBySubTasks"));

        aq.id(R.id.add_task).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TaskAddUpdateFragment.class));
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
                        baseClass.setTaskSortType("All");
                        GetGeneralList();
                    }
                }
            }
        });
        aq.id(R.id.up_coming).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoaded()) {
                    baseClass.setTaskSortType("UpComing");
                    SortList();
                }
            }
        });
        aq.id(R.id.over_due).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoaded()) {
                    baseClass.setTaskSortType("OverDue");
                    SortList();
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e("id", "/" + radioGroup.indexOfChild(view.findViewById(checkedId)));
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
    public void SortList(){
        if(baseClass.getTaskSortType().equalsIgnoreCase("All")){
            GetGeneralList();
        }
        if(baseClass.getTaskSortType().equalsIgnoreCase("UpComing")){
            UpComing();
        }
        if(baseClass.getTaskSortType().equalsIgnoreCase("OverDue")){
            OverDueDate();
        }
        FilterList();
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
                        baseClass.setTaskFilterType("DueDate");
                    }
                if (position == 1) {
                    baseClass.setTaskFilterType("Priority");
                }
                if (position == 2) {
                    baseClass.setTaskFilterType("Alphabetically");
                }
                if (position == 3) {
                    baseClass.setTaskFilterType("TaskList");
                }
                SortList();
            }
        });
    }
    public void FilterList(){
        if(baseClass.getTaskFilterType().equalsIgnoreCase("DueDate")){
            Collections.sort(generalList, Date);
            ArrayList<TasksList> lists = new ArrayList<>();
            lists.addAll(generalList);generalList.clear();
            for(int loop=0;loop<lists.size();loop++){
                if(lists.get(loop).getProgress()<100){
                    generalList.add(lists.get(loop));
                }
            }
        }
        if(baseClass.getTaskFilterType().equalsIgnoreCase("Priority")){
            Collections.sort(generalList, byPriority);
        }
        if(baseClass.getTaskFilterType().equalsIgnoreCase("Alphabetically")){
            Collections.sort(generalList);
        }
        if(baseClass.getTaskFilterType().equalsIgnoreCase("TaskList")){
            Collections.sort(generalList, byTaskList);
        }
        SetAdapterList();
    }
    public void SetAdapterList(){
        if (TaskListBySubTaskModel.getInstance().responseCode == 100) {
            adapterTasksList = new AdapterTasksList(getActivity(),generalList);
            listView.setAreHeadersSticky(true);
            listView.setAdapter(adapterTasksList);
        }
    }
    public void TaskListBySubTasks(Object caller, Object model) {
        TaskListBySubTaskModel.getInstance().setList((TaskListBySubTaskModel) model);
        if (TaskListBySubTaskModel.getInstance().responseCode == 100) {
            AddAllTasks();
            GetGeneralList();
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    public void GetGeneralList(){
        generalList.clear();
        generalList.addAll(allTaskList);
        FilterList();
    }
    public void AddAllTasks(){
        allTaskList.clear();
        Log.e("E","/"+TaskListBySubTaskModel.getInstance().responseObject.size());
        for (int loop = 0; loop < TaskListBySubTaskModel.getInstance().responseObject.size(); loop++) {
            TaskListBySubTaskModel.ResponseObject taskModel = TaskListBySubTaskModel.getInstance().responseObject.get(loop);
            TasksList tasksList = new TasksList();
            if (taskModel.title == null) {
                tasksList.setTaskName("-");
            } else {
                tasksList.setTaskName(taskModel.title.substring(0, 1).toUpperCase() + taskModel.title.substring(1));
            }
            tasksList.setTaskID(taskModel.taskID);
            tasksList.setEndMilli(DateMilli(taskModel.endDate));
            tasksList.setStartMilli(DateMilli(taskModel.startDate));
            tasksList.setProjectName(taskModel.projectName);
            tasksList.setProjectID(taskModel.projectID);
            tasksList.setStartDate(DateFormatter(taskModel.startDate));
            tasksList.setEndDate(DateFormatter(taskModel.endDate));
            tasksList.setHeader(DateMilli(taskModel.endDate));
            tasksList.setDescription(taskModel.description);
            tasksList.setPriority(taskModel.priority);
            tasksList.setProgress(taskModel.progress);
            tasksList.setCommentsCount(taskModel.commentsCount);
            tasksList.setDocumentsCount(taskModel.documentsCount);
            tasksList.setSubTasksCount(taskModel.subTasksCount);
            tasksList.setTaskListName(taskModel.taskListName);
            tasksList.setParentTaskID(taskModel.parentTaskID);
            tasksList.setTaskListNameID(taskModel.tasklistID);
            //************** Assignee List ************//
            ArrayList<TaskListAssignee> listAssignees = new ArrayList<>();
            for (int loop1 = 0; loop1 < taskModel.userObject.size(); loop1++) {
                TaskListBySubTaskModel.Users users = taskModel.userObject.get(loop1);
                TaskListAssignee assignee = new TaskListAssignee();
                assignee.setUserID(users.responsibleID);
                assignee.setFirstName(users.firstName);
                assignee.setLastName(users.lastName);
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
            if(Long.parseLong(allTaskList.get(loop).getStartMilli()) > System.currentTimeMillis()
                    || baseClass.DateFormatter(allTaskList.get(loop).getStartMilli())
                    .equalsIgnoreCase(baseClass.DateFormatter(String.valueOf(System.currentTimeMillis())))) {
                if (!(allTaskList.get(loop).getStartMilli().equalsIgnoreCase("62135535600000")
                        || allTaskList.get(loop).getStartMilli().equalsIgnoreCase("-62135571600000")
                        || allTaskList.get(loop).getStartMilli().equalsIgnoreCase("62135571600000"))) {
                    allTaskList.get(loop).setHeader(allTaskList.get(loop).getStartMilli());
                    generalList.add(allTaskList.get(loop));
                }
            }
        }
        Collections.sort(generalList, byUpComingDate);
    }
    public void OverDueDate(){
        generalList.clear();
        for (int loop = 0; loop < allTaskList.size(); loop++) {
            if(Long.parseLong(allTaskList.get(loop).getEndMilli()) < System.currentTimeMillis()
                    && !baseClass.DateFormatter(allTaskList.get(loop).getStartMilli())
                    .equalsIgnoreCase(baseClass.DateFormatter(String.valueOf(System.currentTimeMillis())))) {
                allTaskList.get(loop).setHeader(allTaskList.get(loop).getEndMilli());
                generalList.add(allTaskList.get(loop));
            }
        }
        Collections.sort(generalList, byOverDate);
    }
    Comparator<TasksList> byPriority = new Comparator<TasksList>() {
        public int compare(TasksList lhs, TasksList rhs) {
            return (Double.valueOf(rhs.getPriority()).compareTo(Double.valueOf(lhs.getPriority())));
        }
    };
    Comparator<TasksList> byTaskList = new Comparator<TasksList>() {
        public int compare(TasksList lhs, TasksList rhs) {
            return (Double.valueOf(lhs.getTaskListNameID()).compareTo(Double.valueOf(rhs.getTaskListNameID())));
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
