package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.AddforumModel;
import com.algorepublic.zoho.Models.CreateForumModel;
import com.algorepublic.zoho.utils.Constants;

import java.util.HashMap;

/**
 * Created by waqas on 2/24/16.
 */
public class AddForumService extends BaseService {

    public AddForumService(Activity act) {
        super(act);
    }

    public void getCategoryList(String projectID, boolean message, CallBack obj){
        String url = Constants.GetForumsList_API+"projectID="+projectID;
        this.get(url, obj, AddforumModel.getInstance(), message);
        Log.e("AddforumModel", url);
    }

    public void createForum(String title,String content, String projectId,boolean makeSticky, boolean makeAnnouncement
          ,int categoryID  ,String createdBy, boolean message, CallBack obj){
        String url = Constants.AddNewForumPost_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Title", title);
        params.put("Content", content);
        params.put("ProjectID", projectId);
        params.put("MakeSticky", String.valueOf(makeSticky));
        params.put("MakeAnnouncement", String.valueOf(makeAnnouncement));
        params.put("CategoryID", String.valueOf(categoryID));
        params.put("CreateBy", String.valueOf(createdBy));
        this.post(url, params, obj, CreateForumModel.getInstance(), message);
        Log.e("CreateForumService", url);
    }
}
