package com.algorepublic.zoho.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android on 2/3/16.
 */
public class StarRatingModel {
    private static StarRatingModel _obj = null;

    private StarRatingModel() {

    }

    public static StarRatingModel getInstance() {
        if (_obj == null) {
            _obj = new StarRatingModel();
        }
        return _obj;
    }

    public void setList(StarRatingModel obj) {
        _obj = obj;
    }


    @SerializedName("responseObject")
    public ArrayList<Response> responseData;

    public class Response{
        @SerializedName("Title")
        public String title;

        @SerializedName("ID")
        public Integer ID;

        @SerializedName("RatingSubCategories")
        public ArrayList<RatingSubCategories> subCategories;
    }
    public class RatingSubCategories{

        @SerializedName("Title")
        public String subTitle;

        @SerializedName("ID")
        public Integer subID;

        @SerializedName("RatingQuestionCategories")
        public ArrayList<RatingQuestionCategories> questCategories;
    }
    public class RatingQuestionCategories{

        @SerializedName("Title")
        public String questTitle;

        @SerializedName("ID")
        public Integer questID;
    }
    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("exceptionObject")
    public String exceptionObject;

}
