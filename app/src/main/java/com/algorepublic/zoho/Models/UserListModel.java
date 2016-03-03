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


            @SerializedName("ResponseCode")
            public String responseCode;


            @SerializedName("exceptionObject")
            public String exceptionObject;

            @SerializedName("ProfileImagePath")
            public String profileImagePath;

            @SerializedName("ID")
            public Integer ID;

        }


}
