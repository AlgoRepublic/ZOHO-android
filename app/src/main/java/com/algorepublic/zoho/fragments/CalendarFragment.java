package com.algorepublic.zoho.fragments;


import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.algorepublic.zoho.Models.TasksListModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends BaseFragment implements
        WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener {

    AQuery aq;
    WeekView mWeekView;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalendarFragment.
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_calendar, container, false);
        aq = new AQuery(getActivity(), view);
        return view;
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) aq.id(R.id.weekView).getView();

        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(this);


        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        if(!TasksListModel.getInstance().responseObject.isEmpty()){
            TaskListService service = new TaskListService(getActivity());
            service.getTasksList(true, new CallBack(this, "TasksList"));
        }
    }

    /**
     * Triggered when clicked on one existing event
     *
     * @param event     : event clicked.
     * @param eventRect : view containing the clicked event.
     */
    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    /**
     * Very important interface, it's the base to load events in the calendar.
     * This method is called three times: once to load the previous month, once to load the next month and once to load the current month.<br/>
     * <strong>That's why you can have three times the same event at the same place if you mess up with the configuration</strong>
     *
     * @param newYear  : year of the events required by the view.
     * @param newMonth : month of the events required by the view <br/><strong>1 based (not like JAVA API) --> January = 1 and December = 12</strong>.
     * @return a list of the events happening <strong>during the specified month</strong>.
     */
    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {


        ArrayList<WeekViewEvent> tasksList = new ArrayList<>();
        for(int i = 0; i < TasksListModel.getInstance().responseObject.size(); i++) {
            WeekViewEvent event = new WeekViewEvent();
            event.setName(TasksListModel.getInstance().responseObject.get(i).Title);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(Long.valueOf(TasksListModel.getInstance().responseObject.get(i).StartDate).toString().replaceAll("\\D+","")));
            event.setStartTime(cal);
            cal.setTimeInMillis(Long.parseLong(Long.valueOf(TasksListModel.getInstance().responseObject.get(i).EndDate).toString().replaceAll("\\D+","")));
            event.setEndTime(cal);
            event.setName(TasksListModel.getInstance().responseObject.get(i).Title);
            event.setId(TasksListModel.getInstance().responseObject.get(i).ResponsibelID);
            event.setColor(getPriorityWiseColor(TasksListModel.getInstance().responseObject.get(i).Priority));
            tasksList.add(event);
        }
        return tasksList;
    }

    /**
     * Similar to {@link WeekView.EventClickListener} but with a long press.
     *
     * @param event     : event clicked.
     * @param eventRect : view containing the clicked event.
     */
    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    public void TasksList(Object caller, Object model) {
        TasksListModel.getInstance().setList((TasksListModel) model);
        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);
    }
}
