package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.ForumsCommentModel;
import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.utils.Constants;

/**
 * Created by waqas on 2/2/16.
 */
public class ForumService extends BaseService {
    public ForumService(Activity act) {
        super(act);
    }

    public void getForumsList(int projectID, boolean message, CallBack obj){
        String url = Constants.GetForumsList_API+ "projectID=" + projectID;
        this.get(url, obj, ForumsModel.getInstance(), message);
        Log.e("ForumsListService", url);
    }
    public void getForumsDetail(int ID, boolean message, CallBack obj){
        String url = Constants.GetForumDetail_API+ "id=" + ID;
        this.get(url, obj, ForumsCommentModel.getInstance(), message);
        Log.e("ForumDetailService", url);
    }
}
