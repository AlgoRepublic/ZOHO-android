package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.CreateCommentModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.GetUserModel;
import com.algorepublic.zoho.Models.TaskAssigneeModel;
import com.algorepublic.zoho.Models.TaskCommentsModel;
import com.algorepublic.zoho.Models.TasksListModel;
import com.algorepublic.zoho.Models.UserModel;
import com.algorepublic.zoho.adapters.TasksList;
import com.algorepublic.zoho.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by android on 12/23/15.
 */
public class TaskListService extends BaseService {

    public TaskListService(Activity act) {
        super(act);
    }

    public void getTasksList(boolean message, CallBack obj){
        String url = Constants.GetTaskList_API;
        this.get(url, obj, TasksListModel.getInstance(), message);
        Log.e("TaskListService", url);
    }
    public void getCommentsByTask(int taskID, boolean message, CallBack obj){
        String url = Constants.GetCommentByTask_API+"task_id="+taskID;
        this.get(url, obj, TasksListModel.getInstance(), message);
        Log.e("CommentByTaskService", url);
    }
    public void createComment(String comment, int taskID, int userID, boolean message, CallBack obj){
        String url = Constants.CreateComment_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("message", comment);
        params.put("taskID", String.valueOf(taskID));
        params.put("userID", String.valueOf(userID));
        this.post(url, params, obj, CreateCommentModel.getInstance(), message);
        Log.e("CreateCommentService", url);
    }
    public void updateTaskProgress(int taskID, int progress, boolean message, CallBack obj){
        String url = Constants.UpdateTaskProgress_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("taskID", String.valueOf(taskID));
        params.put("progress", String.valueOf(progress));
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("UpdateProgressService", url);
    }
    public void deleteTask(int taskID, boolean message, CallBack obj){
        String url = Constants.DeleteTask_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("taskID", String.valueOf(taskID));
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("DeleteTaskService", url);
    }
    public void completeTask(int taskID,int opt, boolean message, CallBack obj){
        String url = Constants.TaskCompleted_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("taskID", String.valueOf(taskID));
        params.put("opt", String.valueOf(opt));
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("CompleteTaskService", url);
    }
    public void getTaskAssignee(int projectID, boolean message, CallBack obj){
        String url = Constants.GetAssigneeByTask_API+"?ProjectID="+projectID;
        this.get(url, obj, TaskAssigneeModel.getInstance(), message);
        Log.e("TaskAssigneeService", url);
    }
}
