package com.algorepublic.zoho.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.AllProjectsByUserModel;
import com.algorepublic.zoho.Models.DashBoardModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DashBoardService;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.dacer.androidcharts.BarView;
import com.dacer.androidcharts.PieHelper;
import com.dacer.androidcharts.PieView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by waqas on 3/10/16.
 */
public class DashboardFragment extends BaseFragment {

    DashBoardService service;ProjectsListService service1;
    AQuery aq;int taskClosed,taskListClosed,milestoneClosed;
    BaseClass baseClass;
    PieView pieGraph;
    BarView barGraph;
    int Color;
    static DashboardFragment fragment;

    public static DashboardFragment newInstance() {
        fragment = new DashboardFragment();
        return fragment;
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.dashboard));
//        getActivity().supportInvalidateOptionsMenu();
//    }
@Override
public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    getToolbar().setTitle(getString(R.string.dashboard));
    super.onViewCreated(view, savedInstanceState);
}
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        getToolbar().setTitle(getString(R.string.dashboard));
//        getActivity().supportInvalidateOptionsMenu();
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_forums
        View view  = inflater.inflate(R.layout.fragment_dashboard, container, false);

        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        if(baseClass.getThemePreference() == R.style.AppThemeBlue) {
           Color = android.graphics.Color.parseColor("#4B7BAA");
        }else {
            Color = android.graphics.Color.parseColor("#414042");
        }
        barGraph = (BarView)view.findViewById(R.id.line_view);
        pieGraph  = (PieView)view.findViewById(R.id.pie_view);
        service = new DashBoardService(getActivity());
        service1 = new ProjectsListService(getActivity());
        if(baseClass.getSelectedProject().equalsIgnoreCase("0")) {
            service1.getAllProjectsByUser_API(baseClass.getUserId(),
                    true, new CallBack(DashboardFragment.this, "AllProjects"));
        }else
        {
            service.getMileStone(baseClass.getSelectedProject(), true,
                    new CallBack(DashboardFragment.this, "DashBoardList"));
        }
        setHasOptionsMenu(true);
        getToolbar().setTitle(getString(R.string.dashboard));
        return view;
    }
    public void AllProjects(Object caller, Object model) {
        AllProjectsByUserModel.getInstance().setList((AllProjectsByUserModel) model);
        if (AllProjectsByUserModel.getInstance().responseCode == 100
                || AllProjectsByUserModel.getInstance().responseData.size() != 0) {
            int completedTasks=0,totalTasks =0;
            ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
            for(int loop=0;loop<AllProjectsByUserModel.getInstance().responseData.size();loop++) {
                AllProjectsByUserModel.Response object = AllProjectsByUserModel.getInstance()
                        .responseData.get(loop);
                completedTasks +=object.completedTasksNo;
                totalTasks +=object.totalTasks;
            }
            float closed = (Float.parseFloat("" + completedTasks) /
                    Float.parseFloat("" + totalTasks)) * 100;
            int opentask = totalTasks - completedTasks;
            float opened = ((Float.parseFloat("" + opentask))
                    / Float.parseFloat("" + totalTasks)) * 100;
            PieHelper pieHelper = new PieHelper(closed,
                    getString(R.string.closed_tasks), 0);
            PieHelper pieHelper1 = new PieHelper(opened,
                    getString(R.string.opened_tasks), 0);

            pieHelperArrayList.add(pieHelper1);
            pieHelperArrayList.add(pieHelper);
            pieGraph.selectedPie(PieView.NO_SELECTED_INDEX);
            pieGraph.showPercentLabel(true,Color);
            pieGraph.setDate(pieHelperArrayList);
            randomSet1();

        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.response_error), Toast.LENGTH_SHORT).show();

        }
    }
    public void DashBoardList(Object caller, Object model) {
        DashBoardModel.getInstance().setList((DashBoardModel) model);
        if (DashBoardModel.getInstance().responseCode == 100) {
            ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
            DashBoardModel.ResponseObject object = DashBoardModel.getInstance().responseObject;
            float closed = (Float.parseFloat(""+object.completedTasksNo)/
                    Float.parseFloat(""+object.totalTasksNo))*100;
            int opentask = object.totalTasksNo -  object.completedTasksNo;
            float opened = ((Float.parseFloat(""+opentask))
                    /Float.parseFloat(""+object.totalTasksNo))*100;
            PieHelper pieHelper = new PieHelper(closed,
                    getString(R.string.closed_tasks),0);
            PieHelper pieHelper1 = new PieHelper(opened,
                    getString(R.string.opened_tasks),0);
            pieHelperArrayList.add(pieHelper1);
            pieHelperArrayList.add(pieHelper);

            pieGraph.selectedPie(PieView.NO_SELECTED_INDEX);
            pieGraph.showPercentLabel(true,Color);
            pieGraph.setDate(pieHelperArrayList);
            randomSet();

        } else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
    private void randomSet1(){

        ArrayList<Integer> barDataList = new ArrayList<>();
        ArrayList<String> barDataList1 = new ArrayList<String>();
        int completedTasksNo=0,totalTasksNo=0,
                completedTaskList=0,totalTaskList=0,completedMilestonesNo=0,totalMilestonesNo=0;

        for(int loop=0;loop<AllProjectsByUserModel.getInstance().responseData.size();loop++) {
            AllProjectsByUserModel.Response object = AllProjectsByUserModel.getInstance()
                    .responseData.get(loop);
            completedTasksNo +=object.completedTasksNo;
            totalTasksNo +=object.totalTasks;
            completedTaskList +=object.completedTaskList;
            totalTaskList +=object.totalTaskList;
            completedMilestonesNo +=object.completedMilestonesNo;
            totalMilestonesNo +=object.toalMilestones;
        }
        if(completedTasksNo >0) {
            taskClosed = (int) (Float.parseFloat("" + completedTasksNo) /
                    Float.parseFloat("" + totalTasksNo) * 100);
        }
        if(completedTaskList >0) {
            taskListClosed = (int)(Float.parseFloat("" + completedTaskList)
                    / Float.parseFloat("" + totalTaskList) * 100);
        }
        if(completedMilestonesNo >0) {
            milestoneClosed = (int)(Float.parseFloat("" + completedMilestonesNo)
                    / Float.parseFloat("" + totalMilestonesNo) * 100);
        }
        aq.id(R.id.open1).text(getString(R.string.opened_tasks)+" "+(totalTasksNo-completedTasksNo));
        aq.id(R.id.close1).text(getString(R.string.closed_tasks)+" "+completedTasksNo);
        aq.id(R.id.open2).text(getString(R.string.opened_taskslist)+" "+(totalTaskList-completedTaskList));
        aq.id(R.id.close2).text(getString(R.string.closed_taskslists)+" "+completedTaskList);
        aq.id(R.id.open3).text(getString(R.string.opened_milestone)+" "+(totalMilestonesNo-completedMilestonesNo));
        aq.id(R.id.close3).text(getString(R.string.closed_milestone)+" "+completedMilestonesNo);
        barDataList.add(taskClosed);
        barDataList.add(taskListClosed);
        barDataList.add(milestoneClosed);
        barDataList1.add(getString(R.string.tasks));
        barDataList1.add(taskClosed+"%");
        barDataList1.add(getString(R.string.tasks_list));
        barDataList1.add(taskListClosed+"%");
        barDataList1.add(getString(R.string.milestones));
        barDataList1.add(milestoneClosed+"%");
        barGraph.setBottomTextList(barDataList1,Color);
        barGraph.setDataList(barDataList, 100);
    }
    private void randomSet(){

        ArrayList<Integer> barDataList = new ArrayList<>();
        ArrayList<String> barDataList1 = new ArrayList<String>();

        DashBoardModel.ResponseObject object = DashBoardModel.getInstance().responseObject;
        if(object.completedTasksNo >0) {
            taskClosed = (int)(Float.parseFloat("" + object.completedTasksNo) /
                    Float.parseFloat("" + object.totalTasksNo) * 100);
        }
        if(object.completedTaskList >0) {
            taskListClosed = (int) (Float.parseFloat("" + object.completedTaskList)
                    / Float.parseFloat("" + object.totalTaskList) * 100);
        }
        if(object.completedMilestonesNo >0) {
            milestoneClosed =(int) (Float.parseFloat("" + object.completedMilestonesNo)
                    / Float.parseFloat("" + object.totalMilestonesNo) * 100);
        }
        aq.id(R.id.open1).text(getString(R.string.opened_tasks)+" "+(object.totalTasksNo-object.completedTasksNo));
        aq.id(R.id.close1).text(getString(R.string.closed_tasks)+" "+object.completedTasksNo);
        aq.id(R.id.open2).text(getString(R.string.opened_taskslist)+" "+(object.totalTaskList-object.completedTaskList));
        aq.id(R.id.close2).text(getString(R.string.closed_taskslists)+" "+object.completedTaskList);
        aq.id(R.id.open3).text(getString(R.string.opened_milestone)+" "+(object.totalMilestonesNo-object.completedMilestonesNo));
        aq.id(R.id.close3).text(getString(R.string.closed_milestone)+" "+object.completedMilestonesNo);
        barDataList.add(taskClosed);
        barDataList.add(taskListClosed);
        barDataList.add(milestoneClosed);
        barDataList1.add(getString(R.string.tasks));
        barDataList1.add(taskClosed+"%");
        barDataList1.add(getString(R.string.tasks_list));
        barDataList1.add(taskListClosed+"%");
        barDataList1.add(getString(R.string.milestones));
        barDataList1.add(milestoneClosed+"%");
        barGraph.setBottomTextList(barDataList1,Color);
        barGraph.setDataList(barDataList, 100);
    }
}
