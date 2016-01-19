package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.ProjectsModel;
import com.algorepublic.zoho.utils.Constants;

/**
 * Created by android on 12/23/15.
 */
public class ProjectsListService extends BaseService {

    public ProjectsListService(Activity act) {
        super(act);
    }

    public void getProjectsList(String ownerID, boolean message, CallBack obj){
        String url = Constants.GetProjectsList_API+ "?OwnerID=" + ownerID;
        this.get(url, obj, ProjectsModel.getInstance(), message);
        Log.e("TaskListService", url);
    }

}
