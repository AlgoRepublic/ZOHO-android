package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.ProjectsByClientModel;
import com.algorepublic.zoho.Models.StarRatingModel;
import com.algorepublic.zoho.Models.StarRatingQuestionModel;
import com.algorepublic.zoho.utils.Constants;

/**
 * Created by android on 2/3/16.
 */
public class StarRatingService extends BaseService {

    public StarRatingService(Activity act) {
        super(act);
    }

    public void getStarRatingHeads_API(String langType, boolean message, CallBack obj){
        String url = Constants.StarRatingHeads_API + "?language=" + langType;
        this.get(url, obj, StarRatingModel.getInstance(), message);
        Log.e("StarRatingService", url);
    }
    public void getStarRatingQuestion_API(int ID ,String langType, boolean message, CallBack obj){
        String url = Constants.StarRatingQuestion_API + "?subcategoryID=" +ID+"&language=" + langType;
        this.get(url, obj, StarRatingQuestionModel.getInstance(), message);
        Log.e("StarRatingQuestService", url);
    }
}
