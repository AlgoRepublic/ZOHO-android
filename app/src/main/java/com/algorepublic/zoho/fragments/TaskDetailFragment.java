package com.algorepublic.zoho.fragments;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.algorepublic.zoho.ActivityTask;
import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.TaskAttachmentsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskDetailAssignee;
import com.algorepublic.zoho.adapters.AdapterTaskDetailAttachments;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.androidquery.AQuery;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.lucasr.twowayview.TwoWayLayoutManager;
import org.lucasr.twowayview.widget.TwoWayView;

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
    TaskListService service;
    int click=0;
    DonutProgress seekBarCompat;
    TwoWayView twoWayAssignee;
    TwoWayView twoWayAttachments;
    SeekBar seekBar;
    View views;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TaskDetailFragment newInstance(int pos) {
        position =pos;
        fragment = new TaskDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_task_detail, container, false);
        setHasOptionsMenu(true);
        seekBarCompat = (DonutProgress) view.findViewById(R.id.circularprogressBar);
        seekBar =(SeekBar) view.findViewById(R.id.progressBar);
        twoWayAssignee = (TwoWayView) view.findViewById(R.id.task_assignee);
      //  twoWayAttachments = (TwoWayView) view.findViewById(R.id.taskdetail_attachments);
        twoWayAssignee.setHasFixedSize(true); //twoWayAttachments.setHasFixedSize(true);
        views =(View) view.findViewById(R.id.priority_bar);
//        twoWayAttachments = (TwoWayView) view.findViewById(R.id.taskdetail_attachments);
        twoWayAssignee.setHasFixedSize(true);// twoWayAttachments.setHasFixedSize(true);
        twoWayAssignee.setLongClickable(true);//twoWayAttachments.setLongClickable(true);
        twoWayAssignee.setOrientation(TwoWayLayoutManager.Orientation.HORIZONTAL);
        //twoWayAttachments.setOrientation(TwoWayLayoutManager.Orientation.HORIZONTAL);
        twoWayAssignee.setAdapter(new AdapterTaskDetailAssignee(getActivity(),
                TasksListFragment.generalList.get(position).getListAssignees()));

        aq = new AQuery(view);
        MainActivity.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setToolbar();
        service = new TaskListService(getActivity());
        service.getTaskAttachments(10, true, new CallBack(TaskDetailFragment.this, "TaskAttachments"));
        aq.id(R.id.start_date).text(TasksListFragment.generalList.get(position).getStartDate());
        aq.id(R.id.end_date).text(TasksListFragment.generalList.get(position).getEndDate());
        aq.id(R.id.category).text(TasksListFragment.generalList.get(position).getTaskListName());
        aq.id(R.id.task_desc).text(TasksListFragment.generalList.get(position).getTaskListName());
//        aq.id(R.id.taskdate).text(DaysDifference(TasksListFragment.generalList.get(position).getEndMilli()));
//        seekBarCompat.setValue(TasksListFragment.generalList.get(position).getProgress());
        seekBar.setProgress(TasksListFragment.generalList.get(position).getProgress());
        seekBarCompat.setProgress(TasksListFragment.generalList.get(position).getProgress());

        Drawable shapeDrawable = (Drawable) aq.id(R.id.priority_bar).getView().getBackground();
        GradientDrawable colorDrawable = (GradientDrawable) shapeDrawable;
        colorDrawable.setColor(getPriorityWiseColor(TasksListFragment.generalList.get(position).getPriority()));
        aq.id(R.id.priority_bar).getView().setBackground(shapeDrawable);
        aq.id(R.id.edit_task).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityTask.class);
                intent.putExtra("pos",position);
                startActivity(intent);
            }
        });

        views.setBackgroundColor(getPriorityWiseColor(TasksListFragment.generalList.get(position).getPriority()));
      seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
              seekBarCompat.setProgress(progress);
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
                callFragmentWithBackStack(R.id.container, TaskCommentFragment.newInstance(position), "TaskComment");
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
    public void TaskAttachments(Object caller, Object model) {
        TaskAttachmentsModel.getInstance().setList((TaskAttachmentsModel) model);
        if (TaskAttachmentsModel.getInstance().responseCode == 100) {
            twoWayAttachments.setAdapter(new AdapterTaskDetailAttachments(getActivity()));
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.invalid_credential), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void UpdateProgress(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            //seekBarCompat.setProgress(100);
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
