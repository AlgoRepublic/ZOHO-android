package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 1/19/16.
 */
public class TaskAttachmentsModel {
    private static TaskAttachmentsModel _obj = null;

    private TaskAttachmentsModel() {

    }

    public static TaskAttachmentsModel getInstance() {
        if (_obj == null) {
            _obj = new TaskAttachmentsModel();
        }
        return _obj;
    }

    public void setList(TaskAttachmentsModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public ArrayList<ResponseObject> responseObject = new ArrayList<>();

    @SerializedName("ResponseCode")
    public int responseCode;

    public class ResponseObject {

        @SerializedName("ID")
        public String Id;

        @SerializedName("FileName")
        public String fileName;

        @SerializedName("FileTypeID")
        public int fileTypeID;

        @SerializedName("FileDescription")
        public String fileDescription;

        @SerializedName("FileSizeInByte")
        public String fileSizeInByte;

    }

}