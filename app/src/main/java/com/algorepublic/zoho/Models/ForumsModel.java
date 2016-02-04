package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by waqas on 2/2/16.
 */
public class ForumsModel {
    private static ForumsModel _obj = null;

    private ForumsModel() {

    }

    public static ForumsModel getInstance() {
        if (_obj == null) {
            _obj = new ForumsModel();
        }
        return _obj;
    }

    public void setList(ForumsModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    @Expose
    public ArrayList<ResponseObject> responseObject = new ArrayList<ResponseObject>();

    public class ResponseObject{

        @SerializedName("Title")
        public String title;

        @SerializedName("ForumContent")
        public String forumContent;

        @SerializedName("ProjectID")
        public String projectID;


        @SerializedName("ForumComments")
        public String forumComments;

        @SerializedName("UpdatedAt")
        public String updatedAt;

        @SerializedName("CreateBy")
        public String createBy;

        @SerializedName("UpdateBy")
        public String updateBy;

        @SerializedName("ID")
        public Integer ID;

        @SerializedName("User")
        public Users user = new Users();

    }
    public class Users {

        @SerializedName("FirstName")
        @Expose
        public String firstName;

        @SerializedName("LastName")
        @Expose
        public String lastName;

        @SerializedName("UserNorifications")
        @Expose
        public String userNorifications;

        @SerializedName("CreatedAt")
        @Expose
        public String createdAt;

        @SerializedName("UpdatedAt")
        @Expose
        public String updatedAt;

        @SerializedName("UpdateBy")
        @Expose
        public String updateBy;

        @SerializedName("ID")
        @Expose
        public String ID;

    }
    @SerializedName("ResponseCode")
    public String responseCode;

    @SerializedName("exceptionObject")
    public String exceptionObject;

}
