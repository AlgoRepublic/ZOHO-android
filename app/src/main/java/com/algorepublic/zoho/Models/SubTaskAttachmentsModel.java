package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 1/19/16.
 */
public class SubTaskAttachmentsModel {
    private static SubTaskAttachmentsModel _obj = null;

    private SubTaskAttachmentsModel() {

    }

    public static SubTaskAttachmentsModel getInstance() {
        if (_obj == null) {
            _obj = new SubTaskAttachmentsModel();
        }
        return _obj;
    }

    public void setList(SubTaskAttachmentsModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public ArrayList<ResponseObject> responseObject = new ArrayList<>();

    @SerializedName("ResponseCode")
    public int responseCode;

    public class ResponseObject {

        @SerializedName("ID")
        public Integer Id;

        @SerializedName("FileName")
        public String fileName;

        @SerializedName("FileTypeID")
        public int fileTypeID;

        @SerializedName("FileDescription")
        public String fileDescription;

        @SerializedName("FileSizeInByte")
        public Integer fileSizeInByte;

        @SerializedName("UpdatedAt")
        public String updatedAt;

        @SerializedName("IsFav")
        public boolean isFav;

        @SerializedName("IsDeleted")
        public boolean isDeleted;

    }

}