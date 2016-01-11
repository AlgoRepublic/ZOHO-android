package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 1/11/16.
 */
public class CreateCommentModel {
    private static CreateCommentModel _obj = null;

    private CreateCommentModel() {

    }

    public static CreateCommentModel getInstance() {
        if (_obj == null) {
            _obj = new CreateCommentModel();
        }
        return _obj;
    }

    public void setList(CreateCommentModel obj) {
        _obj = obj;
    }


    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("responseObject")
    public Integer responseObject;


}

