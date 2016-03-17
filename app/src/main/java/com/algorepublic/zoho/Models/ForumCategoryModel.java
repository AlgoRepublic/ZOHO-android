package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by waqas on 2/24/16.
 */
public class ForumCategoryModel
{

    private static ForumCategoryModel _obj = null;

    private ForumCategoryModel() {

    }

    public static ForumCategoryModel getInstance() {
        if (_obj == null) {
            _obj = new ForumCategoryModel();
        }
        return _obj;
    }

    public void setList(ForumCategoryModel obj) {
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

        @SerializedName("CategoryName")
        public String categoryName;

        @SerializedName("ProjectID")
        public Integer ProjectID;

        @SerializedName("ID")
        public Integer ID;

    }
}
