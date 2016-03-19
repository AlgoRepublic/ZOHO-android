package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by android on 1/7/16.
 */
public class DocumentsListModel {
    private static DocumentsListModel _obj = null;

    private DocumentsListModel() {

    }

    public static DocumentsListModel getInstance() {
        if (_obj == null) {
            _obj = new DocumentsListModel();
        }
        return _obj;
    }

    public void setList(DocumentsListModel obj) {
        _obj = obj;
    }


    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("exceptionObject")
    public Object exceptionObject;

    @SerializedName("responseObject")
    @Expose
    public ArrayList<ResponseObject> responseObject = new ArrayList<ResponseObject>();


    public class ResponseObject{
        @SerializedName("ID")
        @Expose
        public int ID;

        @SerializedName("FolderName")
        @Expose
        public String folderName;

        @SerializedName("Files")
        @Expose
        public ArrayList<Files> files = new ArrayList<>();
    }
    public class Files {

        @SerializedName("ID")
        @Expose
        public int ID;

        @SerializedName("FileName")
        @Expose
        public String fileName;

        @SerializedName("FileDescription")
        @Expose
        public String fileDescription;

        @SerializedName("FileSizeInByte")
        @Expose
        public Integer fileSizeInByte;

        @SerializedName("UpdatedAt")
        @Expose
        public String updatedAt;

        @SerializedName("FileTypeID")
        @Expose
        public int fileTypeID;


        @SerializedName("IsFav")
        @Expose
        public boolean isFav;

        @SerializedName("responseObject")
        public User user = new User();

    }

    public class User {

        @SerializedName("ID")
        public String Id;

        @SerializedName("Email")
        public String eMail;

        @SerializedName("FirstName")
        public String firstName;

        @SerializedName("LastName")
        @Expose
        public String lastName;

        @SerializedName("ProfileImagePath")
        @Expose
        public String profileImagePath;

        @SerializedName("ProfilePictureID")
        @Expose
        public Integer profilePictureID;
    }

}
