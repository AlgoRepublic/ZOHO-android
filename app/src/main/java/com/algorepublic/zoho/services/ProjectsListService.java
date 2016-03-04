package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.AllProjectsByUserModel;
import com.algorepublic.zoho.Models.AllProjectsModel;
import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.ProjectsByClientModel;
import com.algorepublic.zoho.Models.ProjectsByDepartmentModel;
import com.algorepublic.zoho.utils.Constants;

import java.util.HashMap;

/**
 * Created by android on 12/23/15.
 */
public class ProjectsListService extends BaseService {

    public ProjectsListService(Activity act) {
        super(act);
    }

    public void getAllProjectsByUser_API(String UserID, boolean message, CallBack obj){
        String url = Constants.GetAllProjectsByUser_API +"userID="+UserID ;
        this.get(url, obj, AllProjectsByUserModel.getInstance(), message);
        Log.e("AllProjByUserService", url);
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
    public void createProject(String name,String createBy, String desc,int ownerID, String deptID
            ,boolean isPrivate, boolean message, CallBack obj){
        String url = Constants.CreateProject_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Name", name);
        params.put("CreateBy", String.valueOf(createBy));
        params.put("Description", desc);
        params.put("OwnerID", String.valueOf(ownerID));
        params.put("Isprivate", String.valueOf(isPrivate));
        params.put("ShowOverview", String.valueOf(false));
        params.put("DepartmentID", deptID);
        this.post(url, params, obj, CreateProjectModel.getInstance(), message);
        Log.e("CreateProjectService", url);
    }
    public void updateProject(String ID,String name,String createBy, String desc,int ownerID,boolean status, String deptID
            ,boolean isPrivate, boolean message, CallBack obj){
        String url = Constants.UpdateProject_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ID", ID);
        params.put("Name", name);
        params.put("CreateBy", String.valueOf(createBy));
        params.put("Description", desc);
        params.put("OwnerID", String.valueOf(ownerID));
        params.put("ProjectStatusID", String.valueOf(status));
        params.put("Isprivate", String.valueOf(isPrivate));
        params.put("ShowOverview", String.valueOf(false));
        params.put("DepartmentID", deptID);
        this.post(url, params, obj, CreateProjectModel.getInstance(), message);
        Log.e("UpdateProjectService", url);
    }
    public void DeleteProject(String ID, boolean message, CallBack obj){
        String url = Constants.DeleteProject_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("projectID", ID);
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("DeleteService", url);
    }
}
