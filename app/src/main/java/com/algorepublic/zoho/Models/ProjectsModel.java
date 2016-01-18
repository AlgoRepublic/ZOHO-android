package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProjectsModel {
    private static ProjectsModel _obj = null;

    private ProjectsModel() {

    }

    public static ProjectsModel getInstance() {
        if (_obj == null) {
            _obj = new ProjectsModel();
        }
        return _obj;
    }

    public void setList(ProjectsModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public ArrayList<Response> reponseData;

    public class Response{
        @SerializedName("Name")
        public String name;

        @SerializedName("Description")
        public String desc;

        @SerializedName("OwnerID")
        public String ownerID;

        @SerializedName("CreatedAt")
        public String createdAt;

        @SerializedName("UpdatedAt")
        public String updatedAt;

        @SerializedName("ID")
        public String projectID;
    }

    @SerializedName("ResponseCode")
    public String responseCode;

    @SerializedName("exceptionObject")
    public String exceptionObject;

}
