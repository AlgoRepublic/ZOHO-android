package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.utils.Constants;

/**
 * Created by waqas on 2/2/16.
 */
public class ForumListService extends BaseService {
    public ForumListService(Activity act) {
        super(act);
    }

    public void getForumsList(int projectID, boolean message, CallBack obj){
        String url = Constants.GetForumsList_API+ "projectID=" + projectID;
        this.get(url, obj, ForumsModel.getInstance(), message);
        Log.e("ForumsListService", url);
    }
}
