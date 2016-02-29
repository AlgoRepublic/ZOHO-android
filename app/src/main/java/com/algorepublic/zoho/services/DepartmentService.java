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

    public void getDepartmentList_API( boolean message, CallBack obj){
        String url = Constants.GetDepartment_API;
        this.get(url, obj, DepartmentsModel.getInstance(), message);
        Log.e("DepartmentsService", url);
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
        this.post(url, params, obj, CreateForumModel.getInstance(), message);
        Log.e("UpdateDepartmentService", url);
    }
    public void DeleteProject(String ID, boolean message, CallBack obj){
        String url = Constants.DeleteDepartment_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ID", ID);
        this.post(url, params, obj, CreateForumModel.getInstance(), message);
        Log.e("DeleteService", url);
    }
}
