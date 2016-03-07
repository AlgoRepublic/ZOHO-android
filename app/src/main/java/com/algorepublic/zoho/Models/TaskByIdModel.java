package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 3/7/16.
 */
public class TaskByIdModel {
    private static TaskByIdModel _obj = null;

    private TaskByIdModel() {

    }

    public static TaskByIdModel getInstance() {
        if (_obj == null) {
            _obj = new TaskByIdModel();
        }
        return _obj;
    }

    public void setList(TaskByIdModel obj) {
        _obj = obj;
    }


    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("exceptionObject")
    public Object exceptionObject;

    @SerializedName("responseObject")
    @Expose
    public Tasks responseObject = new Tasks();

    public class Tasks{

        @SerializedName("ID")
        @Expose
        public Integer taskID;

        @SerializedName("Title")
        @Expose
        public String title;

        @SerializedName("Description")
        public String description;

        @SerializedName("projectName")
        public String projectName;


        @SerializedName("Priority")
        public int priority;

        @SerializedName("Progess")
        public int progress;

        @SerializedName("OwnerID")
        public int ownerID;

        @SerializedName("StartDate")
        public String startDate;

        @SerializedName("EndDate")
        public String endDate;

        @SerializedName("ProjectID")
        public int projectID;

        @SerializedName("DocumentsCount")
        public Integer documentsCount;

        @SerializedName("SubTasksCount")
        public Integer subTasksCount;

        @SerializedName("CommentsCount")
        public Integer commentsCount;

        @SerializedName("ParentTaskID")
        public Integer parentTaskID;

        @SerializedName("Users")
        @Expose
        public ArrayList<Users> userObject = new ArrayList<Users>();
    }
    public class Users {

        @SerializedName("ID")
        @Expose
        public Integer responsibleID;

        @SerializedName("FirstName")
        @Expose
        public String firstName;

        @SerializedName("LastName")
        @Expose
        public String lastName;

        @SerializedName("ProfileImagePath")
        @Expose
        public String profileImagePath;
    }
}
