package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.AddforumModel;
import com.algorepublic.zoho.Models.CreateCommentModel;
import com.algorepublic.zoho.Models.CreateForumModel;
import com.algorepublic.zoho.Models.ForumsCommentModel;
import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.UserModel;
import com.algorepublic.zoho.utils.Constants;

import java.util.HashMap;

/**
 * Created by waqas on 2/2/16.
 */
public class ForumService extends BaseService {
    public ForumService(Activity act) {
        super(act);
    }

    public void getForumsList(String projectID, boolean message, CallBack obj){
        String url = Constants.GetForumsList_API+ "projectID=" + projectID;
        this.get(url, obj, ForumsModel.getInstance(), message);
        Log.e("ForumsListService", url);
    }
    public void getForumsDetail(int ID, boolean message, CallBack obj){
        String url = Constants.GetForumDetail_API+ "id=" + ID;
        this.get(url, obj, ForumsCommentModel.getInstance(), message);
        Log.e("ForumDetailService", url);
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
        params.put("ForumContent", content);
        params.put("ProjectID", projectId);
        params.put("MakeSticky", String.valueOf(makeSticky));
        params.put("MakeAnnouncement", String.valueOf(makeAnnouncement));
        params.put("CategoryID", String.valueOf(categoryID));
        params.put("CreateBy", String.valueOf(createdBy));
        this.post(url, params, obj, CreateForumModel.getInstance(), message);
        Log.e("CreateForumService", url);
    }
    public void updateForum(String ID,String title,String content, String projectId,boolean makeSticky, boolean makeAnnouncement
            ,int categoryID  ,String createdBy, boolean message, CallBack obj){
        String url = Constants.AddNewForumPost_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Title", title);
        params.put("ID", ID);
        params.put("ForumContent", content);
        params.put("ProjectID", projectId);
        params.put("MakeSticky", String.valueOf(makeSticky));
        params.put("MakeAnnouncement", String.valueOf(makeAnnouncement));
        params.put("CategoryID", String.valueOf(categoryID));
        params.put("UpdateBy", String.valueOf(createdBy));
        this.post(url, params, obj, CreateForumModel.getInstance(), message);
        Log.e("CreateForumService", url);
    }
    public void deleteForum(String ID, boolean message, CallBack obj){
        String url = Constants.DeleteForum_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", ID);
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("DeleteForumService", url);
    }
    public void createforumComments(String forumID, String comment, String userID,boolean message, CallBack obj){
        String url = Constants.CreateForumComment_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ForumCommentID", forumID);
        params.put("Message", comment);
        params.put("CreatedBy", userID);
        this.post(url, params, obj, CreateCommentModel.getInstance(), message);
        Log.e("LoginService", url);
    }
}
