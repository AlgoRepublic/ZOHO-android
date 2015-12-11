package com.algorepublic.zoho.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.algorepublic.zoho.Models.GetUserModel;
import com.algorepublic.zoho.Models.UserModel;
import com.algorepublic.zoho.utils.Constants;

import java.util.HashMap;



/**
 * Created by android on 10/10/14.
 */
public class LoginService extends BaseService {

    public LoginService(Activity act) {
        super(act);
    }

    public void login(String mEmail, String mPassword,boolean message, CallBack obj){
        String url = Constants.Login_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", mEmail);
        params.put("password", mPassword);
        this.post(url, params, obj, UserModel.getInstance(), message);

        Log.e("LoginService", url);
    }
    public void GetById(String id,boolean message, CallBack obj){
        String url = Constants.GetUser_API;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
          this.post(url, params, obj, GetUserModel.getInstance(), message);

        Log.e("IDService", url);
    }
}
