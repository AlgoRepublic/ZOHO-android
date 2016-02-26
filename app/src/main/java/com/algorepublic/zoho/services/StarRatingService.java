package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.ProjectsByClientModel;
import com.algorepublic.zoho.Models.StarRatingModel;
import com.algorepublic.zoho.Models.StarRatingQuestionModel;
import com.algorepublic.zoho.utils.Constants;

import java.util.HashMap;

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
    public void StarUpdateProgress(int ID,int progress, boolean message, CallBack obj){
        String url = Constants.StarUpdateProgress_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("questionId", Integer.toString(ID));
        params.put("progress", String.valueOf(progress));
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("StarProgressService", url);
    }
    public void StarEditComment(int ID,String comment, boolean message, CallBack obj){
        String url = Constants.StarEditComment_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("questionId", Integer.toString(ID));
        params.put("comment", comment);
        this.post(url, params, obj, GeneralModel.getInstance(), message);
        Log.e("StarEditCommentService", url);
    }
}
