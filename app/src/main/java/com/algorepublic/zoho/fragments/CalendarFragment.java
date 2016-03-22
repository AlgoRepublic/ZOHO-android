package com.algorepublic.zoho.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.TasksListByOwnerModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
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
 * create an instance of this fragment_forums.
 */
public class CalendarFragment extends BaseFragment implements CalendarPickerController {

    private AQuery aq;
    private AgendaCalendarView calendarView;
    private BaseClass baseClass;
    private Receiver receiver;
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

//        initCalendarView();
        TaskListService service = new TaskListService(getActivity());
        if(baseClass.getSelectedProject().equalsIgnoreCase("0")) {
            service.getTasksListByOwner(baseClass.getUserId(), true,
                    new CallBack(CalendarFragment.this, "OwnerTasksList"));
        }else{
            service.getTasksListByProject(baseClass.getSelectedProject(), true,
                    new CallBack(CalendarFragment.this, "OwnerTasksList"));
        }
    }

    public List<CalendarEvent> getTasksList() {
        List<CalendarEvent> eventList = new ArrayList<>();
        for(int i = 0; i < TasksListByOwnerModel.getInstance().responseObject.size(); i++) {
            for (int loop = 0; loop < TasksListByOwnerModel.getInstance().responseObject.get(i).taskObject.size(); loop++) {
                TasksListByOwnerModel.Tasks task = TasksListByOwnerModel.getInstance().responseObject.get(i).taskObject.get(loop);
                Calendar startTime = Calendar.getInstance();
                long startInMillis = Long.parseLong(DateMilli(task.startDate));
                startTime.setTimeInMillis(startInMillis);
                String startDate = DateFormatter(task.startDate);
                Calendar endTime = Calendar.getInstance();
                long endInMillis = Long.parseLong(DateMilli(task.endDate));
                endTime.setTimeInMillis(endInMillis);
                String endDate = DateFormatter(task.endDate);
                task.position = eventList.size();
                eventList.add(new BaseCalendarEvent(eventList.size(),task.title, task.projectName, Integer.toString(task.commentsCount),task.userObject.size()
                       ,startDate,endDate , getPriorityWiseColor(task.priority), startTime, endTime, true));
            }
        }
        return eventList;
    }

    public void OwnerTasksList(Object caller, Object model) {
        TasksListByOwnerModel.getInstance().setList((TasksListByOwnerModel) model);
        initCalendarView();
    }

    private void initCalendarView(){
        aq.id(R.id.alertMessage).text("No Tasks");
        Locale locale;
        // Get a reference for the week view in the layout.
        calendarView = (AgendaCalendarView) aq.id(R.id.agenda_calendar_view).getView();
       // Calendar minCal = new GregorianCalendar(2016, Calendar.JANUARY, 1);
        if(true) // update this check as language button goes functional
         locale = new Locale("ar");
        else
            locale = Locale.US;
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -11);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 2);
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
//        Log.d(LOG_TAG, String.format("Selected event: %s", event));
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
            if(action.contentEquals("Detail"))
            Toast.makeText(getContext(), position+": "+action, Toast.LENGTH_SHORT).show();
            if(action.contentEquals("Edit"))
                Toast.makeText(getContext(), position+": "+action, Toast.LENGTH_SHORT).show();
            if(action.contentEquals("Delete"))
                Toast.makeText(getContext(), position+": "+action, Toast.LENGTH_SHORT).show();
        }
    }

}
