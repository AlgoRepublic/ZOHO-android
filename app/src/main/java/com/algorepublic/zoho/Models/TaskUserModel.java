package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 1/4/16.
 */
public class TaskUserModel {
    private static TaskUserModel _obj = null;

    private TaskUserModel() {

    }

    public static TaskUserModel getInstance() {
        if (_obj == null) {
            _obj = new TaskUserModel();
        }
        return _obj;
    }

    public void setList(TaskUserModel obj) {
        _obj = obj;
    }


    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("exceptionObject")
    public Object exceptionObject;

    @SerializedName("responseObject")
    @Expose
    public ArrayList<ResponseObject> responseObject = new ArrayList<ResponseObject>();

    public class ResponseObject{

        @SerializedName("ID")
        @Expose
        public Integer ID;

        @SerializedName("Email")
        @Expose
        public String email;

        @SerializedName("FirstName")
        public String firstName;

    }

}
