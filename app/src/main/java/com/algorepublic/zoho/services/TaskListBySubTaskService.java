package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.TaskListBySubTaskModel;
import com.algorepublic.zoho.utils.Constants;

/**
 * Created by waqas on 2/10/16.
 */
public class TaskListBySubTaskService extends BaseService {

    public TaskListBySubTaskService(Activity act) {
        super(act);
    }

    public void getSubTaskById(int subTaskID, boolean message, CallBack obj){
        String url = Constants.GetSubTakById_API+ "id=" + subTaskID;
        this.get(url, obj, TaskListBySubTaskModel.getInstance(), message);
        Log.e("ForumsListService", url);
    }
}
