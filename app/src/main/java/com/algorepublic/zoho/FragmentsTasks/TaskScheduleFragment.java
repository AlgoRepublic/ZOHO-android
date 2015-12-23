package com.algorepublic.zoho.FragmentsTasks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.androidquery.AQuery;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskScheduleFragment extends BaseFragment {

    static TaskScheduleFragment fragment;
    public static final String DATEPICKER_TAG = "datepicker";
    AQuery aq;
    String start_date,end_date;
    int start_day,start_month,start_year,end_day,end_month,end_year;
    public TaskScheduleFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskScheduleFragment newInstance() {
        if (fragment==null) {
            fragment = new TaskScheduleFragment();
        }
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
        View view =  inflater.inflate(R.layout.fragment_task_schedule, container, false);
        aq =  new AQuery(view);
        aq.id(R.id.btn_start_date).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplitStartDate();
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog;
                if(start_date==null) {
                    datePickerDialog = DatePickerDialog.newInstance(startDatePicker
                            , calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            false);
                }else{
                    datePickerDialog = DatePickerDialog.newInstance(startDatePicker
                            ,start_year,
                            start_month,
                            start_day,
                            false);
                }
                datePickerDialog.show(getFragmentManager(),DATEPICKER_TAG);
            }
        });
        aq.id(R.id.btn_end_date).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplitEndDate();
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog;
                if(end_date == null) {
                    datePickerDialog = DatePickerDialog.newInstance(endDatePicker
                            , calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            false);
                }else
                {
                    datePickerDialog = DatePickerDialog.newInstance(endDatePicker
                            ,end_year,
                            end_month,
                            end_day,
                            false);
                }
                datePickerDialog.show(getFragmentManager(),DATEPICKER_TAG);
            }
        });
        return view;
    }
    DatePickerDialog.OnDateSetListener startDatePicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
           start_date = dayOfMonth+"/"+(++monthOfYear)+"/"+year;
            start_day = dayOfMonth;start_month = monthOfYear;start_year = year;
           aq.id(R.id.btn_start_date).text(start_date);
        }
    };
    DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            end_date = dayOfMonth+"/"+(++monthOfYear)+"/"+year;
            end_day = dayOfMonth;end_month = monthOfYear;end_year = year;
            aq.id(R.id.btn_end_date).text(end_date);
        }
    };
    public void SplitStartDate(){
        String start_date = aq.id(R.id.btn_start_date).getText().toString();
        if(!start_date.equalsIgnoreCase("Start Date")) {
            String[] splited = start_date.split("/");
            start_day = Integer.parseInt(splited[0]);
            start_month = Integer.parseInt(splited[1])-1;
            start_year = Integer.parseInt(splited[2]);
        }
    }
    public void SplitEndDate(){
        String end_date = aq.id(R.id.btn_end_date).getText().toString();
        if(!end_date.equalsIgnoreCase("End Date")) {
            String[] splited = end_date.split("/");
            end_day = Integer.parseInt(splited[0]);
            end_month = Integer.parseInt(splited[1])-1;
            end_year = Integer.parseInt(splited[2]);
        }
    }
}
