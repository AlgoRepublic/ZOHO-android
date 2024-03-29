package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.CreateCommentModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.TaskByIdModel;
import com.algorepublic.zoho.Models.TaskUserModel;
import com.algorepublic.zoho.Models.SubTaskAttachmentsModel;
import com.algorepublic.zoho.Models.TaskCommentsModel;
import com.algorepublic.zoho.Models.TaskListBySubTaskModel;
import com.algorepublic.zoho.Models.TasksListByOwnerModel;
import com.algorepublic.zoho.utils.Constants;

import java.util.HashMap;

/**
 * Created by android on 12/23/15.
 */
public class TaskListService extends BaseService {

    public TaskListService(Activity act) {
        super(act);
    }

    public void getTasksListByOwner(String userID, boolean message, CallBack obj){
        String url = Constants.GetTaskListByOwner_API+"userID="+userID;
        this.get(url, obj, TasksListByOwnerModel.getInstance(), message);
        Log.e("TaskListByOwnerService", url);
    }
    public void getTasksById(int ID, boolean message, CallBack obj){
        String url = Constants.TaskById_API+"id="+ID;
        this.get(url, obj, TaskByIdModel.getInstance(), message);
        Log.e("TaskListByOwnerService", url);
    }
    public void getTasksListByProject(String projectID, boolean message, CallBack obj){
        String url = Constants.GetTaskListByProject_API+"projectID="+projectID;
        this.get(url, obj, TasksListByOwnerModel.getInstance(), message);
        Log.e("TaskListByProjService", url);
    }
    public void getTasksListBySubTasks(int taskID, boolean message, CallBack obj){
        String url = Constants.GetTaskListBySubTasks_API+"taskID="+taskID;
        this.get(url, obj, TaskListBySubTaskModel.getInstance(), message);
        Log.e("TaskBySubTaskService", url);
    }

    public void getCommentsByTask(int taskID, boolean message, CallBack obj){
        String url = Constants.GetCommentByTask_API+"taskID="+taskID;
        this.get(url, obj, TaskCommentsModel.getInstance(), message);
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
        Log.e("A", "A" + taskID);
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
        this.get(url, obj, TaskUserModel.getInstance(), message);
        Log.e("TaskAssigneeService", url);
    }

    public void getOwnersByPermission(String permissionId, boolean message, CallBack obj){
        String url = Constants.GetUserListByPermission_API+"permissionID="+permissionId;
        this.get(url, obj, TaskUserModel.getInstance(), message);
        Log.e("GetOwnersService", url);
    }
    public void getTaskAttachments(int taskID, boolean message, CallBack obj){
        String url = Constants.TaskAttachments_API+"?taskID="+taskID;
        this.get(url, obj, SubTaskAttachmentsModel.getInstance(), message);
        Log.e("TaskAttachmentService", url);
    }

}
