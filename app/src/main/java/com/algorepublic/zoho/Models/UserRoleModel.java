package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 3/4/16.
 */
public class UserRoleModel {
    private static UserRoleModel _obj = null;

    private UserRoleModel() {

    }

    public static UserRoleModel getInstance() {
        if (_obj == null) {
            _obj = new UserRoleModel();
        }
        return _obj;
    }

    public void setList(UserRoleModel obj) {
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

        @SerializedName("Role")
        @Expose
        public String role;

        @SerializedName("Role_AR")
        public String role_AR;

    }

}
