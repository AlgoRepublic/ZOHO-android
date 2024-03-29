package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProjectsByClientModel {
    private static ProjectsByClientModel _obj = null;

    private ProjectsByClientModel() {

    }

    public static ProjectsByClientModel getInstance() {
        if (_obj == null) {
            _obj = new ProjectsByClientModel();
        }
        return _obj;
    }

    public void setList(ProjectsByClientModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public ArrayList<Response> responseData;

    public class Response{
        @SerializedName("CompanyName")
        public String companyName;

        @SerializedName("CreatedAt")
        public String createdAt;

        @SerializedName("UpdatedAt")
        public String updatedAt;

        @SerializedName("ID")
        public String ID;

        @SerializedName("Projects")
        public ArrayList<Projects> projects;
    }
    public class Projects{

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
