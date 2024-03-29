package com.github.tibolte.agendacalendarview.models;

import java.util.Calendar;

public interface CalendarEvent {

    long getId();

    void setId(long mId);

    Calendar getStartTime();

    void setStartTime(Calendar mStartTime);

    Calendar getEndTime();

    void setEndTime(Calendar mEndTime);

    String getStartDate();
    void setPosition(int position);
    int getPosition();

    void setStartDate(String startDate);

    String getEndDate();

    void setEndDate(String endDate);

    String getTitle();

    void setTitle(String mTitle);

    Calendar getInstanceDay();

    void setInstanceDay(Calendar mInstanceDay);

    DayItem getDayReference();

    void setDayReference(DayItem mDayReference);

    WeekItem getWeekReference();

    void setWeekReference(WeekItem mWeekReference);

    CalendarEvent copy();
}
