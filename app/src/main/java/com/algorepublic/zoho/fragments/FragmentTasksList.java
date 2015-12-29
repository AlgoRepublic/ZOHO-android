package com.algorepublic.zoho.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.algorepublic.zoho.ActivityTask;
import com.algorepublic.zoho.Models.TasksListModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTasksList;
import com.algorepublic.zoho.adapters.ChildTasksList;
import com.algorepublic.zoho.adapters.HeaderTasksList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by android on 12/15/15.
 */
public class FragmentTasksList extends BaseFragment {

    static FragmentTasksList fragment;
    TaskListService taskListService;
    StickyListHeadersAdapter adapterTasksList;
    AQuery aq;
    public static ArrayList<HeaderTasksList> headerTasksLists;
    public static ArrayList<ChildTasksList> list = new ArrayList<>();
    BaseClass baseClass;
    StickyListHeadersListView listView;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tasklist, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taskslist, container, false);
        listView = (StickyListHeadersListView) view.findViewById(R.id.list_taskslist);
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        setHasOptionsMenu(true);
        taskListService = new TaskListService(getActivity());
        taskListService.getTasksList(true, new CallBack(FragmentTasksList.this, "TasksList"));
        aq.id(R.id.list_taskslist).itemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ActivityTask.class);
                intent.putExtra("pos", position);
                startActivity(intent);
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
                SortList();
            }
        });
        aq.id(R.id.up_coming).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            UpComing();
            }
        });
        aq.id(R.id.over_due).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
    public void SetAdapterList(){
        if (TasksListModel.getInstance().responseCode == 0) {
            adapterTasksList = new AdapterTasksList(getActivity());
            listView.setAreHeadersSticky(false);
            listView.setAdapter(adapterTasksList);
        }
    }
    public void callForTaskSorting(){
        ArrayList<DialogMenuItem> menuItems = new ArrayList<>();
        DialogMenuItem dialogMenuItem1 = new DialogMenuItem("Due Date",R.drawable.calendar);
        DialogMenuItem dialogMenuItem2 = new DialogMenuItem("Priority",R.drawable.alert);
        DialogMenuItem dialogMenuItem3 = new DialogMenuItem("Alphabetically",android.R.drawable.ic_menu_sort_alphabetically);
        DialogMenuItem dialogMenuItem4 = new DialogMenuItem("Task List",R.drawable.task);
        menuItems.add(dialogMenuItem1);
        menuItems.add(dialogMenuItem2);
        menuItems.add(dialogMenuItem3);
        menuItems.add(dialogMenuItem4);
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(),menuItems, getView());
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("pos", "/" + position);
                dialog.dismiss();
                if (position == 0) {
                    baseClass.setSortType("Due Date");
                }
                if (position == 1) {
                    baseClass.setSortType("Priority");
                }
                if (position == 2) {
                    baseClass.setSortType("Alphabetically");
                }
                if (position == 3) {
                    baseClass.setSortType("Task List");
                }
                SortList();
            }
        });
    }
    public void SortList(){
        if(baseClass.getSortType().equalsIgnoreCase("Due Date")){
            GetGeneralList();
        }
        if(baseClass.getSortType().equalsIgnoreCase("Priority")){

        }
        if(baseClass.getSortType().equalsIgnoreCase("Alphabetically")){

        }
        if(baseClass.getSortType().equalsIgnoreCase("Task List")){

        }
        SetAdapterList();
    }
    public void TasksList(Object caller, Object model) {
        TasksListModel.getInstance().setList((TasksListModel) model);
        if (TasksListModel.getInstance().responseCode == 0) {
            SortList();
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
    public void GetGeneralList(){
//        headerTasksLists = new ArrayList<>();
//        for(int loop=0;loop<TasksListModel.getInstance().responseObject.size();loop++){
////            HeaderTasksList headerTasksList = null;
//
//           // if(loop < headerTasksLists.size()) {
//                if (!headerTasksLists..contains(DateFormatter(TasksListModel.getInstance().responseObject.get(loop).startDate))) {
//                    HeaderTasksList headerTasksList = new HeaderTasksList();
//                    headerTasksList.setHeaderID(loop);
//                    headerTasksList.setHeader(DateFormatter(TasksListModel.getInstance().responseObject.get(loop).startDate));
//                    headerTasksLists.add(headerTasksList);
//                }
//           // }
//        }
        AddChild();
    }
    public void AddChild(){
//        for(int loop1=0;loop1<headerTasksLists.size();loop1++) {
            ArrayList<ChildTasksList> childTasksLists = new ArrayList<>();
            for (int loop2 = 0; loop2 < TasksListModel.getInstance().responseObject.size(); loop2++) {
              //  if (headerTasksLists.get(loop2).getHeader().equalsIgnoreCase(DateFormatter(TasksListModel.getInstance().responseObject.get(loop2).startDate))) {
                    ChildTasksList tasksList = new ChildTasksList();
                    tasksList.setTaskName(TasksListModel.getInstance().responseObject.get(loop2).title);
                    tasksList.setMilli(DateMilli(TasksListModel.getInstance().responseObject.get(loop2).startDate));
                    tasksList.setProjectName(TasksListModel.getInstance().responseObject.get(loop2).projectName);
                    tasksList.setStartDate(DateFormatter(TasksListModel.getInstance().responseObject.get(loop2).startDate));
                    tasksList.setEndDate(DateFormatter(TasksListModel.getInstance().responseObject.get(loop2).endDate));
                    childTasksLists.add(tasksList);
                    list.add(tasksList);
                }
            Collections.sort(list);
//            }
//            headerTasksLists.get(loop1).setChildList(childTasksLists);
       // }
    }
    public void UpComing(){
        SetAdapterList();
    }
    Comparator<ChildTasksList> byDate = new Comparator<ChildTasksList>() {

        public int compare(ChildTasksList ord1, ChildTasksList ord2) {
            return (Long.parseLong(ord1.getMilli()) > System.currentTimeMillis() ? -1 : 1);     //descending
            //  return (d1.getTime() > d2.getTime() ? 1 : -1);     //ascending
        }
    };
    public String DateFormatter(String date){
        String a = date.replace("/Date(", "");
        String b =  a.replace(")/", "");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(b));

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        return (mDay+"/"+mMonth+"/"+mYear);
    }
    public String DateMilli(String date) {
        String a = date.replace("/Date(", "");
        String b = a.replace(")/", "");
        return b;
    }
}
