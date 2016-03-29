package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 2/23/16.
 */
public class AllProjectsByUserModel {
    private static AllProjectsByUserModel _obj = null;

    private AllProjectsByUserModel() {

    }

    public static AllProjectsByUserModel getInstance() {
        if (_obj == null) {
            _obj = new AllProjectsByUserModel();
        }
        return _obj;
    }

    public void setList(AllProjectsByUserModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public ArrayList<Response> responseData;

    public class Response{

        @SerializedName("Name")
        public String projectName;

        @SerializedName("Description")
        public String description;

        @SerializedName("OwnerID")
        public String ownerID;

        @SerializedName("OwnerN")
        public String ownerName;

        @SerializedName("CreatedAt")
        public String createdAt;

        @SerializedName("UpdatedAt")
        public String updatedAt;

        @SerializedName("ID")
        public Integer projectID;

        @SerializedName("TotlTasksNo")
        public Integer totalTasks;

        @SerializedName("UsersCount")
        public String usersCount;

        @SerializedName("TotalMilestonesNo")
        public Integer toalMilestones;

        @SerializedName("IsDeleted")
        public boolean IsDeleted;

        @SerializedName("Isprivate")
        public boolean Isprivate;

        @SerializedName("CompletedTasksNo")
        public Integer completedTasksNo;

        @SerializedName("CompletedTaskList")
        public Integer completedTaskList;

        @SerializedName("CompletedMilestonesNo")
        public Integer completedMilestonesNo;

        @SerializedName("TotalTaskList")
        public Integer totalTaskList;
    }

    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("exceptionObject")
    public String exceptionObject;

}
