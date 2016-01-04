package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 1/4/16.
 */
public class GeneralModel {
    private static GeneralModel _obj = null;

    private GeneralModel() {

    }

    public static GeneralModel getInstance() {
        if (_obj == null) {
            _obj = new GeneralModel();
        }
        return _obj;
    }

    public void setList(GeneralModel obj) {
        _obj = obj;
    }


    @SerializedName("ResponseCode")
    public String responseCode;

    @SerializedName("responseObject")
    public boolean responseObject;


}
