package com.algorepublic.zoho.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.algorepublic.zoho.ActivityTask;
import com.algorepublic.zoho.R;
import com.androidquery.AQuery;

import app.minimize.com.seek_bar_compat.SeekBarCompat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTaskDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTaskDetail extends BaseFragment {

    AQuery aq;
    static FragmentTaskDetail fragment;
    static int position;
    SeekBarCompat seekBarCompat;

    public FragmentTaskDetail() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentTaskDetail newInstance(int pos) {
        position =pos;
        if(fragment==null){
            fragment = new FragmentTaskDetail();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_taskdetail, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit_task:
               Intent intent = new Intent(getActivity(), ActivityTask.class);
                intent.putExtra("pos",position);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_task_detail, container, false);
        setHasOptionsMenu(true);
        seekBarCompat = (SeekBarCompat) view.findViewById(R.id.seekBar);
        aq = new AQuery(view);
        setPriority();
        aq.id(R.id.task_name).text(FragmentTasksList.generalList.get(position).getTaskName());
        aq.id(R.id.task_desc).text(FragmentTasksList.generalList.get(position).getTaskListName());
        aq.id(R.id.taskdate).text(DaysDifference(FragmentTasksList.generalList.get(position).getEndMilli()));
        seekBarCompat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                aq.id(R.id.percentage).text(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        aq.id(R.id.comment).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithBackStack(R.id.container,FragmentTaskComment.newInstance(position),"TaskComment");
            }
        });
        return view;
    }

    public void setPriority(){
        aq.id(R.id.textView).backgroundColor(
                getPriorityWiseColor(FragmentTasksList.generalList.get(position).getPriority()));
        if(FragmentTasksList.generalList.get(position).getPriority()==0)
            aq.id(R.id.textView).text("None");
        if(FragmentTasksList.generalList.get(position).getPriority()==1)
            aq.id(R.id.textView).text("Low");
        if(FragmentTasksList.generalList.get(position).getPriority()==2)
            aq.id(R.id.textView).text("Medium");
        if(FragmentTasksList.generalList.get(position).getPriority()==3)
            aq.id(R.id.textView).text("High");
    }
}
