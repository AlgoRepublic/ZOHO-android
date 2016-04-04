package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.CreateForumModel;
import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.Models.DepartmentsModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.ProjectsByClientModel;
import com.algorepublic.zoho.utils.Constants;

import java.util.HashMap;

/**
 * Created by android on 2/18/16.
 */
public class DepartmentService extends BaseService {

    public DepartmentService(Activity act) {
        super(act);
    }

    public void addProjectIntoDepartment(String did,String pid, String userID, boolean message, CallBack obj){
        String url = Constants.AddProjectIntoDepartment;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("pid", pid);
        params.put("did", did);
        params.put("userID", userID);
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("AddToDeptService", url);
    }
    public void createDepartment(String name,String createBy, boolean message, CallBack obj){
        String url = Constants.CreateDepartment_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Name", name);
        params.put("CreateBy", createBy);
        this.post(url, params, obj, CreateForumModel.getInstance(), message);
        Log.e("CreateDepartmentService", url);
    }
    public void updateDepartment(String Id,String name,String updateBy, boolean message, CallBack obj){
        String url = Constants.UpdateDepartment_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Name", name);
        params.put("UpdateBy", updateBy);
        params.put("ID", Id);
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("UpdateDepartmentService", url);
    }
    public void DeleteDepartment(String ID,String userId, boolean message, CallBack obj){
        String url = Constants.DeleteDepartment_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("departmentID", ID);
        params.put("userID", userId);
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("DeleteService", url);
    }
}
