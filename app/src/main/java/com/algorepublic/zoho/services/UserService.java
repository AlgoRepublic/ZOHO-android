package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.UserListModel;
import com.algorepublic.zoho.utils.Constants;

/**
 * Created by waqas on 2/15/16.
 */
public class UserService extends BaseService {
    public UserService(Activity act) {
        super(act);
    }

    public void getListByProject(int projectID, boolean message, CallBack obj){
        String url = Constants.GetListByProject_User+ "projectID=" + projectID;
        this.get(url, obj, UserListModel.getInstance(), message);
        Log.e("UserService", url);
    }
}
