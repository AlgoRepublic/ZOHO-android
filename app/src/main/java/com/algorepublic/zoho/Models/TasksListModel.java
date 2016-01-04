package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 12/23/15.
 */
public class TasksListModel {
    private static TasksListModel _obj = null;

    private TasksListModel() {

    }

    public static TasksListModel getInstance() {
        if (_obj == null) {
            _obj = new TasksListModel();
        }
        return _obj;
    }

    public void setList(TasksListModel obj) {
        _obj = obj;
    }


    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("exceptionObject")
    public Object exceptionObject;

    @SerializedName("responseObject")
    @Expose
    public ArrayList<ResponseObject> responseObject = new ArrayList<ResponseObject>();

    public class ResponseObject{

        @SerializedName("Title")
        @Expose
        public String title;

        @SerializedName("Description")
        public String description;

        @SerializedName("projectName")
        public String projectName;

        @SerializedName("ResponsibelID")
        public int responsibelID;

        @SerializedName("Priority")
        public int priority;

        @SerializedName("OwnerID")
        public int ownerID;

        @SerializedName("CreateStatusId")
        public int createStatusId;

        @SerializedName("StartDate")
        public String startDate;

        @SerializedName("EndDate")
        public String endDate;

        @SerializedName("ProjectID")
        public int projectID;

        @SerializedName("MilestoneID")
        public Integer milestoneID;

        @SerializedName("MilestoneName")
        public String milestoneName;

        @SerializedName("ResponsibleName")
        public String responsibleName;

        @SerializedName("OwnerName")
        public String ownerName;

        @SerializedName("IsAttach")
        public int isAttach;

        @SerializedName("TaskListName")
        public String taskListName;
    }

}
