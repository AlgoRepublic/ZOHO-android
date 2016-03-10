package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.CreateCommentModel;
import com.algorepublic.zoho.Models.DocumentsListModel;
import com.algorepublic.zoho.Models.FolderListModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.SubTaskAttachmentsModel;
import com.algorepublic.zoho.Models.TaskCommentsModel;
import com.algorepublic.zoho.Models.TasksDocumentModel;
import com.algorepublic.zoho.Models.UserModel;
import com.algorepublic.zoho.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by android on 1/7/16.
 */
public class DocumentsService extends BaseService {

        public DocumentsService(Activity act) {
            super(act);
        }

    public void getDocuments(int projectID,boolean message, CallBack obj){
        String url = Constants.GetDocuments_API+"projectID="+projectID;
        this.get(url, obj, DocumentsListModel.getInstance(), message);
        Log.e("DocService", url);
    }
    public void getFolderList(String projectID,boolean message, CallBack obj){
        String url = Constants.GetFolderList_API+"projectID="+projectID;
        this.get(url, obj, FolderListModel.getInstance(), message);
        Log.e("DocService", url);
    }
    public void getDocsBySubTasks(int taskID, boolean message, CallBack obj){
        String url = Constants.TaskAttachments_API+"taskID="+taskID;
        this.get(url, obj, SubTaskAttachmentsModel.getInstance(), message);
        Log.e("AttachBySubTaskService", url);
    }

    public void deleteDocument(ArrayList<Integer> fileIDs, boolean message, CallBack obj){
        String url = Constants.DeleteDocuments_API;
        HashMap<String, String> params = new HashMap<String, String>();
        for(int loop=0;loop<fileIDs.size();loop++) {
            params.put("filesID[" + loop + "]", String.valueOf(fileIDs.get(loop)));
        }
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("DeleteDocumentsService", url);
    }
    public void deleteDocumentByTask(int taskID, ArrayList<Integer> fileIDs, boolean message, CallBack obj){
        String url = Constants.DeleteDocumentsByTasks_API;
        HashMap<String, String> params = new HashMap<String, String>();
        for(int loop=0;loop<fileIDs.size();loop++) {
            params.put("filesToDelete["+loop+"]", String.valueOf(fileIDs.get(loop)));
        }
        params.put("taskID",Integer.toString(taskID));
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("DeleteDocumentsService", url);
    }
    public void getDocumentComments(int fileID, boolean message, CallBack obj){
        String url = Constants.GetDocumentComments_API+"fileID="+fileID;
        this.get(url, obj, TaskCommentsModel.getInstance(), message);
        Log.e("AttachBySubTaskService", url);
    }
    public void createDocComments(String fileID, String comment, String userID,boolean message, CallBack obj){
        String url = Constants.CreateDocComments_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("fileID", fileID);
        params.put("comment", comment);
        params.put("userID", userID);
        this.post(url, params, obj, CreateCommentModel.getInstance(), message);
        Log.e("LoginService", url);
    }
}
