package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 1/4/16.
 */
public class TaskAssigneeModel {
    private static TaskAssigneeModel _obj = null;

    private TaskAssigneeModel() {

    }

    public static TaskAssigneeModel getInstance() {
        if (_obj == null) {
            _obj = new TaskAssigneeModel();
        }
        return _obj;
    }

    public void setList(TaskAssigneeModel obj) {
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

        @SerializedName("ID")
        @Expose
        public Integer taskID;

        @SerializedName("Email")
        @Expose
        public String email;

        @SerializedName("FirstName")
        public String firstName;

        @SerializedName("IsDeleted")
        public String isDeleted;

//        @SerializedName("ResponsibelID")
//        public int responsibelID;
//
//        @SerializedName("Priority")
//        public int priority;
//
//        @SerializedName("Progess")
//        public int progress;
//
//        @SerializedName("OwnerID")
//        public int ownerID;
//
//        @SerializedName("CreateStatusId")
//        public int createStatusId;
//
//        @SerializedName("StartDate")
//        public String startDate;
//
//        @SerializedName("EndDate")
//        public String endDate;
//
//        @SerializedName("ProjectID")
//        public int projectID;
//
//        @SerializedName("MilestoneID")
//        public Integer milestoneID;
//
//        @SerializedName("MilestoneName")
//        public String milestoneName;
//
//        @SerializedName("ResponsibleName")
//        public String responsibleName;
//
//        @SerializedName("OwnerName")
//        public String ownerName;
//
//        @SerializedName("IsAttach")
//        public int isAttach;
//
//        @SerializedName("TaskListName")
//        public String taskListName;
    }

}
