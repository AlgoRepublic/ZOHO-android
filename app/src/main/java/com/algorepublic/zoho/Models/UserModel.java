package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    private static UserModel _obj = null;

    private UserModel() {

    }

    public static UserModel getInstance() {
        if (_obj == null) {
            _obj = new UserModel();
        }
        return _obj;
    }

    public void setList(UserModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public String userToken;

    @SerializedName("ResponseCode")
    public String responseCode;

}
