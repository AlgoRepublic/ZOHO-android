package com.algorepublic.zoho.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.algorepublic.zoho.adapters.AdapterTaskDetailAssignee;
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

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskDetailFragment#newInstance} factory method to
 * create an instance of this fragment_forums.
 */
public class TaskDetailFragment extends BaseFragment {

    AQuery aq;
    static TaskDetailFragment fragment;
    static int position;
    TaskListService service;
    int click=0;
    DonutProgress seekBarCompat;
    TwoWayView twoWayAssignee;
    SeekBar seekBar;
    View views;
    int multiple=5;
    int progress=0;
    public static ACProgressFlower dialog;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setRetainInstance(true);
        getToolbar().setTitle(getString(R.string.task_detail));
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_forums
        final View view =  inflater.inflate(R.layout.fragment_task_detail, container, false);
        setHasOptionsMenu(true);
        dialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        seekBarCompat = (DonutProgress) view.findViewById(R.id.circularprogressBar);
        seekBar =(SeekBar) view.findViewById(R.id.seekBar);
        twoWayAssignee = (TwoWayView) view.findViewById(R.id.task_assignee);
        twoWayAssignee.setHasFixedSize(true);
        views =(View) view.findViewById(R.id.priority_bar);
        twoWayAssignee.setHasFixedSize(true);
        twoWayAssignee.setLongClickable(true);
        twoWayAssignee.setOrientation(TwoWayLayoutManager.Orientation.HORIZONTAL);
        twoWayAssignee.setAdapter(new AdapterTaskDetailAssignee(getActivity(),
                TasksListFragment.generalList.get(position).getListAssignees()));

        aq = new AQuery(view);
        getToolbar().setTitle(getString(R.string.task_details));
        service = new TaskListService(getActivity());
        service.getTaskAttachments(10, true, new CallBack(TaskDetailFragment.this, "TaskAttachments"));
        aq.id(R.id.start_date).text(TasksListFragment.generalList.get(position).getStartDate());
        aq.id(R.id.end_date).text(TasksListFragment.generalList.get(position).getEndDate());
        aq.id(R.id.category).text(TasksListFragment.generalList.get(position).getTaskListName());
        aq.id(R.id.task_name).text(TasksListFragment.generalList.get(position).getTaskName());
        aq.id(R.id.task_desc).text(TasksListFragment.generalList.get(position).getDescription());
        aq.id(R.id.category).text(TasksListFragment.generalList.get(position).getTaskListName());
        aq.id(R.id.comment_count).text(Integer.toString(TasksListFragment.generalList.get(position).getCommentsCount()));
        aq.id(R.id.docs_count).text(Integer.toString(TasksListFragment.generalList.get(position).getDocumentsCount()));
        aq.id(R.id.subtask_count).text(Integer.toString(TasksListFragment.generalList.get(position).getSubTasksCount()));
        seekBar.setProgress(TasksListFragment.generalList.get(position).getProgress());
        seekBarCompat.setProgress(TasksListFragment.generalList.get(position).getProgress());

        Drawable shapeDrawable = aq.id(R.id.priority_bar).getView().getBackground();
        GradientDrawable colorDrawable = (GradientDrawable) shapeDrawable;
        colorDrawable.setColor(getPriorityWiseColor(TasksListFragment.generalList.get(position).getPriority()));
        aq.id(R.id.priority_bar).getView().setBackground(shapeDrawable);
        views.setBackgroundColor(getPriorityWiseColor(TasksListFragment.generalList.get(position).getPriority()));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progres, boolean fromUser) {

                progres = ((int) Math.round(progres / multiple)) * multiple;
                seekBarCompat.setProgress(progres);
                progress=progres;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(progress==100){
                    aq.id(R.id.mark_as_done).text("ReOPen Task");
                    service.updateTaskProgress(TasksListFragment.generalList.get(position).getTaskID()
                            , progress, true, new CallBack(TaskDetailFragment.this, "UpdateProgress"));
                }
                aq.id(R.id.mark_as_done).text("Mark as done");
                service.updateTaskProgress(TasksListFragment.generalList.get(position).getTaskID()
                        , progress, true, new CallBack(TaskDetailFragment.this, "UpdateProgress"));
            }
        });


        aq.id(R.id.comment).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(aq.id(R.id.comment_count).getText().toString()) > 0) {
                    callFragmentWithBackStack(R.id.container, TaskCommentFragment.newInstance(TasksListFragment.
                            generalList.get(position).getTaskID())
                            , "TaskComment");
                }
            }
        });
        aq.id(R.id.documents).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(aq.id(R.id.docs_count).getText().toString()) > 0) {
                    callFragmentWithBackStack(R.id.container, DocumentsListBySubTaskFragment.newInstance(TasksListFragment.
                            generalList.get(position).getTaskID()), "DocumentsListBySubTaskFragment");
                }
            }
        });
        aq.id(R.id.subtask).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(aq.id(R.id.subtask_count).getText().toString()) > 0) {
                    callFragmentWithBackStack(R.id.container, TaskListBySubTasksFragment.newInstance(TasksListFragment.
                            generalList.get(position).getTaskID()), "TaskListBySubTasksFragment");
                }
            }
        });
        if(TasksListFragment.generalList.get(position).getProgress()==100){
            aq.id(R.id.mark_as_done).text("ReOpen Task");

        }
            aq.id(R.id.mark_as_done).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TasksListFragment.generalList.get(position).getProgress()==100){
                        NormalDialogCustomAttr(getString(R.string.reopen_task));
                    }else {
                        NormalDialogCustomAttr(getString(R.string.mark_as_done));
                    }

                }
            });



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_task_details, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_task:
                Intent intent = new Intent(getActivity(), ActivityTask.class);
                intent.putExtra("pos",position);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void UpdateProgress(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            //seekBarCompat.setProgress(100);
            TasksListFragment.generalList.get(position).setProgress(progress);
            Snackbar.make(getView(), getString(R.string.update_progress), Snackbar.LENGTH_SHORT).show();
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
        dialog.dismiss();
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            Snackbar.make(getView(), getString(R.string.task_done), Snackbar.LENGTH_SHORT).show();
            seekBar.setProgress(100);
            seekBarCompat.setProgress(100);
            aq.id(R.id.mark_as_done).text("ReOpen Task");
            TasksListFragment.generalList.get(position).setProgress(100);

        }
        else
        {
            Snackbar.make(getView(), getString(R.string.invalid_credential), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void UpdateTask(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        dialog.dismiss();
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            Snackbar.make(getView(), getString(R.string.reopen_task), Snackbar.LENGTH_SHORT).show();
            seekBar.setProgress(0);
            seekBarCompat.setProgress(0);
            aq.id(R.id.mark_as_done).text("Mark as done");
            TasksListFragment.generalList.get(position).setProgress(0);

        }}




    public void ReOpenTask(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        dialog.dismiss();
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            Snackbar.make(getView(), getString(R.string.reopen_task), Snackbar.LENGTH_SHORT).show();
            seekBar.setProgress(0);
            seekBarCompat.setProgress(0);
            aq.id(R.id.mark_as_done).text("Mark as done");
            TasksListFragment.generalList.get(position).setProgress(0);

        }
        else
        {
            Snackbar.make(getView(), getString(R.string.invalid_credential), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void NormalDialogCustomAttr(String content) {
        final NormalDialog dialog = new NormalDialog(getActivity());
        dialog.isTitleShow(false)//
                .bgColor(getResources().getColor(R.color.colorBaseWrapper))//
                .cornerRadius(5)//
                .content(content)//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(getResources().getColor(R.color.colorBaseHeader))//
                .dividerColor(getResources().getColor(R.color.colorContentWrapper))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(getResources().getColor(R.color.colorBaseHeader)
                        , getResources().getColor(R.color.colorBaseHeader))//
                .btnPressColor(getResources().getColor(R.color.colorBaseMenu))//
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
                            dialog.show();
                            if(aq.id(R.id.mark_as_done).getText().toString().equalsIgnoreCase("Mark as done")) {
                                service.updateTaskProgress(TasksListFragment.generalList.get(position).getTaskID()
                                        , 100, true, new CallBack(TaskDetailFragment.this, "CompleteTask"));
                            }else{
                                service.updateTaskProgress(TasksListFragment.generalList.get(position).getTaskID()
                                        , 0, true, new CallBack(TaskDetailFragment.this, "ReOpenTask"));
                            }

                        }
                    }
                });
    }
}
