package com.algorepublic.zoho.adapters;

import java.util.Comparator;

/**
 * Created by android on 12/29/15.
 */
public class TasksList implements Comparable<TasksList>{


    public void setTaskName(String taskName){
        this.taskName = taskName;
    }
    public String getTaskName(){
        return taskName;
    }
    public void setPriority(int priority){
        this.priority = priority;
    }
    public int getPriority(){
        return priority;
    }
    public void setProjectName(String projectName){
        this.projectName = projectName;
    }
    public String getProjectName(){
        return projectName;
    }
    public void setStartDate(String startDate){
        this.startDate = startDate;
    }
    public String getStartDate(){
        return startDate;
    }
    public void setEndDate(String endDate){
        this.endDate = endDate;
    }
    public String getEndDate(){
        return endDate;
    }

    public void setMilli(String milli){
        this.milli = milli;
    }
    public String getMilli(){
        return milli;
    }

    public void setHeaderID(long headerid){
        headerID = headerid;
    }
    public long getHeaderID(){
        return headerID;
    }
    public void setHeader(String headerList){
        this.header = headerList;
    }
    public String getHeader(){
        return header;
    }


    @Override
    public int compareTo(TasksList object) {
        return (this.getTaskName().compareTo(object.getTaskName()));
    }


    long headerID;
    String header;
    int priority;
    String startDate;
    String milli;
    String endDate;
    String taskName;
    String projectName;
}
