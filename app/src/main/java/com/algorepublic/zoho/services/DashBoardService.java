package com.algorepublic.zoho.services;

import android.app.Activity;
import android.util.Log;

import com.algorepublic.zoho.Models.DashBoardModel;
import com.algorepublic.zoho.utils.Constants;

/**
 * Created by waqas on 3/12/16.
 */
public class DashBoardService extends BaseService {

    public DashBoardService(Activity act) {
        super(act);
    }

    public void getMileStone (String projectID, boolean message, CallBack obj){
        String url = Constants.MilesStone_API+"projectID="+projectID;
        this.get(url, obj, DashBoardModel.getInstance(), message);
        Log.e("DashBoardService", url);
    }
}
