package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by waqas on 2/3/16.
 */
public class ForumsCommentModel {

    private static ForumsCommentModel _obj = null;

    private ForumsCommentModel() {

    }

    public static ForumsCommentModel getInstance() {
        if (_obj == null) {
            _obj = new ForumsCommentModel();
        }
        return _obj;
    }

    public void setList(ForumsCommentModel obj) {
        _obj = obj;
    }

    @SerializedName("responseObject")
    public ResponseObject responseObject = new ResponseObject();

    public class ResponseObject{

        @SerializedName("Title")
        public String title;

        @SerializedName("ForumContent")
        public String forumContent;

        @SerializedName("ProjectID")
        public String projectID;

        @SerializedName("ForumComments")
        @Expose
        public ArrayList<ForumComments> forumComments = new ArrayList<ForumComments>();
    }

    public class ForumComments{

        @SerializedName("Message")
        public String message;

        @SerializedName("User")
        public Users user = new Users();
    }
    public class Users{
        @SerializedName("FirstName")
        public String firstName;

        @SerializedName("LastName")
        @Expose
        public String lastName;


        @SerializedName("ProfileImagePath")
        @Expose
        public String profileImagePath;

        @SerializedName("CreatedAt")
        public String createdAt;

        @SerializedName("UpdatedAt")
        public String updatedAt;
    }

}
