package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by waqas on 2/3/16.
 */
public class ForumCommentModel  {

    private static ForumCommentModel _obj = null;

    private ForumCommentModel() {

    }

    public static ForumCommentModel getInstance() {
        if (_obj == null) {
            _obj = new ForumCommentModel();
        }
        return _obj;
    }

    public void setList(ForumCommentModel obj) {
        _obj = obj;
    }

    @SerializedName("responseObject")
    public ResponseObject responseObject = new ResponseObject();

    public class ResponseObject{

        @SerializedName("ForumID")
        public String forumID;

        @SerializedName("Title")
        public String title;

        @SerializedName("ForumContent")
        public String forumContent;

        @SerializedName("ProjectID")
        public String projectID;

        @SerializedName("ForumAttachments")
        @Expose
        public ArrayList<ForumAttachments> forumAttachmentses = new ArrayList<ForumAttachments>();

        @SerializedName("ForumComments")
        @Expose
        public ArrayList<ForumComments> forumCommentses = new ArrayList<ForumComments>();
    }
    public class ForumAttachments{

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

        @SerializedName("CreatedAt")
        public String createdAt;

        @SerializedName("UpdatedAt")
        public String updatedAt;
    }

}
