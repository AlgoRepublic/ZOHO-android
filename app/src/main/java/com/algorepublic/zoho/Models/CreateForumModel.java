package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by waqas on 2/24/16.
 */
public class CreateForumModel {
    private static CreateForumModel _obj = null;

    private CreateForumModel() {

    }

    public static CreateForumModel getInstance() {
        if (_obj == null) {
            _obj = new CreateForumModel();
        }
        return _obj;
    }

    public void setList(CreateForumModel obj) {
        _obj = obj;
    }


    @SerializedName("ResponseCode")
    public String responseCode;

    @SerializedName("responseObject")
    public ResponseObject responseObject = new ResponseObject();


    public class ResponseObject {
    }

}
