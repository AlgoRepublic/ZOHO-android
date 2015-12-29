package com.algorepublic.zoho.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by android on 12/29/15.
 */
public class ChildTasksList implements Comparable<ChildTasksList> {


    public void setTaskName(String taskName){
        this.taskName = taskName;
    }
    public String getTaskName(){
        return taskName;
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
    @Override
    public int compareTo(ChildTasksList o) {
        return getMilli().compareTo(o.getMilli());
    }
    String startDate;
    String milli;
    String endDate;
    String taskName;
    String projectName;

}
