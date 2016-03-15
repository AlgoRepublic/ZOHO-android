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
import com.dacer.androidcharts.BarView;
import com.dacer.androidcharts.LineView;
import com.dacer.androidcharts.PieHelper;
import com.dacer.androidcharts.PieView;

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
    int allTasksCount;
    ACProgressFlower dialogAC;
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
        aq = new AQuery(view);
        dialogAC = InitializeDialog(getActivity());
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        barGraph = (BarView)view.findViewById(R.id.line_view);
        pieGraph  = (PieView)view.findViewById(R.id.pie_view);
        service = new DashBoardService(getActivity());
        if(baseClass.getSelectedProject().equalsIgnoreCase("0")) {
            Toast.makeText(getActivity(), "Please Select Project", Toast.LENGTH_SHORT).show();
        }else
        {
            service.getMileStone(baseClass.getSelectedProject(), false,
                    new CallBack(DashboardFragment.this, "DashBoardList"));
            dialogAC.show();
        }
        return view;
    }

    public void DashBoardList(Object caller, Object model) {
        DashBoardModel.getInstance().setList((DashBoardModel) model);
        dialogAC.dismiss();
        if (DashBoardModel.getInstance().responseCode == 100) {
            ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();

            allTasksCount =DashBoardModel.getInstance().responseObject.tasks.size();
            for (int i=0;i< allTasksCount;i++){
                int progress=DashBoardModel.getInstance().responseObject.tasks.get(i).Position;
                if (progress==100){
                    tasksCloased.add(i);
                }else {
                    tasksOpen.add(i);
                }
            }
            PieHelper pieHelper = new PieHelper(30,
                    "Title",R.color.colorBaseWrapperBlue);
            PieHelper pieHelper1 = new PieHelper(70,
                    "Title1",R.color.theme_accent);
            pieHelperArrayList.add(pieHelper);
            pieHelperArrayList.add(pieHelper1);

            pieGraph.selectedPie(PieView.NO_SELECTED_INDEX);
            pieGraph.showPercentLabel(true);
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
            int random = (int)(Math.random()*20)+6;
        ArrayList<String> titles = new ArrayList<String>();
        titles.add("Task");
        titles.add("Tasks List");
        titles.add("Milestones");
        barGraph.setBottomTextList(titles);

        ArrayList<Integer> barDataList = new ArrayList<Integer>();
        for(int i=0; i<3; i++){
            barDataList.add((int)(Math.random() * 100));
        }

        barGraph.setDataList(barDataList,100);
    }
}
