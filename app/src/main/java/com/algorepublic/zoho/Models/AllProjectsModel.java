package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 2/23/16.
 */
public class AllProjectsModel {
    private static AllProjectsModel _obj = null;

    private AllProjectsModel() {

    }

    public static AllProjectsModel getInstance() {
        if (_obj == null) {
            _obj = new AllProjectsModel();
        }
        return _obj;
    }

    public void setList(AllProjectsModel obj) {
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
        public String projectID;

        @SerializedName("TotlTasksNo")
        public String totalTasks;

        @SerializedName("UsersCount")
        public String usersCount;

        @SerializedName("TotalMilestonesNo")
        public String toalMilestones;

        @SerializedName("IsDeleted")
        public boolean IsDeleted;

        @SerializedName("Isprivate")
        public boolean Isprivate;
    }

    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("exceptionObject")
    public String exceptionObject;

}
