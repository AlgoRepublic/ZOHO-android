package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 12/11/15.
 */
public class GetUserModel {
    private static GetUserModel _obj = null;

    private GetUserModel() {

    }

    public static GetUserModel getInstance() {
        if (_obj == null) {
            _obj = new GetUserModel();
        }
        return _obj;
    }

    public void setList(GetUserModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public User user = new User();

    @SerializedName("ResponseCode")
    public String responseCode;

    public class User {

        @SerializedName("ID")
        public String Id;

        @SerializedName("Email")
        public String eMail;

        @SerializedName("FirstName")
        public String firstName;

    }

}
