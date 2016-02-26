package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by waqas on 2/24/16.
 */
public class AddforumModel
{

    private static AddforumModel _obj = null;

    private AddforumModel() {

    }

    public static AddforumModel getInstance() {
        if (_obj == null) {
            _obj = new AddforumModel();
        }
        return _obj;
    }

    public void setList(AddforumModel obj) {
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

        @SerializedName("ForumID")
        @Expose
        public Integer ForumID;

        @SerializedName("CategoryId")
        @Expose
        public Integer CategoryId;

        @SerializedName("Title")
        public String Title;

        @SerializedName("ProjectID")
        public Integer ProjectID;

        @SerializedName("ID")
        public Integer ID;

    }
}
