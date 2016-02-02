package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 2/2/16.
 */
public class ProjectsByDepartmentModel {
    private static ProjectsByDepartmentModel _obj = null;

    private ProjectsByDepartmentModel() {

    }

    public static ProjectsByDepartmentModel getInstance() {
        if (_obj == null) {
            _obj = new ProjectsByDepartmentModel();
        }
        return _obj;
    }

    public void setList(ProjectsByDepartmentModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public ArrayList<Response> responseData;

    public class Response{
        @SerializedName("Name")
        public String departmentName;

        @SerializedName("CreatedAt")
        public String createdAt;

        @SerializedName("UpdatedAt")
        public String updatedAt;

        @SerializedName("ID")
        public Integer ID;

        @SerializedName("Projects")
        public ArrayList<Projects> projects;
    }
    public class Projects{

        @SerializedName("Name")
        public String projectName;

        @SerializedName("Description")
        public String description;

        @SerializedName("CreatedAt")
        public String createdAt;

        @SerializedName("UpdatedAt")
        public String updatedAt;

        @SerializedName("ID")
        public Integer projectID;
    }
    @SerializedName("ResponseCode")
    public String responseCode;

    @SerializedName("exceptionObject")
    public String exceptionObject;

}
