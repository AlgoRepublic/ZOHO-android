package com.algorepublic.zoho.fragments;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.TaskByIdModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskDetailAssignee;
import com.algorepublic.zoho.adapters.TaskListAssignee;
import com.algorepublic.zoho.adapters.TaskListName;
import com.algorepublic.zoho.adapters.TasksList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.lucasr.twowayview.TwoWayLayoutManager;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class TaskDetailFragment extends BaseFragment {

    AQuery aq;
    static TaskDetailFragment fragment;
    TaskListService service;
    int click=0;
    DonutProgress seekBarCompat;
    TwoWayView twoWayAssignee;
    SeekBar seekBar;
    static TaskListName listName;
    static ArrayList<TaskListName> taskListName = new ArrayList<>();
    View views;
    int multiple=5;
    public static TasksList tasksList;
    int progress=0;static int position;
    BaseClass baseClass;
    // WebView webView;

    @SuppressLint("ValidFragment")
    public TaskDetailFragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static TaskDetailFragment newInstance(TasksList tasks,
                                                 ArrayList<TaskListName> listNames,int pos) {
        tasksList =tasks;
        position = pos;
        taskListName = listNames;
        Log.e("Size", "S" + taskListName.size());
        fragment = new TaskDetailFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        getToolbar().setTitle(getString(R.string.task_detail));
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        seekBarCompat = (DonutProgress) view.findViewById(R.id.circularprogressBar);
        if(baseClass.getThemePreference() == R.style.AppThemeBlue) {
            seekBarCompat.setFinishedStrokeColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryBlue));
            seekBarCompat.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryBlue));
        }
        else{
            seekBarCompat.setFinishedStrokeColor(ContextCompat.getColor(getActivity(), R.color.colorBaseHeader));
            seekBarCompat.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBaseHeader));
        }
        seekBar =(SeekBar) view.findViewById(R.id.seekBar);
        twoWayAssignee = (TwoWayView) view.findViewById(R.id.task_assignee);
        twoWayAssignee.setHasFixedSize(true);
        views = view.findViewById(R.id.priority_bar);
        twoWayAssignee.setHasFixedSize(true);
        twoWayAssignee.setLongClickable(true);
        twoWayAssignee.setOrientation(TwoWayLayoutManager.Orientation.HORIZONTAL);
        twoWayAssignee.setAdapter(new AdapterTaskDetailAssignee(getActivity(),
                tasksList.getListAssignees()));

        aq = new AQuery(view);
        getToolbar().setTitle(getString(R.string.task_details));
        service = new TaskListService(getActivity());
        service.getTasksById(tasksList.getTaskID(),true
                ,new CallBack(TaskDetailFragment.this,"TaskDetails"));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progres, boolean fromUser) {
                progres = ((int) Math.round(progres / multiple)) * multiple;
                progress = progres;
                seekBarCompat.setProgress(progres);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarCompat.setProgress(progress);
                if (progress == 100) {
                    aq.id(R.id.icon).image(R.drawable.ic_notifications_green_24dp);
                    aq.id(R.id.mark_as_done).text(getString(R.string.reopen_task));
                    service.updateTaskProgress(tasksList.getTaskID()
                            , progress, true, new CallBack(TaskDetailFragment.this, "UpdateProgress"));
                }else {
                    aq.id(R.id.icon).image(R.drawable.taskdetail_icon);
                    aq.id(R.id.mark_as_done).text(getString(R.string.task_as_done));
                    service.updateTaskProgress(tasksList.getTaskID()
                            , progress, true, new CallBack(TaskDetailFragment.this, "UpdateProgress"));
                }
            }
        });
        /**
         * tasks_view_comment Permission impl on click.
         */
        aq.id(R.id.comment).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(baseClass.hasPermission(getResources().getString(R.string.tasks_view_comment))) //
                    callFragmentWithAddBackStack(R.id.container, TaskCommentFragment.newInstance(tasksList.getTaskID())
                            , "TaskComment");
                else
                    Toast.makeText(v.getContext(), "You don't have Permission to view comments", Toast.LENGTH_SHORT).show();
            }
        });
        aq.id(R.id.documents).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithAddBackStack(R.id.container, DocumentsListBySubTaskFragment.newInstance(tasksList.getTaskID()), "DocumentsListBySubTaskFragment");
            }
        });
        aq.id(R.id.subtask).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskListName obj = new TaskListName();
                obj.setTaskListName(tasksList.getTaskListName());
                obj.setTaskListID(tasksList.getTaskID());
                callFragmentWithAddBackStack(R.id.container, TaskListBySubTasksFragment.newInstance(
                        tasksList.getTaskID(),obj), "TaskListBySubTasksFragment");
                // Log.e("Size", "S" + taskListName.get(position).getTaskListName());
            }
        });
        aq.id(R.id.mark_as_done).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TaskByIdModel.getInstance().responseObject.progress==100){
                    click= 3;
                    NormalDialogCustomAttr(getString(R.string.reopen));
                }else {
                    click=2;
                    NormalDialogCustomAttr(getString(R.string.mark_as_done));
                }
            }
        });
        aq.id(R.id.delete).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click=1;
                NormalDialogCustomAttr(getString(R.string.delete_task));

            }
        });

        return view;
    }
    public void TaskDetails(Object caller, Object model) {
        TaskByIdModel.getInstance().setList((TaskByIdModel) model);
        if (TaskByIdModel.getInstance().responseCode == 100) {
            AddTasks();
            UpdateValue();
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.response_error), Toast.LENGTH_SHORT).show();

        }
    }

    public void UpdateValue(){
        if(TaskByIdModel.getInstance().responseObject.startDate != null) {
            if (DateFormatter(TaskByIdModel.getInstance().responseObject.startDate).equalsIgnoreCase("12/31/3938")||
                    DateFormatter(TaskByIdModel.getInstance().responseObject.startDate).equalsIgnoreCase("2/1/3938")) {
                aq.id(R.id.start_date).text("No Date");
            } else if (DateFormatter(TaskByIdModel.getInstance().responseObject.startDate).equalsIgnoreCase("3/0/1")) {
                aq.id(R.id.start_date).text("No Date");
            } else
                aq.id(R.id.start_date).text(DateFormatter(TaskByIdModel.getInstance().responseObject.startDate));
        }
        if(TaskByIdModel.getInstance().responseObject.endDate != null) {
            if (DateFormatter(TaskByIdModel.getInstance().responseObject.endDate).equalsIgnoreCase("12/31/3938")||
                    DateFormatter(TaskByIdModel.getInstance().responseObject.endDate).equalsIgnoreCase("2/1/3938")) {
                aq.id(R.id.end_date).text("No Date");
            } else if (DateFormatter(TaskByIdModel.getInstance().responseObject.endDate).equalsIgnoreCase("3/0/1")) {
                aq.id(R.id.end_date).text("No Date");
            } else
                aq.id(R.id.end_date).text(DateFormatter(TaskByIdModel.getInstance().responseObject.endDate));
        }
        aq.id(R.id.user_counter).text(String.valueOf(TaskByIdModel.getInstance().responseObject.userObject.size()));
        aq.id(R.id.category).text(TaskByIdModel.getInstance().responseObject.taskListName);
        aq.id(R.id.task_name).text(TaskByIdModel.getInstance().responseObject.title);
        //webView.loadDataWithBaseURL("", TaskByIdModel.getInstance().responseObject.description, "text/html", "UTF-8", "");

        if (TaskByIdModel.getInstance().responseObject.description ==null){
            aq.id(R.id.task_desc).text(getActivity().getString(R.string.n_a));
        }else
            aq.id(R.id.task_desc).text(Html.fromHtml(TaskByIdModel.getInstance().responseObject.description));

        twoWayAssignee.setAdapter(new AdapterTaskDetailAssignee(getActivity(),
                tasksList.getListAssignees()));

        aq.id(R.id.comment_count).text(Integer.toString(TaskByIdModel.getInstance().responseObject.commentsCount));
        aq.id(R.id.docs_count).text(Integer.toString(TaskByIdModel.getInstance().responseObject.documentsCount));
        aq.id(R.id.subtask_count).text(Integer.toString(TaskByIdModel.getInstance().responseObject.subTasksCount));
        applyLightBackground(aq.id(R.id.parent2).getView(), baseClass);
        seekBar.setProgress(TaskByIdModel.getInstance().responseObject.progress);
        seekBarCompat.setProgress(TaskByIdModel.getInstance().responseObject.progress);
        if(TaskByIdModel.getInstance().responseObject.progress==100){
            aq.id(R.id.icon).image(R.drawable.ic_notifications_green_24dp);
            aq.id(R.id.mark_as_done).text(getString(R.string.reopen_task));
        }
        Drawable shapeDrawable = aq.id(R.id.priority_bar).getView().getBackground();
        GradientDrawable colorDrawable = (GradientDrawable) shapeDrawable;
        colorDrawable.setColor(getPriorityWiseColor(TaskByIdModel.getInstance().responseObject.priority));
        aq.id(R.id.priority_bar).getView().setBackground(shapeDrawable);
        views.setBackgroundColor(getPriorityWiseColor(TaskByIdModel.getInstance().responseObject.priority));
    }
    public void AddTasks(){
        tasksList = new TasksList();
        TaskByIdModel.Tasks taskModel = TaskByIdModel.getInstance().responseObject;
        if (taskModel.title == null) {
            tasksList.setTaskName("-");
        } else {
            tasksList.setTaskName(taskModel.title.substring(0, 1).toUpperCase() + taskModel.title.substring(1));
        }
        tasksList.setTaskID(taskModel.taskID);
        tasksList.setEndMilli(DateMilli(taskModel.endDate));
        tasksList.setStartMilli(DateMilli(taskModel.startDate));
        tasksList.setProjectName(taskModel.projectName);
        tasksList.setProjectID(TaskByIdModel.getInstance().responseObject.projectID);
        tasksList.setStartDate(DateFormatter(taskModel.startDate));
        tasksList.setEndDate(DateFormatter(taskModel.endDate));
        tasksList.setHeader(DateMilli(taskModel.endDate));
        if(taskModel.description == null){
            tasksList.setDescription(getActivity().getString(R.string.n_a));
        }else {
            tasksList.setDescription(taskModel.description);
        }
        if(taskModel.tasklistID==0){
            taskModel.taskListName = getString(R.string.general);
        }

        tasksList.setTaskListName(TaskByIdModel.getInstance().responseObject.taskListName);
        tasksList.setTaskListNameID(TaskByIdModel.getInstance().responseObject.tasklistID);
        tasksList.setPriority(taskModel.priority);
        tasksList.setProgress(taskModel.progress);
        tasksList.setParentTaskID(taskModel.parentTaskID);
        tasksList.setCommentsCount(taskModel.commentsCount);
        tasksList.setDocumentsCount(taskModel.documentsCount);
        tasksList.setSubTasksCount(taskModel.subTasksCount);

        //************** Assignee List ************//
        ArrayList<TaskListAssignee> listAssignees = new ArrayList<>();
        for (int loop = 0; loop < TaskByIdModel.getInstance().responseObject.userObject.size(); loop++) {
            TaskByIdModel.Users users = TaskByIdModel.getInstance().responseObject.userObject.get(loop);
            TaskListAssignee assignee = new TaskListAssignee();
            assignee.setUserID(users.responsibleID);
            assignee.setFirstName(users.firstName);
            assignee.setLastName(users.lastName);
            assignee.setProfileImage(users.profileImagePath);
            listAssignees.add(assignee);
        }
        tasksList.setListAssignees(listAssignees);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_task_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_task:
                Log.e("S","/"+tasksList.getListAssignees().size());
                baseClass.setSelectedTaskProject(Integer.toString(tasksList.getProjectID()));
                if (Integer.parseInt(baseClass.getSelectedTaskProject()) >0) {
                    baseClass.db.putString("ProjectName", tasksList.getProjectName());
                    baseClass.setSelectedProject(Integer.toString(tasksList.getProjectID()));
                    callFragmentWithBackStack(R.id.container, TaskAddUpdateFragment.newInstance(tasksList,taskListName), "TaskAddUpdateFragment");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void UpdateProgress(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            TaskByIdModel.getInstance().responseObject.progress=100;
            Toast.makeText(getActivity(), getActivity().getString(R.string.update_progress), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getActivity(), getActivity().getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
    public void DeleteTask(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.task_deleted), Toast.LENGTH_SHORT).show();


            getActivity().getSupportFragmentManager().popBackStack();
        }
        else
        {
            Toast.makeText(getActivity(), getActivity().getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
    public void CompleteTask(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.task_done), Toast.LENGTH_SHORT).show();

            seekBar.setProgress(100);
            seekBarCompat.setProgress(100);
            TaskByIdModel.getInstance().responseObject.progress=100;
            aq.id(R.id.mark_as_done).text(getString(R.string.reopen_task));
        }
        else
        {
            Toast.makeText(getActivity(), getActivity().getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }



    public void ReOpenTask(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.reopen_task), Toast.LENGTH_SHORT).show();

            seekBar.setProgress(0);
            seekBarCompat.setProgress(0);
            aq.id(R.id.mark_as_done).text(getString(R.string.task_as_done));
            tasksList.setProgress(0);
            TaskByIdModel.getInstance().responseObject.progress=0;
        }
        else
        {
            Toast.makeText(getActivity(), getActivity().getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();

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
                            service.deleteTask(tasksList.getTaskID()
                                    , true, new CallBack(TaskDetailFragment.this, "DeleteTask"));
                        }
                        if (click==2) {
                            service.updateTaskProgress(tasksList.getTaskID()
                                    , 100, true, new CallBack(TaskDetailFragment.this, "CompleteTask"));
                        }
                        if(click==3){
                            service.updateTaskProgress(tasksList.getTaskID()
                                    , 0, true, new CallBack(TaskDetailFragment.this, "ReOpenTask"));
                        }
                    }
                });
    }

}
