package com.algorepublic.zoho.adapters;

/**
 * Created by android on 12/29/15.
 */
public class TasksList implements Comparable<TasksList>{


    public void setTaskID(int taskID){
        this.taskID = taskID;
    }
    public int getTaskID(){
        return taskID;
    }
    public void setTaskName(String taskName){
        this.taskName = taskName;
    }
    public String getTaskName(){
        return taskName;
    }
    public void setTaskListName(String taskListName){
        this.taskListName = taskListName;
    }
    public String getTaskListName(){
        return taskListName;
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
    public void setPriority(int priority){ this.priority = priority; }
    public int getPriority(){ return priority; }

    public void setProgress(int progress){ this.progress = progress; }
    public int getProgress(){ return progress; }

    public void setStartMilli(String milli){
        this.startDate = milli;
    }
    public String getStartMilli(){
        return startMilli;
    }
    public void setEndMilli(String milli){
        this.endMilli = milli;
    }
    public String getEndMilli(){
        return endMilli;
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

    public void setCharToAscii(long charToAscii){
        this.charToAscii = charToAscii;
    }
    public long getCharToAscii(){
        return charToAscii;
    }


    @Override
    public int compareTo(TasksList object) {
        return (this.getTaskName().compareTo(object.getTaskName()));
    }


    long headerID;
    String header;
    int priority;
    int progress;
    long charToAscii;
    String startDate;
    String startMilli;
    String endMilli;
    String endDate;
    String taskName;
    int taskID;
    String taskListName;
    String projectName;
}
