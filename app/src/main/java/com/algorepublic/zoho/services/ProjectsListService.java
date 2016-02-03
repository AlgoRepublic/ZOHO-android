package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.ProjectsByClientModel;
import com.algorepublic.zoho.Models.ProjectsByDepartmentModel;
import com.algorepublic.zoho.utils.Constants;

/**
 * Created by android on 12/23/15.
 */
public class ProjectsListService extends BaseService {

    public ProjectsListService(Activity act) {
        super(act);
    }

    public void getProjectsByClient_API(String ownerID, boolean message, CallBack obj){
        String url = Constants.GetProjectsByClient_API + "?OwnerID=" + ownerID;
        this.get(url, obj, ProjectsByClientModel.getInstance(), message);
        Log.e("ProjectsClientService", url);
    }
    public void getProjectsByDepartment(String ownerID, boolean message, CallBack obj){
        String url = Constants.GetProjectsByDepartment_API + "?OwnerID=" + ownerID;
        this.get(url, obj, ProjectsByDepartmentModel.getInstance(), message);
        Log.e("ProjectsDeptService", url);
    }

}
