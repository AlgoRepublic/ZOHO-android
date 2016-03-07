package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 3/4/16.
 */
public class AllProjectsModel {
    private static AllProjectsModel _obj = null;

    private AllProjectsModel() {

    }

    public static AllProjectsModel getInstance() {
        if (_obj == null) {
            _obj = new AllProjectsModel();
        }
        return _obj;
    }

    public void setList(AllProjectsModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public ArrayList<Response> responseData;

    public class Response{

        @SerializedName("Name")
        public String projectName;

        @SerializedName("ID")
        public Integer projectID;
    }

    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("exceptionObject")
    public String exceptionObject;

}
