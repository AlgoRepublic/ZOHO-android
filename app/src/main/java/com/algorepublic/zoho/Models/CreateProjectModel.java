package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 2/17/16.
 */
public class CreateProjectModel {
    private static CreateProjectModel _obj = null;

    private CreateProjectModel() {

    }

    public static CreateProjectModel getInstance() {
        if (_obj == null) {
            _obj = new CreateProjectModel();
        }
        return _obj;
    }

    public void setList(CreateProjectModel obj) {
        _obj = obj;
    }


    @SerializedName("ResponseCode")
    public String responseCode;

    @SerializedName("responseObject")
    public Object responseObject;

}
