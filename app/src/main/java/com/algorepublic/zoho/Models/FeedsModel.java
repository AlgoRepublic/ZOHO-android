package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 3/29/16.
 */
public class FeedsModel {
    private static FeedsModel _obj = null;

    private FeedsModel() {

    }

    public static FeedsModel getInstance() {
        if (_obj == null) {
            _obj = new FeedsModel();
        }
        return _obj;
    }

    public void setList(FeedsModel obj) {
        _obj = obj;
    }

    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("exceptionObject")
    public Object exceptionObject;

    @SerializedName("responseObject")
    @Expose
    public ArrayList<ResponseObject> responseObject = new ArrayList<>();

    public class ResponseObject {

        @SerializedName("Message")
        public String message;

        @SerializedName("UserId")
        public Integer userId;

        @SerializedName("ProjectId")
        public Integer projectId;

        @SerializedName("UpdateType")
        public Integer updateType;

        @SerializedName("UserName")
        public String userName;

        @SerializedName("CreatedAt")
        public String createdAt;

        @SerializedName("Comments")
        @Expose
        public ArrayList<Comments> comments = new ArrayList<>();

        @SerializedName("User")
        public User user = new User();

    }
    public class User {

        @SerializedName("ProfileImagePath")
        public String profileImagePath;

    }
    private class Comments{

    }

}
