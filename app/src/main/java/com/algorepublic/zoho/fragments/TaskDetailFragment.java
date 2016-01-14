package com.algorepublic.zoho.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.algorepublic.zoho.ActivityTask;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.androidquery.AQuery;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.github.lzyzsd.circleprogress.ArcProgress;

import app.minimize.com.seek_bar_compat.SeekBarCompat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskDetailFragment extends BaseFragment {

    AQuery aq;
    static TaskDetailFragment fragment;
    static int position;
    int Progress,opt;
    ArcProgress arcProgress;
    TaskListService service;
    int click=0;
    SeekBarCompat seekBarCompat;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TaskDetailFragment newInstance(int pos) {
        position =pos;
        if(fragment==null){
            fragment = new TaskDetailFragment();
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
        final View view =  inflater.inflate(R.layout.fragment_task_detail, container, false);
        setHasOptionsMenu(true);
        seekBarCompat = (SeekBarCompat) view.findViewById(R.id.seekBar);
        aq = new AQuery(view);
        setPriority();
        service = new TaskListService(getActivity());
        arcProgress = (ArcProgress) view.findViewById(R.id.arc_progress);
        arcProgress.setProgress(TasksListFragment.generalList.get(position).getProgress());
        aq.id(R.id.task_name).text(TasksListFragment.generalList.get(position).getTaskName());
        aq.id(R.id.task_desc).text(TasksListFragment.generalList.get(position).getTaskListName());
        aq.id(R.id.taskdate).text(DaysDifference(TasksListFragment.generalList.get(position).getEndMilli()));
        seekBarCompat.setProgress(TasksListFragment.generalList.get(position).getProgress());
        seekBarCompat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                arcProgress.setProgress(progress);
                Progress = progress;
                if(progress==100)
                    opt= 1;
                else
                    opt=0;
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
            callFragmentWithBackStack(R.id.container, TaskCommentFragment.newInstance(position),"TaskComment");
            }
        });
        aq.id(R.id.delete).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = 1;
                NormalDialogCustomAttr(getString(R.string.delete_task));
            }
        });
        aq.id(R.id.mark_as_done).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = 2;
                NormalDialogCustomAttr(getString(R.string.mark_as_done));
            }
        });
        return view;
    }
    public void UpdateProgress(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            seekBarCompat.setProgress(100);
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.invalid_credential), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void DeleteTask(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            Snackbar.make(getView(), getString(R.string.task_deleted), Snackbar.LENGTH_SHORT).show();
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.invalid_credential), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void CompleteTask(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            Snackbar.make(getView(), getString(R.string.task_done), Snackbar.LENGTH_SHORT).show();
            service.updateTaskProgress(TasksListFragment.generalList.get(position).getTaskID()
                    , Progress, true, new CallBack(TaskDetailFragment.this, "UpdateProgress"));
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.invalid_credential), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void setPriority(){
        aq.id(R.id.textView).backgroundColor(
                getPriorityWiseColor(TasksListFragment.generalList.get(position).getPriority()));
        if(TasksListFragment.generalList.get(position).getPriority()==0)
            aq.id(R.id.textView).text("None");
        if(TasksListFragment.generalList.get(position).getPriority()==1)
            aq.id(R.id.textView).text("Low");
        if(TasksListFragment.generalList.get(position).getPriority()==2)
            aq.id(R.id.textView).text("Medium");
        if(TasksListFragment.generalList.get(position).getPriority()==3)
            aq.id(R.id.textView).text("High");
    }
    private void NormalDialogCustomAttr(String content) {
        final NormalDialog dialog = new NormalDialog(getActivity());
        dialog.isTitleShow(false)//
                .bgColor(getResources().getColor(R.color.colorLightgray))//
                .cornerRadius(5)//
                .content(content)//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(getResources().getColor(R.color.colorPrimaryDark))//
                .dividerColor(getResources().getColor(R.color.colorPrimary))//
                .btnTextSize(15.5f, 15.5f)//
                        .btnTextColor(getResources().getColor(R.color.colorPrimaryDark)
                                , getResources().getColor(R.color.colorPrimaryDark))//
                .btnPressColor(getResources().getColor(R.color.colorDarkgray))//
                .widthScale(0.85f)//
                .showAnim(new BounceLeftEnter())//
                .dismissAnim(new SlideRightExit())//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                    dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        if(click==1)
                        {
                            service.deleteTask(TasksListFragment.generalList.get(position).getTaskID()
                                    , true, new CallBack(TaskDetailFragment.this, "DeleteTask"));
                        }
                        if (click==2)
                        {
                            service.completeTask(TasksListFragment.generalList.get(position).getTaskID()
                                    , opt, true, new CallBack(TaskDetailFragment.this, "CompleteTask"));
                        }
                    }
                });
    }
}
