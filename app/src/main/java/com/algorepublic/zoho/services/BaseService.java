package com.algorepublic.zoho.services;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.NetworkUtil;
import com.algorepublic.zoho.utils._;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by hasanali on 1/10/14.
 */

public class BaseService{

    AQuery aq;
    Activity context;
    ProgressBar progressBar;
    // private AlertDialog mAlertDialogBuilder;

    public BaseService(Activity act){
        aq = new AQuery(act);
        context = act;
    }

    /**
     *
     * @param url
     * @param callBack
     * @param model
     * @param showProgress
     */
    public void get(String url, final CallBack callBack, final Object model, final boolean showProgress){

        if(!NetworkUtil.isInternetConnected(context)){
            NetworkUtil.internetFailedDialog(context);
            return;
        }
        if (aq.id(R.id.progress_bar).isExist() && showProgress) {
            progressBar = (ProgressBar) aq.id(R.id.progress_bar).getView();
            progressBar.setVisibility(View.VISIBLE);
        }
        aq.ajax(url, JSONObject.class,
                new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json,
                                         AjaxStatus status) {
                        Object obj = model;
                        if (json != null) {
                            _.log("JSON::"+ json.toString());
                            if(validateToken(json)) {
                                return;
                            }
                            Gson gson = new Gson();
                            obj = gson.fromJson(json.toString(), obj.getClass());

                            if (progressBar != null && showProgress)  {
                                progressBar.setVisibility(View.GONE);
                            }
                            callBack.invoke(obj);
                        } else {
                            if (progressBar != null && showProgress)  {
                                progressBar.setVisibility(View.GONE);
                            }
                            showServerError(status);
                            return;
                        }

                    }
                });
    }

    private boolean validateToken(JSONObject json) {
        try{
            if(json.getJSONObject("response").getInt("code") == -2){
//                if (mAlertDialogBuilder != null && !mAlertDialogBuilder.isShowing()) {
//                    mAlertDialogBuilder =
//                    mAlertDialogBuilder.show();
//
//                }
                NetworkUtil.showStatusDialog(context, context.getResources().getString(R.string.app_name), "" + json.getJSONObject("response").getString("msg"), true);
                if(progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }
                return true;
            }
        } catch (JSONException je){

        }
        return  false;
    }

    /**
     *
     * @param url
     * @param params
     * @param callBack
     * @param model
     * @param showProgress
     */
    public void post(String url,HashMap<String, String> params,final CallBack callBack, final Object model, final boolean showProgress){
        if(!NetworkUtil.isInternetConnected(context)){
            NetworkUtil.internetFailedDialog(context);
            return;
        }
        if(aq.id(R.id.progress_bar).isExist() && showProgress){
            progressBar = (ProgressBar) aq.id(R.id.progress_bar).getView();
            progressBar.setVisibility(View.VISIBLE);
        }

        aq.ajax(url, params, JSONObject.class,
                new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json,
                                         AjaxStatus status) {

                        Object obj = model;
                        if (json != null) {
                            try {
                                _.log("JSON:: "+json.toString());
                                if(validateToken(json))
                                {
                                    return;
                                }
                                Gson gson = new Gson();
                                obj = gson.fromJson(json.toString(),
                                        obj.getClass());
                                //Log.e("JSON::", json.toString());
                                if (progressBar != null && showProgress) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                callBack.invoke(obj);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            if (progressBar != null && showProgress) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showServerError(status);
                            return;
                        }

                    }
                });
    }

    /**
     *
     * @param url
     * @param params
     * @param callBack
     * @param model
     * @param showProgress
     */
    public void post_with_file(String url,HashMap<String, Object> params,final CallBack callBack, final Object model, final boolean showProgress){
        if(!NetworkUtil.isInternetConnected(context)){
            NetworkUtil.internetFailedDialog(context);
            return;
        }
        if (aq.id(R.id.progress_bar).isExist() && showProgress) {
            progressBar = (ProgressBar) aq.id(R.id.progress_bar).getView();
            progressBar.setVisibility(View.VISIBLE);
        }
        aq.ajax(url, params, JSONObject.class,
                new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json,
                                         AjaxStatus status) {

                        Object obj = model;
                        if (json != null) {
                            try {
                                Gson gson = new Gson();
                                obj = gson.fromJson(json.toString(),
                                        obj.getClass());
                                _.log("JSON::"+ json.toString());
                                if (progressBar != null && showProgress)
                                    progressBar.setVisibility(View.GONE);
                                callBack.invoke(obj);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            if (progressBar != null && showProgress)  {
                                progressBar.setVisibility(View.GONE);
                            }
                            showServerError(status);
                            return;
                        }
                    }
                });
    }


    private void showServerError(AjaxStatus status) {
        _.log("status.getError(): "+status.getError());
        _.log("status.getCode(): "+status.getCode());
    }

}
