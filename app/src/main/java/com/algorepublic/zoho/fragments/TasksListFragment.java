package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.algorepublic.zoho.Models.TasksListByOwnerModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTasksList;
import com.algorepublic.zoho.adapters.TaskListAssignee;
import com.algorepublic.zoho.adapters.TaskListName;
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
public class TasksListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    static TasksListFragment fragment;
    TaskListService taskListService;
    StickyListHeadersAdapter adapterTasksList;
    AQuery aq;View view;int Color;
    RadioGroup radioGroup;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static ArrayList<TaskListName> taskListName = new ArrayList<>();
    public static ArrayList<TasksList> allTaskList = new ArrayList<>();
    public static ArrayList<TasksList> generalList = new ArrayList<>();
    BaseClass baseClass;
    StickyListHeadersListView listView;

    public TasksListFragment() {
    }

    @SuppressWarnings("unused")
    public static TasksListFragment newInstance() {
        fragment = new TasksListFragment();
        return fragment;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getToolbar().setTitle(getString(R.string.tasks));
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_tasklist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_project:
                if(baseClass.getSelectedProject().equalsIgnoreCase("0")){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.select_project), Toast.LENGTH_SHORT).show();
                }else {
                    callFragmentWithBackStack(R.id.container, TaskAddUpdateFragment.newInstance(taskListName), "Add Task");
                }
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onResume() {
        super.onResume();
        getToolbar().setTitle(getString(R.string.tasks));
    }

    @Override
    public void onRefresh() {
        if(baseClass.getSelectedProject().equalsIgnoreCase("0")) {
            taskListService.getTasksListByOwner(baseClass.getUserId(), false,
                    new CallBack(TasksListFragment.this, "OwnerTasksList"));
        }else{
            taskListService.getTasksListByProject(baseClass.getSelectedProject(), false,
                    new CallBack(TasksListFragment.this, "OwnerTasksList"));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_taskslist, container, false);
        // check if has permissions to add New Project
        if(baseClass.hasPermission(getResources().getString(R.string.tasks_add)))
            setHasOptionsMenu(true);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        listView = (StickyListHeadersListView) view.findViewById(R.id.list_taskslist);
        listView.setFastScrollEnabled(true);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        aq = new AQuery(view);
        InitializeDialog(getActivity());
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        if(baseClass.getThemePreference() == R.style.AppThemeBlue) {
            Color = android.graphics.Color.parseColor("#4B7BAA");
        }else{
            Color = android.graphics.Color.parseColor("#414042");
        }
        applyLightBackground(aq.id(R.id.layout_bottom).getView(), baseClass);
        taskListService = new TaskListService(getActivity());
        requestApiBasedOnPermission();
        aq.id(R.id.all).checked(true);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconifiedByDefault(true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
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
    /**
     * If User Doesn't have permissions to view Tasks
     * request will not be send and error msg will show
     */
    public void requestApiBasedOnPermission(){
        if(baseClass.hasPermission(getResources().getString(R.string.tasks_view))){
            if(baseClass.getSelectedProject().equalsIgnoreCase("0")) {
                taskListService.getTasksListByOwner(baseClass.getUserId(), true,
                        new CallBack(TasksListFragment.this, "OwnerTasksList"));
            }else{
                taskListService.getTasksListByProject(baseClass.getSelectedProject(), true,
                        new CallBack(TasksListFragment.this, "OwnerTasksList"));
            }
        }else {
            aq.id(R.id.layout_bottom).visibility(View.GONE);
            aq.id(R.id.response_alert).visibility(View.VISIBLE);
            aq.id(R.id.alertMessage).text("You don't have permissions to view Tasks.");
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(false);
        }
    }
    public void SortList(){
        if(baseClass.getTaskSortType().equalsIgnoreCase(getString(R.string.all))){
            GetGeneralList();
        }
        if(baseClass.getTaskSortType().equalsIgnoreCase(getString(R.string.upcoming))){
            UpComing();
        }
        if(baseClass.getTaskSortType().equalsIgnoreCase(getString(R.string.overdue))){
            OverDueDate();
        }
        FilterList();
    }
    public void callForTaskSorting(){
        String[] menuItems = {getString(R.string.due_date),getString(R.string.priority),
                getString(R.string.alphabetically),getString(R.string.task_list)};
        final ActionSheetDialog dialog = new ActionSheetDialog(
                getActivity(),menuItems,getString(R.string.cancel), getView());
        dialog.titleTextColor(Color);
        dialog.itemTextColor(Color);
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
//        ArrayList<TasksList> lists = new ArrayList<>();
//        lists.addAll(generalList);generalList.clear();
//        for(int loop=0;loop<lists.size();loop++){
//            if(lists.get(loop).getProgress()<100){
//                generalList.add(lists.get(loop));
//            }
//        }
        if(baseClass.getTaskFilterType().equalsIgnoreCase("DueDate")){
            Collections.sort(generalList, Date);
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
        if (TasksListByOwnerModel.getInstance().responseCode == 100) {
            aq.id(R.id.alertMessage).text(getString(R.string.no_tasks));
            if(generalList.size() ==0){
                aq.id(R.id.response_alert).visibility(View.VISIBLE);
            }else{
                aq.id(R.id.response_alert).visibility(View.GONE);
            }
            adapterTasksList = new AdapterTasksList(getActivity(),generalList,taskListName);
            listView.setAreHeadersSticky(true);
            listView.setAdapter(adapterTasksList);
        }
    }
    public void OwnerTasksList(Object caller, Object model) {
        swipeRefreshLayout.setRefreshing(false);
        TasksListByOwnerModel.getInstance().setList((TasksListByOwnerModel) model);
        if (TasksListByOwnerModel.getInstance().responseCode == 100) {
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
        FilterList();
    }
    public void AddAllTasks(){
        allTaskList.clear();taskListName.clear();
        for (int loop = 0; loop < TasksListByOwnerModel.getInstance().responseObject.size(); loop++) {
            TaskListName tasklistName = new TaskListName();
            tasklistName.setTaskListID(TasksListByOwnerModel.getInstance().responseObject.get(loop).tasklistID);
            tasklistName.setTaskListName(TasksListByOwnerModel.getInstance().responseObject.get(loop).taskListName);
            taskListName.add(tasklistName);
            for (int loop1 = 0; loop1 < TasksListByOwnerModel.getInstance().responseObject.get(loop).taskObject.size(); loop1++) {
                TasksListByOwnerModel.Tasks taskModel = TasksListByOwnerModel.getInstance().responseObject.get(loop).taskObject.get(loop1);
                TasksList tasksList = new TasksList();
                if (taskModel.title == null) {
                    tasksList.setTaskName("-");
                } else {
                    tasksList.setTaskName(taskModel.title.substring(0, 1).toUpperCase() + taskModel.title.substring(1));
                }
                tasksList.setTaskID(taskModel.taskID);
                tasksList.setEndMilli(DateMilli(taskModel.endDate));
                tasksList.setStartMilli(DateMilli(taskModel.startDate));
                tasksList.setOwnerId(taskModel.ownerID);
                tasksList.setProjectName(taskModel.projectName);
                tasksList.setProjectID(TasksListByOwnerModel.getInstance().responseObject.get(loop).projectID);
                tasksList.setStartDate(DateFormatter(taskModel.startDate));
                tasksList.setEndDate(DateFormatter(taskModel.endDate));
                tasksList.setHeader(DateMilli(taskModel.endDate));
                if(taskModel.description == null){
                    tasksList.setDescription(getActivity().getString(R.string.n_a));
                }else {
                    tasksList.setDescription(taskModel.description);
                }
                tasksList.setPriority(taskModel.priority);
                tasksList.setProgress(taskModel.progress);
                tasksList.setParentTaskID(taskModel.parentTaskID);
                tasksList.setCommentsCount(taskModel.commentsCount);
                tasksList.setDocumentsCount(taskModel.documentsCount);
                tasksList.setSubTasksCount(taskModel.subTasksCount);
                tasksList.setTaskListName(TasksListByOwnerModel.getInstance().responseObject.get(loop).taskListName);
                tasksList.setTaskListNameID(TasksListByOwnerModel.getInstance().responseObject.get(loop).tasklistID);
                //************** Assignee List ************//
                ArrayList<TaskListAssignee> listAssignees = new ArrayList<>();
                for (int loop2 = 0; loop2 < taskModel.userObject.size(); loop2++) {
                    TasksListByOwnerModel.Users users = taskModel.userObject.get(loop2);
                    TaskListAssignee assignee = new TaskListAssignee();
                    assignee.setUserID(users.responsibleID);
                    assignee.setFirstName(users.firstName);
                    assignee.setLastName(users.lastName);
                    assignee.setProfileImage(users.profileImagePath);
                    listAssignees.add(assignee);
                }
                tasksList.setListAssignees(listAssignees);
                //************** ******* ************//
                boolean viewOwnUnAssigned = baseClass.hasPermission(getResources().getString(R.string.tasks_view_own_unassigned));
                boolean viewOtherUnAssigned = baseClass.hasPermission(getResources().getString(R.string.tasks_view_others_unassigned));
                tasksViewUnassigned(viewOwnUnAssigned,viewOtherUnAssigned,tasksList,listAssignees);
            }
        }
    }

    /**
     * Apply permissions on TasksList. add tasks in list if true
     * @param tasksList
     * @param listAssignees
     */
    private void tasksViewUnassigned(boolean viewOwnUnAssigned,boolean viewOtherUnAssigned,TasksList tasksList,ArrayList<TaskListAssignee> listAssignees){
        if(!viewOwnUnAssigned && !viewOtherUnAssigned){
        }
        else if(!viewOwnUnAssigned){ // listAssignees.size() < 1 means "UNASSIGNED"
                if(tasksList.getOwnerId()==Integer.parseInt(baseClass.getUserId()) &&  listAssignees.size() < 1){}
                   else allTaskList.add(tasksList);

        }
        else if(!viewOtherUnAssigned) { // listAssignees.size() < 1 means "UNASSIGNED"
                if((tasksList.getOwnerId() != Integer.parseInt(baseClass.getUserId())) && listAssignees.size() < 1) {}
                   else allTaskList.add(tasksList);

        }
        else
            allTaskList.add(tasksList);
    }


    public void UpComing(){
        generalList.clear();
        for (int loop = 0; loop < allTaskList.size(); loop++) {
            if(Long.parseLong(allTaskList.get(loop).getEndMilli()) > System.currentTimeMillis()
                    || baseClass.DateFormatter(allTaskList.get(loop).getEndMilli())
                    .equalsIgnoreCase(baseClass.DateFormatter(String.valueOf(System.currentTimeMillis())))) {
                if (!(allTaskList.get(loop).getEndMilli().equalsIgnoreCase("62135535600000")
                        || allTaskList.get(loop).getEndMilli().equalsIgnoreCase("-62135571600000")
                        || allTaskList.get(loop).getEndMilli().equalsIgnoreCase("62135571600000"))) {
                    if(allTaskList.get(loop).getProgress()<100) {
                        allTaskList.get(loop).setHeader(allTaskList.get(loop).getEndMilli());
                        generalList.add(allTaskList.get(loop));
                    }
                }
            }
        }
        Collections.sort(generalList,byUpComingDate);
    }
    public void OverDueDate(){
        generalList.clear();
        for (int loop = 0; loop < allTaskList.size(); loop++) {
            if(Long.parseLong(allTaskList.get(loop).getEndMilli()) < System.currentTimeMillis()
                    && !baseClass.DateFormatter(allTaskList.get(loop).getStartMilli())
                    .equalsIgnoreCase(baseClass.DateFormatter(String.valueOf(System.currentTimeMillis())))) {
                if(allTaskList.get(loop).getProgress()<100) {
                    allTaskList.get(loop).setHeader(allTaskList.get(loop).getEndMilli());
                    generalList.add(allTaskList.get(loop));
                }
            }
        }
        Collections.sort(generalList,byOverDate);
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
