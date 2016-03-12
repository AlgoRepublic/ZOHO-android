package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 1/11/16.
 */
public class CreateCommentModel {
    private static CreateCommentModel _obj = null;

    private CreateCommentModel() {

    }

    public static CreateCommentModel getInstance() {
        if (_obj == null) {
            _obj = new CreateCommentModel();
        }
        return _obj;
    }

    public void setList(CreateCommentModel obj) {
        _obj = obj;
    }

    public class ResponseObject {

        @SerializedName("CommentID")
        public String Id;

        @SerializedName("Message")
        public String message;

        @SerializedName("CreatedAt")
        public String createdAt;

        @SerializedName("UpdatedAt")
        public String updatedAt;

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
    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("responseObject")
    public ResponseObject responseObject;


}

