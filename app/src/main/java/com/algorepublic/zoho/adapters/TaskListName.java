package com.algorepublic.zoho.adapters;

/**
 * Created by android on 2/9/16.
 */
public class TaskListName {
    public int getTaskListID() {
        return taskListID;
    }
    public void setTaskListID(int taskListID) {
        this.taskListID = taskListID;
    }

    public void setTaskListName(String taskListName) {
        this.taskListName = taskListName;
    }
    public String getTaskListName() {
        return taskListName;
    }


    private int taskListID ;
    private String taskListName;
}
