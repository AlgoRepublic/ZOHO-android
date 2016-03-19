package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.DashBoardModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DashBoardService;
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

    DashBoardService service;
    AQuery aq;float taskClosed,taskListClosed,milestoneClosed;
    BaseClass baseClass;
    PieView pieGraph;
    BarView barGraph;
    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getToolbar().setTitle(getString(R.string.dashboard));
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_forums
        View view  = inflater.inflate(R.layout.fragment_dashboard, container, false);
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        barGraph = (BarView)view.findViewById(R.id.line_view);
        pieGraph  = (PieView)view.findViewById(R.id.pie_view);
        service = new DashBoardService(getActivity());
        if(baseClass.getSelectedProject().equalsIgnoreCase("0")) {
            Toast.makeText(getActivity(), getString(R.string.select_project), Toast.LENGTH_SHORT).show();
        }else
        {
            service.getMileStone(baseClass.getSelectedProject(), true,
                    new CallBack(DashboardFragment.this, "DashBoardList"));
        }
        return view;
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
            pieGraph.showPercentLabel(true);
            //pieGraph.setTextSize(12);
            pieGraph.setDate(pieHelperArrayList);
            randomSet();

        } else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }

    private void randomSet(){

        ArrayList<Float> barDataList = new ArrayList<Float>();
        ArrayList<String> barDataList1 = new ArrayList<String>();

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        DashBoardModel.ResponseObject object = DashBoardModel.getInstance().responseObject;
        if(object.completedTasksNo >0) {
            taskClosed = (Float.parseFloat("" + object.completedTasksNo) /
                    Float.parseFloat("" + object.totalTasksNo)) * 100;
        }
        if(object.completedTaskList >0) {
            taskListClosed = (Float.parseFloat("" + object.completedTaskList))
                    / Float.parseFloat("" + object.totalTaskList) * 100;
        }
        if(object.completedMilestonesNo >0) {
            milestoneClosed = (Float.parseFloat("" + object.completedMilestonesNo))
                    / Float.parseFloat("" + object.totalMilestonesNo) * 100;
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
        barDataList1.add(decimalFormat.format(taskClosed)+"%");
        barDataList1.add(getString(R.string.tasks_list));
        barDataList1.add(decimalFormat.format(taskListClosed)+"%");
        barDataList1.add(getString(R.string.milestones));
        barDataList1.add(decimalFormat.format(milestoneClosed)+"%");
        barGraph.setBottomTextList(barDataList1);
        barGraph.setDataList(barDataList, 100);
    }
}
