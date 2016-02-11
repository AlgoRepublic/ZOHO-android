package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 1/7/16.
 */
public class TasksDocumentModel {
    private static TasksDocumentModel _obj = null;

    private TasksDocumentModel() {

    }

    public static TasksDocumentModel getInstance() {
        if (_obj == null) {
            _obj = new TasksDocumentModel();
        }
        return _obj;
    }

    public void setList(TasksDocumentModel obj) {
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

        @SerializedName("FileName")
        @Expose
        public String fileName;

        @SerializedName("FileDescription")
        @Expose
        public String fileDescription;

        @SerializedName("FileSizeInByte")
        @Expose
        public String fileSizeInByte;

        @SerializedName("CreatedAt")
        @Expose
        public String createdAt;

        @SerializedName("FileTypeID")
        @Expose
        public int fileTypeID;


        @SerializedName("IsFav")
        @Expose
        public boolean isFav;

    }

}
