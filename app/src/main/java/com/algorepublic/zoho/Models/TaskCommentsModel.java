package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

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
    public ResponseObject responseObject = new ResponseObject();

    @SerializedName("ResponseCode")
    public String responseCode;

    public class ResponseObject {

        @SerializedName("ID")
        public String Id;

        @SerializedName("Email")
        public String eMail;

        @SerializedName("FirstName")
        public String firstName;

    }

}
