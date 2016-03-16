package com.algorepublic.zoho.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.Models.DashBoardModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DashBoardService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.dacer.androidcharts.BarView;
import com.dacer.androidcharts.LineView;
import com.dacer.androidcharts.PieHelper;
import com.dacer.androidcharts.PieView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by waqas on 3/10/16.
 */
public class DashboardFragment extends BaseFragment {

    DashBoardService service;
    AQuery aq;
    BaseClass baseClass;
    PieView pieGraph;
    BarView barGraph;
    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setRetainInstance(true);
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
            Toast.makeText(getActivity(), "Please Select Project", Toast.LENGTH_SHORT).show();
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
            float opened = ((Float.parseFloat(""+object.totalTasksNo) - Float.parseFloat(""+object.completedTasksNo))
                    /Float.parseFloat(""+object.totalTasksNo))*100;
            PieHelper pieHelper = new PieHelper(closed,
                    "Closed Tasks",R.color.low_priority);
            PieHelper pieHelper1 = new PieHelper(opened,
                    "Opened Tasks",R.color.high_priority);
            pieHelperArrayList.add(pieHelper);
            pieHelperArrayList.add(pieHelper1);

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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tasklist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void randomSet(){

        ArrayList<Float> barDataList = new ArrayList<Float>();
        ArrayList<String> barDataList1 = new ArrayList<String>();

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        DashBoardModel.ResponseObject object = DashBoardModel.getInstance().responseObject;
        float taskClosed = (Float.parseFloat(""+object.completedTasksNo)/
                Float.parseFloat(""+object.totalTasksNo))*100;
        float taskListClosed = (Float.parseFloat(""+object.completedTaskList))
                /Float.parseFloat(""+object.totalTasksNo)*100;
        float milestoneClosed = (Float.parseFloat(""+object.completedTaskList))
                /Float.parseFloat(""+object.totalTasksNo)*100;

        barDataList.add(taskClosed);
        barDataList.add(taskListClosed);
        barDataList.add(milestoneClosed);
        barDataList1.add("Task");
        barDataList1.add(decimalFormat.format(taskClosed)+"%");
        barDataList1.add("Tasks List");
        barDataList1.add(decimalFormat.format(taskListClosed)+"%");
        barDataList1.add("Milestones");
        barDataList1.add(decimalFormat.format(milestoneClosed)+"%");
        barGraph.setBottomTextList(barDataList1);
        barGraph.setDataList(barDataList, 100);
    }
}
