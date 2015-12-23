package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
        public String Title;

        @SerializedName("Description")
        public String Description;

        @SerializedName("projectName")
        public String projectName;

        @SerializedName("ResponsibelID")
        public Integer ResponsibelID;

        @SerializedName("Priority")
        public Integer Priority;

        @SerializedName("OwnerID")
        public Integer OwnerID;

        @SerializedName("CreateStatusId")
        public Integer CreateStatusId;

        @SerializedName("StartDate")
        public String StartDate;

        @SerializedName("EndDate")
        public String EndDate;

        @SerializedName("ProjectID")
        public Integer ProjectID;

        @SerializedName("MilestoneID")
        public String MilestoneID;

        @SerializedName("MilestoneName")
        public Integer MilestoneName;

        @SerializedName("ResponsibleName")
        public String ResponsibleName;

        @SerializedName("OwnerName")
        public String OwnerName;

        @SerializedName("IsAttach")
        public Integer IsAttach;
    }

}
