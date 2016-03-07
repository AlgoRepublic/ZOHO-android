package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.UserListModel;
import com.algorepublic.zoho.Models.UserRoleModel;
import com.algorepublic.zoho.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by waqas on 2/15/16.
 */
public class UserService extends BaseService {
    public UserService(Activity act) {
        super(act);
    }

    public void getUserListByProject(int projectID, boolean message, CallBack obj){
        String url = Constants.GetUserListByProject_API + "projectID=" + projectID;
        this.get(url, obj, UserListModel.getInstance(), message);
        Log.e("UserService", url);
    }
    public void getAllUsers( boolean message, CallBack obj){
        String url = Constants.GetAllUserList_API;
        this.get(url, obj, UserListModel.getInstance(), message);
        Log.e("GetAllUserService", url);
    }
    public void getUserRole( boolean message, CallBack obj){
        String url = Constants.GetUserRole_API;
        this.get(url, obj, UserRoleModel.getInstance(), message);
        Log.e("GetUserRoleService", url);
    }
 
    public void updateUserWithoutProjectSelected(String ID,String firstname,String lastname,
                           String email,String mobileNo,int userRole ,ArrayList<Integer> Ids,boolean message, CallBack obj){
        String url = Constants.UpdateUser_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ID", ID);
        params.put("FirstName", firstname);
        params.put("LastName", lastname);
        params.put("Email", email);
        params.put("Mobile", mobileNo);
        params.put("RoleID", Integer.toString(userRole));
        for(int loop=0;loop<Ids.size();loop++) {
            Log.e("File", "/" + Ids);
            params.put("ProjectIDs[" + loop + "]", Integer.toString(Ids.get(loop)));
        }
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("UpdateUserWOPService", url);
    }
    public void updateUserWithProjectSelected(String ID,String firstname,String lastname,
                                                 String email,String mobileNo,int userRole ,boolean message, CallBack obj){
        String url = Constants.UpdateUser_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ID", ID);
        params.put("FirstName", firstname);
        params.put("LastName", lastname);
        params.put("Email", email);
        params.put("Mobile", mobileNo);
        params.put("RoleID", Integer.toString(userRole));
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("UpdateUserWPService", url);
    }
    public void deleteUser(String ID,boolean message, CallBack obj){
        String url = Constants.CreateUser_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ID", ID);
        this.post(url, params, obj, CreateProjectModel.getInstance(), message);
        Log.e("DeleteUserService", url);
    }
}
