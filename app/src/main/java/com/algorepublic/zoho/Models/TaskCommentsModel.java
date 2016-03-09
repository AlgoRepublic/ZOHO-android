package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 1/4/16.
 */
public class TaskCommentsModel {
    private static TaskCommentsModel _obj = null;

    private TaskCommentsModel() {

    }

    public static TaskCommentsModel getInstance() {
        if (_obj == null) {
            _obj = new TaskCommentsModel();
        }
        return _obj;
    }

    public void setList(TaskCommentsModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public ArrayList<ResponseObject> responseObject = new ArrayList<>();

    @SerializedName("ResponseCode")
    public String responseCode;

    public class ResponseObject {

        @SerializedName("ID")
        public String Id;

        @SerializedName("Message")
        public String message;

        @SerializedName("CreatedAt")
        public String createdAt;

        @SerializedName("User")
        @Expose
        public User userObject = new User();
    }
    public class User {

        @SerializedName("ID")
        @Expose
        public Integer ID;

        @SerializedName("FirstName")
        @Expose
        public String firstName;

        @SerializedName("LastName")
        @Expose
        public String lastName;

        @SerializedName("ProfileImagePath")
        @Expose
        public String profileImagePath;

        @SerializedName("ProfilePictureID")
        @Expose
        public Integer profilePictureID;
    }

}
