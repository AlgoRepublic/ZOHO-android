package com.algorepublic.zoho.fragments;

import android.graphics.Color;
import android.os.Bundle;
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
import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

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
    PieGraph pieGraph;
    BarGraph barGraph;
    int allTasksCount;
    ArrayList<Integer> tasksOpen = new ArrayList<>();
    ArrayList<Integer> tasksCloased= new ArrayList<>();
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
        InitializeDialog(getActivity());
        aq = new AQuery(view);
        InitializeDialog(getActivity());
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        barGraph = (BarGraph)view.findViewById(R.id.graph);
        pieGraph  = (PieGraph)view.findViewById(R.id.graphy);
        service = new DashBoardService(getActivity());
        if(baseClass.getSelectedProject().equalsIgnoreCase("0")) {
            Toast.makeText(getActivity(), "Please Select Project", Toast.LENGTH_SHORT).show();
        }else
        {
            service.getMileStone(baseClass.getSelectedProject(), false,
                    new CallBack(DashboardFragment.this, "DashBoardList"));
            BaseActivity.dialogAC.show();
        }
        return view;
    }

    public void DashBoardList(Object caller, Object model) {
        DashBoardModel.getInstance().setList((DashBoardModel) model);
        BaseActivity.dialogAC.dismiss();
        if (DashBoardModel.getInstance().responseCode == 100) {

            ArrayList<Bar> points = new ArrayList<Bar>();
            Bar d = new Bar();
            d.setColor(Color.parseColor("#DDF4FD"));
            d.setName("Tasks");
            d.setValue(DashBoardModel.getInstance().responseObject.tasks.size());
            Bar d2 = new Bar();
            d2.setColor(Color.parseColor("#DFEFB4"));
            d2.setName("TasksList");
            d2.setValue(DashBoardModel.getInstance().responseObject.tasksList.size());
            Bar d3 = new Bar();
            d3.setColor(Color.parseColor("#FDEADD"));
            d3.setName("Milestones");
            d3.setValue(DashBoardModel.getInstance().responseObject.milestones.size());
            points.add(d);
            points.add(d2);
            points.add(d3);
            barGraph.setBars(points);
            allTasksCount =DashBoardModel.getInstance().responseObject.tasks.size();
            for (int i=0;i< allTasksCount;i++){
                int progress=DashBoardModel.getInstance().responseObject.tasks.get(i).Position;
                if (progress==100){
                    tasksCloased.add(i);
                }else {
                    tasksOpen.add(i);
                }
            }

            PieSlice slice = new PieSlice();
            slice.setColor(Color.parseColor("#DDF4FD"));
            slice.setValue((tasksCloased.size() / allTasksCount) * 100);
            slice.setTitle("Closed");
            pieGraph.addSlice(slice);
            slice = new PieSlice();
            slice.setColor(Color.parseColor("#DFEFB4"));
            slice.setValue((tasksOpen.size()/ allTasksCount)*100);
            slice.setTitle("Opened");
            pieGraph.addSlice(slice);

        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tasklist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
