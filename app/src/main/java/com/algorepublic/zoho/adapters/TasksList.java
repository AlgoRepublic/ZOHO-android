package com.algorepublic.zoho.adapters;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by android on 12/29/15.
 */
public class TasksList extends ArrayList<Parcelable> implements Comparable<TasksList>{

    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }

    public void setParentTaskID(Integer parentTaskID){
        this.parentTaskID = parentTaskID;
    }
    public Integer getParentTaskID(){
        return parentTaskID;
    }

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
    public void setTaskListNameID(int taskListNameID){
        this.taskListNameID = taskListNameID;
    }
    public int getTaskListNameID(){
        return taskListNameID;
    }
    public void setProjectName(String projectName){
        this.projectName = projectName;
    }
    public String getProjectName(){
        return projectName;
    }

    public void setCommentsCount(int commentsCount){
        this.commentsCount = commentsCount;
    }
    public int getCommentsCount(){
        return commentsCount;
    }

    public void setDocumentsCount(int documentsCount){
        this.documentsCount = documentsCount;
    }
    public int getDocumentsCount(){
        return documentsCount;
    }

    public void setSubTasksCount(int subTasksCount){
        this.subTasksCount = subTasksCount;
    }
    public int getSubTasksCount(){
        return subTasksCount;
    }

    public void setProjectID(int projectID){
        this.projectID = projectID;
    }
    public int getProjectID(){
        return projectID;
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
        this.startMilli = milli;
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

    public void setHeader(String headerList){
        this.header = headerList;
    }
    public String getHeader(){
        return header;
    }


    public void setListAssignees(ArrayList<TaskListAssignee> listAssignees){
        this.listAssignees.clear();
        this.listAssignees.addAll(listAssignees);
    }
    public ArrayList<TaskListAssignee> getListAssignees(){
        return listAssignees;
    }
    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }


    @Override
    public int compareTo(TasksList object) {
        return (this.getTaskName().compareTo(object.getTaskName()));
    }

    ArrayList<TaskListAssignee> listAssignees = new ArrayList<>();
    String header;
    int priority;
    int progress;
    int documentsCount;
    int subTasksCount;
    int commentsCount;
    String startDate;
    String startMilli;
    String endMilli;
    String endDate;
    String taskName;
    int taskID;
    String taskListName;
    int taskListNameID;
    String projectName;
    int projectID;
    String description;
    Integer parentTaskID;
    int ownerId;
}
