package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.Models.UserListModel;
import com.algorepublic.zoho.utils.Constants;

import java.util.HashMap;

/**
 * Created by waqas on 2/15/16.
 */
public class UserService extends BaseService {
    public UserService(Activity act) {
        super(act);
    }

    public void getListByProject(int projectID, boolean message, CallBack obj){
        String url = Constants.GetUserListByProject_API + "projectID=" + projectID;
        this.get(url, obj, UserListModel.getInstance(), message);
        Log.e("UserService", url);
    }
    public void createUser(String firstname,String lastname, String email,int mobileNo, String deptID
            ,boolean isPrivate, boolean message, CallBack obj){
        String url = Constants.CreateUser_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("FirstName", firstname);
        params.put("LastName", lastname);
        params.put("Email", email);
        params.put("Mobile", String.valueOf(mobileNo));
        this.post(url, params, obj, CreateProjectModel.getInstance(), message);
        Log.e("CreateUserService", url);
    }
}
