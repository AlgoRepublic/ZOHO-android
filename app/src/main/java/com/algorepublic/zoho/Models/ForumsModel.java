package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by waqas on 2/2/16.
 */
public class ForumsModel {
    private static ForumsModel _obj = null;

    private ForumsModel() {

    }

    public static ForumsModel getInstance() {
        if (_obj == null) {
            _obj = new ForumsModel();
        }
        return _obj;
    }

    public void setList(ForumsModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    @Expose
    public ArrayList<ResponseObject> responseObject = new ArrayList<ResponseObject>();

    public class ResponseObject{
        @SerializedName("ForumID")
        public String forumID;

        @SerializedName("Title")
        public String title;

        @SerializedName("ForumContent")
        public String forumContent;

        @SerializedName("ProjectID")
        public String projectID;

        @SerializedName("MakeSticky")
        public Boolean MakeSticky;

        @SerializedName("ForumAttachments")
        public String forumAttachments;

        @SerializedName("ForumComments")
        public String forumComments;

        @SerializedName("User")
        public Users user = new Users();

        @SerializedName("CategoryName")
        public String categoryName;

        @SerializedName("UpdatedAt")
        public String updatedAt;

        @SerializedName("CreateBy")
        public String createBy;

        @SerializedName("UpdateBy")
        public String updateBy;


        @SerializedName("IsDeleted")
        public String isDeleted;

        @SerializedName("ID")
        public String iD;

    }
    public class Users {

        @SerializedName("FirstName")
        @Expose
        public String firstName;

        @SerializedName("Password")
        @Expose
        public String password;

        @SerializedName("Email")
        @Expose
        public String email;


        @SerializedName("UserTypeID")
        @Expose
        public String userTypeID;

        @SerializedName("LastName")
        @Expose
        public String lastName;

        @SerializedName("NickName")
        @Expose
        public String nickName;

        @SerializedName("OfficePhone")
        @Expose
        public Integer officePhone;


        @SerializedName("HomePhone")
        @Expose
        public Integer HomePhone;


        @SerializedName("Mobile")
        @Expose
        public String mobile;

        @SerializedName("LanguageID")
        @Expose
        public String languageID;

        @SerializedName("ProfilePictureID")
        @Expose
        public String profilePictureID;

        @SerializedName("ThemeID")
        @Expose
        public String themeID;

        @SerializedName("CompanyID")
        @Expose
        public String companyID;


        @SerializedName("LoadingpageOption")
        @Expose
        public String loadingpageOption;


        @SerializedName("Prointernal")
        @Expose
        public String prointernal;


        @SerializedName("EmailActivityOption")
        @Expose
        public String emailActivityOption;

        @SerializedName("StatusID")
        @Expose
        public String statusID;

        @SerializedName("isClientUser")
        @Expose
        public String isClientUser;

        @SerializedName("RoleID")
        @Expose
        public String roleID;



        @SerializedName("UserRole")
        @Expose
        public String userRole;


        @SerializedName("ProjectIDs")
        @Expose
        public String projectIDs;



        @SerializedName("ProfileImagePath")
        @Expose
        public String profileImagePath;


        @SerializedName("UserNorifications")
        @Expose
        public String userNorifications;


        @SerializedName("UserTheme")
        @Expose
        public String userTheme;


        @SerializedName("CreatedAt")
        @Expose
        public String createdAt;


        @SerializedName("UpdatedAt")
        @Expose
        public String updatedAt;

        @SerializedName("UpdateBy")
        @Expose
        public String updateBy;

        @SerializedName("IsDeleted")
        @Expose
        public String isDeleted;

        @SerializedName("ID")
        @Expose
        public String iD;



    }
    @SerializedName("ResponseCode")
    public String responseCode;

    @SerializedName("exceptionObject")
    public String exceptionObject;

}
