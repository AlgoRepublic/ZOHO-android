package com.algorepublic.zoho.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.R;
import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;

import java.util.ArrayList;

/**
 * Created by waqas on 3/10/16.
 */
public class DashboardFragment extends BaseFragment {
    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_forums
        View view  = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ArrayList<Bar> points = new ArrayList<Bar>();
        Bar d = new Bar();
        d.setColor(Color.parseColor("#DDF4FD"));
        d.setName("Test1");
        d.setValue(30);
        Bar d2 = new Bar();
        d2.setColor(Color.parseColor("#DFEFB4"));
        d2.setName("Test2");
        d2.setValue(50);
        Bar d3 = new Bar();
        d3.setColor(Color.parseColor("#FDEADD"));
        d3.setName("Test3 %");
        d3.setValue(70);
        points.add(d);
        points.add(d2);
        points.add(d3);
        BarGraph g = (BarGraph)view.findViewById(R.id.graph);
        g.setBars(points);


        return view;
    }
}
