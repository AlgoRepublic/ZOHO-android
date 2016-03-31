package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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

        @SerializedName("LastName")
        @Expose
        public String lastName;

        @SerializedName("NickName")
        @Expose
        public String nickName;

        @SerializedName("Mobile")
        @Expose
        public String mobile;

        @SerializedName("ProfileImagePath")
        @Expose
        public String profileImagePath;

        @SerializedName("ProfilePictureID")
        @Expose
        public Integer profilePictureID;

        @SerializedName("UserRole")
        public UserRole userRole = new UserRole();

    }

    public class UserRole {

        @SerializedName("ID")
        @Expose
        public Integer ID;

        @SerializedName("Role")
        @Expose
        public String role;

        @SerializedName("Role_AR")
        public String role_AR;

        @SerializedName("PermissionIds")
        public ArrayList<Integer> permissionIds = new ArrayList<>();

    }

}
