package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 3/8/16.
 */
public class FolderListModel {
    private static FolderListModel _obj = null;

    private FolderListModel() {

    }

    public static FolderListModel getInstance() {
        if (_obj == null) {
            _obj = new FolderListModel();
        }
        return _obj;
    }

    public void setList(FolderListModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public ArrayList<ResponseObject> responseObject = new ArrayList<>();

    @SerializedName("ResponseCode")
    public String responseCode;

    public class ResponseObject {

        @SerializedName("ID")
        public Integer Id;

        @SerializedName("FolderName")
        public String folderName;

    }

}
