package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/12/16.
 */
public class CreateForumCommentModel {
    private static CreateForumCommentModel _obj = null;

    private CreateForumCommentModel() {

    }

    public static CreateForumCommentModel getInstance() {
        if (_obj == null) {
            _obj = new CreateForumCommentModel();
        }
        return _obj;
    }

    public void setList(CreateForumCommentModel obj) {
        _obj = obj;
    }

    public class ResponseObject {

        @SerializedName("ID")
        public String Id;

        @SerializedName("Message")
        public String message;

        @SerializedName("CreatedAt")
        public String createdAt;

        @SerializedName("UpdatedAt")
        public String updatedAt;

    }
    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("responseObject")
    public ResponseObject responseObject;


}

