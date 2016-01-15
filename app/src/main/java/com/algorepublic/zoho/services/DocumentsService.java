package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.DocumentsListModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.GetUserModel;
import com.algorepublic.zoho.Models.UserModel;
import com.algorepublic.zoho.utils.Constants;
import com.google.common.collect.HashBiMap;

import java.io.File;
import java.util.HashMap;

/**
 * Created by android on 1/7/16.
 */
public class DocumentsService extends BaseService {

        public DocumentsService(Activity act) {
            super(act);
        }

    public void login(String mEmail, String mPassword,boolean message, CallBack obj){
        String url = Constants.Login_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", mEmail);
        params.put("password", mPassword);
        this.post(url, params, obj, UserModel.getInstance(), message);
        Log.e("LoginService", url);
    }
    public void getDocuments(int projectID,boolean message, CallBack obj){
        String url = Constants.GetDocuments_API+"projectID="+projectID;
        this.get(url, obj, DocumentsListModel.getInstance(), message);
        Log.e("DocService", url);
    }
    public void uploadDocuments(File file,boolean message, CallBack obj){
        String url = "http://www.jitpac.com/FileUploadsManager/uploads/";
        HashMap<String,Object> params =  new HashMap<>();
        params.put("status[image]",file);
        this.post_with_file(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("uploadService", url);
    }
}
