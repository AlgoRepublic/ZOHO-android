package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 2/3/16.
 */
public class StarRatingQuestionModel {
    private static StarRatingQuestionModel _obj = null;

    private StarRatingQuestionModel() {

    }

    public static StarRatingQuestionModel getInstance() {
        if (_obj == null) {
            _obj = new StarRatingQuestionModel();
        }
        return _obj;
    }

    public void setList(StarRatingQuestionModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public ArrayList<Response> responseData;

    public class Response{
        @SerializedName("Statement")
        public String question;

        @SerializedName("Comment")
        public String comment;

        @SerializedName("Progress")
        public Integer progress;

        @SerializedName("ID")
        public Integer ID;
    }
    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("exceptionObject")
    public String exceptionObject;
}
