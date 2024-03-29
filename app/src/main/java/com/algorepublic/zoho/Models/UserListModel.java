package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by waqas on 2/15/16.
 */
public class UserListModel  {


        private static UserListModel _obj = null;

        private UserListModel() {

        }

        public static UserListModel getInstance() {
            if (_obj == null) {
                _obj = new UserListModel();
            }
            return _obj;
        }

        public void setList(UserListModel obj) {
            _obj = obj;
        }

        @SerializedName("responseObject")
        @Expose
        public ArrayList<ResponseObject> responseObject = new ArrayList<ResponseObject>();

        public class ResponseObject {

            @SerializedName("Email")
            public String email;

            @SerializedName("Mobile")
            public String mobile;

            @SerializedName("FirstName")
            public String firstName;

            @SerializedName("LastName")
            public String lastName;

            @SerializedName("NickName")
            public String nickName;


            @SerializedName("ResponseCode")
            public String responseCode;


            @SerializedName("exceptionObject")
            public String exceptionObject;

            @SerializedName("ProfileImagePath")
            public String profileImagePath;

            @SerializedName("ProfilePictureID")
            public String profilePictureID;

            @SerializedName("ID")
            public Integer ID;

            @SerializedName("UserRole")
            public UserRole userRole = new UserRole();

            @SerializedName("ProjectIDs")
            public ArrayList<Integer> projectIDs = new ArrayList<>();

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

    }
}
