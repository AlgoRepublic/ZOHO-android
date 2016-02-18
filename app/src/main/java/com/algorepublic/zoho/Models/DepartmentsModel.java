package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 2/18/16.
 */
public class DepartmentsModel {
    private static DepartmentsModel _obj = null;

    private DepartmentsModel() {

    }

    public static DepartmentsModel getInstance() {
        if (_obj == null) {
            _obj = new DepartmentsModel();
        }
        return _obj;
    }

    public void setList(DepartmentsModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public ArrayList<Response> responseData;

    public class Response{
        @SerializedName("Name")
        public String name;

        @SerializedName("CreatedAt")
        public String createdAt;

        @SerializedName("UpdatedAt")
        public String updatedAt;

        @SerializedName("IsDeleted")
        public boolean IsDeleted;

        @SerializedName("ID")
        public String ID;
    }

    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("exceptionObject")
    public String exceptionObject;

}
