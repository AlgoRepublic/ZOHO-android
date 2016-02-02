package com.algorepublic.zoho.utils;

public class Constants {
	public static String BASE_URL = "http://api.jitpac.com";
	public static String Image_URL = "http://jitpac.com";
	public static String Login_API = BASE_URL + "/User/Login";
	public static String GetUser_API = BASE_URL+ "/User/GetById";
	public static String GetAssigneeByTask_API = BASE_URL+ "/User/GetListByProject";//ProjectID
	public static String GetTaskList_API = BASE_URL+ "/Task/GetList";
	public static String CreateTask_API = BASE_URL+ "/Task/CreateTask?"; //taskObject,assignee,files
	public static String GetCommentByTask_API = BASE_URL+ "/Task/GetCommentsByTaskID?";
	public static String CreateComment_API = BASE_URL+ "/Task/CreateComment";
	public static String DeleteTask_API = BASE_URL+ "/Task/DeleteTask"; //taskID
	public static String UpdateTaskProgress_API = BASE_URL+ "/Task/UpdateProgress"; //taskID,progress
	public static String TaskCompleted_API = BASE_URL+ "/Task/TaskCompleted"; //taskID,opt

	public static String GetProjectsByDepartment_API = BASE_URL+ "/Project/GetDepartmentProjects";
	public static String GetProjectsByClient_API = BASE_URL+ "/Project/GetCompanyProjects";
	public static String AddPeopleToTask_API = BASE_URL+ "/Task/AddPeopletoTask"; //PeopleInThisTask,projectID,taskID
	public static String TaskAttachments_API = BASE_URL+ "/Task/AttachmentList"; //taskID
	public static String GetForumsList_API = BASE_URL+ "/Forum/GetListByProjectID?";
	public static String GetDocuments_API = BASE_URL+ "/Document/GetListByProjectID?"; //projectID
	public static String UploadDocuments_API = BASE_URL+ "/Document/FileUpload?"; //fileObj,files,folderID
	public static String LinkedIn_API = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,pictureUrl)";
}
