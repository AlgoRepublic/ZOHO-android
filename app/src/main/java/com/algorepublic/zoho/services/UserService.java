package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.Models.TaskAssigneeModel;
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
    public void getAllUserList( boolean message, CallBack obj){
        String url = Constants.GetAllUserList_API;
        this.get(url, obj, TaskAssigneeModel.getInstance(), message);
        Log.e("GetAllService", url);
    }
    public void getUserRole( boolean message, CallBack obj){
        String url = Constants.GetUserRole_API;
        this.get(url, obj, TaskAssigneeModel.getInstance(), message);
        Log.e("GetUserRoleService", url);
    }
    public void deleteUser(String ID,boolean message, CallBack obj){
        String url = Constants.CreateUser_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ID", ID);
        this.post(url, params, obj, CreateProjectModel.getInstance(), message);
        Log.e("CreateUserService", url);
    }
}
