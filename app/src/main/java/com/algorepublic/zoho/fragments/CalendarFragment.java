package com.algorepublic.zoho.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.TasksListModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.androidquery.AQuery;
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
 * create an instance of this fragment.
 */
public class CalendarFragment extends BaseFragment implements CalendarPickerController {

    AQuery aq;
    AgendaCalendarView calendarView;

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

//        initCalendarView();
        if(TasksListModel.getInstance().responseObject.isEmpty()){
            TaskListService service = new TaskListService(getActivity());
            service.getTasksList(true, new CallBack(this, "TasksList"));
        }else{
            initCalendarView();
        }
    }

    public List<CalendarEvent> getTasksList() {
        List<CalendarEvent> eventList = new ArrayList<>();
        for(int i = 0; i < TasksListModel.getInstance().responseObject.size(); i++) {
            TasksListModel.ResponseObject task = TasksListModel.getInstance().responseObject.get(i);
            Calendar startTime = Calendar.getInstance();
            startTime.setTimeInMillis(Long.parseLong(TasksListModel.getInstance().responseObject.get(i).startDate.replaceAll("\\D+","")));
            startTime.add(Calendar.MONTH, 1);

            Calendar endTime = Calendar.getInstance();
            endTime.setTimeInMillis(Long.parseLong(TasksListModel.getInstance().responseObject.get(i).endDate.replaceAll("\\D+","")));
            endTime.add(Calendar.MONTH, 1);

            BaseCalendarEvent event = new BaseCalendarEvent(task.title, task.projectName, "", getPriorityWiseColor(task.priority), startTime, endTime, true);
            eventList.add(event);
        }
        return eventList;
    }


    public void TasksList(Object caller, Object model) {
        TasksListModel.getInstance().setList((TasksListModel) model);
        initCalendarView();
    }

    private void initCalendarView(){
        // Get a reference for the week view in the layout.
        calendarView = (AgendaCalendarView) aq.id(R.id.agenda_calendar_view).getView();

        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.YEAR, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 2);
        List<CalendarEvent> events = getTasksList();
        if(events.size() != 0 ){
            try {
                calendarView.init(events, minDate, maxDate, Locale.getDefault(), this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(getActivity(), "No tasks found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDaySelected(DayItem dayItem) {
//        Log.d(LOG_TAG, String.format("Selected day: %s", dayItem));
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
//        Log.d(LOG_TAG, String.format("Selected event: %s", event));
    }
}
