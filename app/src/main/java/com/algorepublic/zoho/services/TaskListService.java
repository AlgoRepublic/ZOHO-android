package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.GetUserModel;
import com.algorepublic.zoho.Models.TasksListModel;
import com.algorepublic.zoho.Models.UserModel;
import com.algorepublic.zoho.utils.Constants;

import java.util.HashMap;

/**
 * Created by android on 12/23/15.
 */
public class TaskListService extends BaseService {

    public TaskListService(Activity act) {
        super(act);
    }

    public void getTasksList(boolean message, CallBack obj){
        String url = Constants.GetTaskList_API;
        this.get(url, obj, TasksListModel.getInstance(), message);
        Log.e("TaskListService", url);
    }
}
