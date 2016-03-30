package com.algorepublic.zoho.fragments;



import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.TasksListByOwnerModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.TaskListAssignee;
import com.algorepublic.zoho.adapters.TaskListName;
import com.algorepublic.zoho.adapters.TasksList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.LocaleHelper;
import com.androidquery.AQuery;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment_forums.
 */
public class CalendarFragment extends BaseFragment implements CalendarPickerController {

    private AQuery aq;
    private AgendaCalendarView calendarView;
    private BaseClass baseClass;
    List<CalendarEvent> eventList = eventList = new ArrayList<>();
    TaskListService service;
    private Receiver receiver;int clickedPosition;
    public static ArrayList<TaskListName> taskListName = new ArrayList<>();
    public static ArrayList<TasksList> allTaskList = new ArrayList<>();
    public CalendarFragment() {
        // Required empty public constructor
        if (receiver ==null)
        receiver = new Receiver();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment_forums using the provided parameters.
     *
     * @return A new instance of fragment_forums CalendarFragment.
     */
    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        IntentFilter filter = new IntentFilter("com.algorepublic.zoho.Event_Click");
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_forums
        View view  = inflater.inflate(R.layout.fragment_calendar, container, false);
        aq = new AQuery(getActivity(), view);
        setHasOptionsMenu(true);
        getToolbar().setTitle(getString(R.string.calendar));
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        return view;
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment_forums's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment_forums is being re-constructed
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        service = new TaskListService(getActivity());
        if(baseClass.getSelectedProject().equalsIgnoreCase("0")) {
            service.getTasksListByOwner(baseClass.getUserId(), true,
                    new CallBack(CalendarFragment.this, "OwnerTasksList"));
        }else{
            service.getTasksListByProject(baseClass.getSelectedProject(), true,
                    new CallBack(CalendarFragment.this, "OwnerTasksList"));
        }
    }

    public List<CalendarEvent> getTasksList() {
        eventList.clear();String startDate,endDate;
        for (int loop = 0; loop < allTaskList.size(); loop++) {
            if (allTaskList.get(loop).getStartMilli().equalsIgnoreCase("62135535600000")
                    || allTaskList.get(loop).getStartMilli().equalsIgnoreCase("-62135571600000")
                    || allTaskList.get(loop).getStartMilli().equalsIgnoreCase("62135571600000")){
                    startDate = getString(R.string.no_date);
            }else {
                startDate =allTaskList.get(loop).getStartDate();
            }
            if (allTaskList.get(loop).getEndMilli().equalsIgnoreCase("62135535600000")
                    || allTaskList.get(loop).getEndMilli().equalsIgnoreCase("-62135571600000")
                    || allTaskList.get(loop).getEndMilli().equalsIgnoreCase("62135571600000")){
                endDate = getString(R.string.no_date);
            }else {
                endDate =allTaskList.get(loop).getEndDate();
            }
            Calendar startTime = Calendar.getInstance();
            long startInMillis = Long.parseLong(allTaskList.get(loop).getStartMilli());
            startTime.setTimeInMillis(startInMillis);
            Calendar endTime = Calendar.getInstance();
            long endInMillis = Long.parseLong(allTaskList.get(loop).getEndMilli());
            endTime.setTimeInMillis(endInMillis);
            int position = eventList.size();
            eventList.add(new BaseCalendarEvent(position,allTaskList.get(loop).getTaskName(),
                    allTaskList.get(loop).getProjectName(),
                    allTaskList.get(loop).getCommentsCount(),
                    allTaskList.get(loop).getListAssignees().size(),
                    startDate,
                    endDate,
                    getPriorityWiseColor( allTaskList.get(loop).getPriority()),
                    startTime, endTime, true));
        }
        return eventList;
    }
    public void AddAllTasks(){
        allTaskList.clear();taskListName.clear();
        for (int loop = 0; loop < TasksListByOwnerModel.getInstance().responseObject.size(); loop++) {
            TaskListName tasklistName = new TaskListName();
            tasklistName.setTaskListID(TasksListByOwnerModel.getInstance().responseObject.get(loop).tasklistID);
            tasklistName.setTaskListName(TasksListByOwnerModel.getInstance().responseObject.get(loop).taskListName);
            taskListName.add(tasklistName);
            for (int loop1 = 0; loop1 < TasksListByOwnerModel.getInstance().responseObject.get(loop).taskObject.size(); loop1++) {
                TasksListByOwnerModel.Tasks taskModel = TasksListByOwnerModel.getInstance().responseObject.get(loop).taskObject.get(loop1);
                TasksList tasksList = new TasksList();
                if (taskModel.title == null) {
                    tasksList.setTaskName("-");
                } else {
                    tasksList.setTaskName(taskModel.title.substring(0, 1).toUpperCase() + taskModel.title.substring(1));
                }
                tasksList.setTaskID(taskModel.taskID);
                tasksList.setEndMilli(DateMilli(taskModel.endDate));
                tasksList.setStartMilli(DateMilli(taskModel.startDate));
                tasksList.setProjectName(taskModel.projectName);
                tasksList.setProjectID(TasksListByOwnerModel.getInstance().responseObject.get(loop).projectID);
                tasksList.setStartDate(DateFormatter(taskModel.startDate));
                tasksList.setEndDate(DateFormatter(taskModel.endDate));
                tasksList.setHeader(DateMilli(taskModel.endDate));
                if(taskModel.description == null){
                    tasksList.setDescription(getActivity().getString(R.string.n_a));
                }else {
                    tasksList.setDescription(taskModel.description);
                }
                tasksList.setPriority(taskModel.priority);
                tasksList.setProgress(taskModel.progress);
                tasksList.setParentTaskID(taskModel.parentTaskID);
                tasksList.setCommentsCount(taskModel.commentsCount);
                tasksList.setDocumentsCount(taskModel.documentsCount);
                tasksList.setSubTasksCount(taskModel.subTasksCount);
                tasksList.setTaskListName(TasksListByOwnerModel.getInstance().responseObject.get(loop).taskListName);
                tasksList.setTaskListNameID(TasksListByOwnerModel.getInstance().responseObject.get(loop).tasklistID);
                //************** Assignee List ************//
                ArrayList<TaskListAssignee> listAssignees = new ArrayList<>();
                for (int loop2 = 0; loop2 < TasksListByOwnerModel.getInstance().responseObject.get(loop).taskObject.get(loop1).userObject.size(); loop2++) {
                    TasksListByOwnerModel.Users users = TasksListByOwnerModel.getInstance().responseObject.get(loop).taskObject.get(loop1).userObject.get(loop2);
                    TaskListAssignee assignee = new TaskListAssignee();
                    assignee.setUserID(users.responsibleID);
                    assignee.setFirstName(users.firstName);
                    assignee.setLastName(users.lastName);
                    assignee.setProfileImage(users.profileImagePath);
                    listAssignees.add(assignee);
                }
                tasksList.setListAssignees(listAssignees);
                //************** ******* ************//
                allTaskList.add(tasksList);
            }
        }
        initCalendarView();
    }
    public void OwnerTasksList(Object caller, Object model) {
        TasksListByOwnerModel.getInstance().setList((TasksListByOwnerModel) model);
        AddAllTasks();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initCalendarView(){
        aq.id(R.id.alertMessage).text("No Tasks");
        Locale locale;
        // Get a reference for the week view in the layout.
        calendarView = (AgendaCalendarView) aq.id(R.id.agenda_calendar_view).getView();

        locale = new Locale(LocaleHelper.getLanguage(getContext()));
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -11);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);
        List<CalendarEvent> events = getTasksList();
        if(events.size() != 0 ){
            try {
                calendarView.init(events, minDate, maxDate, locale, this);
            }catch (Exception e){
                e.printStackTrace();
            }
            aq.id(R.id.response_alert).visibility(View.GONE);
        }
        else {
            aq.id(R.id.response_alert).visibility(View.VISIBLE);
            Snackbar.make(getView(), getString(R.string.no_task_found), Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDaySelected(DayItem dayItem) {
//        Log.d(LOG_TAG, String.format("Selected day: %s", dayItem));
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
       Log.e("LOG_TAG", String.format("Selected event: %s", event));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            String action=null;int position;
            position = arg1.getIntExtra("position",0);
            if(arg1.hasExtra("Action"))
             action = arg1.getExtras().getString("Action");
            if(action.contentEquals("Detail")) {

                callFragmentWithBackStack(R.id.container, TaskDetailFragment.newInstance
                        (allTaskList.get(position), taskListName), "TaskDetail");
                Toast.makeText(getContext(), position + ": " + action, Toast.LENGTH_SHORT).show();
            }
            if(action.contentEquals("Edit")) {
                baseClass.setSelectedProject(Integer.toString(allTaskList.get(position).getProjectID()));
                if (allTaskList.get(position).getProjectID() > 0) {
                    baseClass.db.putString("ProjectName", allTaskList.get(position).getProjectName());
                    baseClass.setSelectedProject(Integer.toString(allTaskList.get(position).getProjectID()));
                    callFragmentWithBackStack(R.id.container,
                            TaskAddUpdateFragment.newInstance(allTaskList.get(position),
                                    taskListName),
                            "TaskAddUpdateFragment");
                }
                Toast.makeText(getContext(), position + ": " + action, Toast.LENGTH_SHORT).show();
            }
            if(action.contentEquals("Delete")) {
                clickedPosition = position;
                NormalDialogCustomAttr(getString(R.string.delete_task), allTaskList.get(position));
                Toast.makeText(getContext(), position + ": " + action, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void NormalDialogCustomAttr(String content, final TasksList tasksList) {
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
                        service.deleteTask(tasksList.getTaskID()
                                , true, new CallBack(CalendarFragment.this, "DeleteTask"));
                    }
                });
    }
    public void DeleteTask(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            allTaskList.remove(clickedPosition);
            initCalendarView();
            Snackbar.make(getView(), getString(R.string.task_deleted), Snackbar.LENGTH_SHORT).show();
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
}
