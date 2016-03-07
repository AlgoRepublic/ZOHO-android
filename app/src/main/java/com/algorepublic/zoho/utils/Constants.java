package com.algorepublic.zoho.utils;

public class Constants {
	// Server API
	public static String BASE_URL = "http://api.jitpac.com";
	public static String Image_URL = "http://jitpac.com/FileUploadsManager/uploads/";
	public static String UserImage_URL = "http://jitpac.com";
	// Login API
	public static String Login_API = BASE_URL + "/User/Login";
	public static String GetUser_API = BASE_URL+ "/User/GetById";
	// User API
	public static String GetUserRole_API = BASE_URL+ "/User/GetUserRolesList";
	public static String GetAllUserList_API = BASE_URL+ "/User/GetList";
	public static String GetUserListByProject_API = BASE_URL+ "/User/GetListByProject?";
	public static String GetAssigneeByTask_API = BASE_URL+ "/User/GetListByProject";//ProjectID
	public static String CreateUser_API = BASE_URL+ "/User/CreateUser?";
	public static String UpdateUser_API = BASE_URL+ "/User/UpdateUserProfile?";
	// Tasks API
	public static String GetTaskListByOwner_API = BASE_URL+ "/TaskList/GetListByOwnerID?"; //userID
	public static String GetTaskListBySubTasks_API = BASE_URL+ "/Task/GetByParentId?"; //taskID
	public static String GetAttachmentsBySubTasks_API = BASE_URL+ "/Task/AttachmentList?"; //taskID
	public static String UploadDocumentsByTasks_API = BASE_URL+ "/Task/AddAttachment?"; //taskID,files
	public static String GetTaskListByProject_API = BASE_URL+ "/TaskList/GetListByProjectID?"; //projectID
	public static String DeleteDocumentsByTasks_API = BASE_URL+ "/Task/DeleteAttachment?"; //taskID,filesToDelete
	public static String CreateTask_API = BASE_URL+ "/Task/CreateTask?"; //taskObject,assignee,files
	public static String UpdateTask_API = BASE_URL+ "/Task/UpdateTask?"; //taskObject,assignee,files
	public static String GetCommentByTask_API = BASE_URL+ "/Task/GetCommentsByTaskID?";
	public static String CreateComment_API = BASE_URL+ "/Task/CreateComment";
	public static String DeleteTask_API = BASE_URL+ "/Task/DeleteTask"; //taskID
	public static String UpdateTaskProgress_API = BASE_URL+ "/Task/UpdateProgress"; //taskID,progress
	public static String TaskCompleted_API = BASE_URL+ "/Task/TaskCompleted"; //taskID,opt
	public static String GetSubTakById_API = BASE_URL+ "/Task/GetById?";
	public static String TaskAttachments_API = BASE_URL+ "/Task/AttachmentList"; //taskID
	// StarRating API
	public static String StarRatingHeads_API = BASE_URL+ "/StarRating/GetListByLanguage"; //language
	public static String StarRatingQuestion_API = BASE_URL+ "/StarRating/GetQuestionCategoriesByCategory"; //catID,language
	public static String StarUpdateProgress_API = BASE_URL+ "/StarRating/UpdateProgressOfQuestion?"; // questionId, progress
	public static String StarEditComment_API = BASE_URL+ "/StarRating/EditCommentOfQuestion?"; // questionId, comment)
	// Project API
	public static String GetAllProjects_API = BASE_URL+ "/Project/GetList";
	public static String GetAllProjectsByUser_API = BASE_URL+ "/Project/GetListByUserID?";
	public static String GetProjectsByDepartment_API = BASE_URL+ "/Project/GetDepartmentProjects";
	public static String GetProjectsByClient_API = BASE_URL+ "/Project/GetCompanyProjects";
	public static String CreateProject_API = BASE_URL+ "/Project/CreateProject?";
	public static String UpdateProject_API = BASE_URL+ "/Project/UpdateProject?";
	public static String DeleteProject_API = BASE_URL+ "/Project/DeleteProject?";
	// Forum API
	public static String GetForumsList_API = BASE_URL+ "/Forum/GetListByProjectID?";
	public static String GetForumDetail_API = BASE_URL+ "/Forum/GetById?";
	public static String DeleteForum_API = BASE_URL+ "/Forum/Delete?"; //id
	public static String AddNewForumPost_API=BASE_URL+"/Forum/AddNewForumPost?";
	// Document API
	public static String GetDocuments_API = BASE_URL+ "/Document/GetListByProjectID?"; //projectID
	public static String DeleteDocuments_API = BASE_URL+ "/Document/DeleteFile?"; //fileID
	public static String UploadDocumentsByProject_API = BASE_URL+ "/Document/FileUpload?"; //fileObj,files,folderID
	public static String LinkedIn_API = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,pictureUrl)";
	// Department API
	public static String CreateDepartment_API = BASE_URL+ "/Department/CreateDepartment?"; //Name, CreateBy
	public static String UpdateDepartment_API = BASE_URL+ "/Department/UpdateDepartment?";
	public static String DeleteDepartment_API = BASE_URL+ "/Department/DeleteDepartment?";
	public static String AddProjectIntoDepartment = BASE_URL+ "/Department/AddProjectIntoDepartment?";


}
