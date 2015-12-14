package com.algorepublic.zoho;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.algorepublic.zoho.Models.GetUserModel;
import com.algorepublic.zoho.Models.UserModel;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.LoginService;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by android on 12/10/15.
 */
public class ActivityLogin extends BaseActivity{

    LoginService loginService;
    BaseClass baseClass;
    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        aq= new AQuery(this);
        baseClass = ((BaseClass) getApplicationContext());
        if(baseClass.getUserId() !="") {
            startActivity(new Intent(this, MainActivity.class));
            ActivityLogin.this.finish();
        }
        loginService = new LoginService(this);
        Glide.with(this)
                .load(R.drawable.loader)
                .crossFade()
                .into(aq.id(R.id.loader).getImageView());
        aq.id(R.id.lang_text).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aq.id(R.id.lang_text).getText().toString().equalsIgnoreCase("Arabic"))
                    aq.id(R.id.lang_text).text("English");
                else
                    aq.id(R.id.lang_text).text("Arabic");
            }
        });
    }
    public void LoginClick(final View view) {
        if(isContentNull(aq.id(R.id.email).getText().toString())) {
            aq.id(R.id.email).getEditText().requestFocus();
            aq.id(R.id.email).getEditText().setError("Please enter email");
            return;
        }
        if(!isEmailValid(aq.id(R.id.email).getText().toString())) {
            aq.id(R.id.email).getEditText().requestFocus();
            aq.id(R.id.email).getEditText().setError("Invalid email");
            return;
        }
        if(isContentNull(aq.id(R.id.password).getText().toString())) {
            aq.id(R.id.password).getEditText().requestFocus();
            aq.id(R.id.password).getEditText().setError("Please enter password");
            return;
        }
        if(!baseClass.isNetworkAvailble(ActivityLogin.this))
            return;
        baseClass.HideKeyPad(view);
        aq.id(R.id.loader).visibility(View.VISIBLE);
        loginService.login(aq.id(R.id.email).getText().toString(), aq.id(R.id.password).getText().toString(), true, new CallBack(ActivityLogin.this, "LoginCall"));
    }
    public void LoginCall(Object caller, Object model) {
        UserModel.getInstance().setList((UserModel) model);
        if (UserModel.getInstance().responseCode.equalsIgnoreCase("100")
                && !UserModel.getInstance().userToken.equalsIgnoreCase("0")) {
            baseClass.setUserId(UserModel.getInstance().userToken);
            loginService.GetById(baseClass.getUserId(),true,new CallBack(ActivityLogin.this,"GetById"));
        }
        else
        {
            Toast.makeText(ActivityLogin.this, "Invalid User Credential!", Toast.LENGTH_SHORT).show();
            aq.id(R.id.loader).visibility(View.GONE);
        }
    }
    public void GetById(Object caller,Object model) {
        GetUserModel.getInstance().setList((GetUserModel) model);
        if (GetUserModel.getInstance().responseCode.equalsIgnoreCase("0")
                && GetUserModel.getInstance().user.toString() !="null") {
            baseClass.setFirstName(GetUserModel.getInstance().user.firstName);
            baseClass.setEmail(GetUserModel.getInstance().user.eMail);
            startActivity(new Intent(this, MainActivity.class));
            ActivityLogin.this.finish();
        }else {
            Toast.makeText(ActivityLogin.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            aq.id(R.id.loader).visibility(View.GONE);
        }
    }


    public void LinkedInClick(final View view) {
        LISessionManager.getInstance(getApplicationContext()).init(ActivityLogin.this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                Snackbar.make(view, "Login !", Snackbar.LENGTH_LONG).show();
                setUpdateState();
                // startActivity(new Intent(ActivityLogin.this, MainActivity.class));
            }

            @Override
            public void onAuthError(LIAuthError error) {
                Log.e("TAG", "onAuthError");
                Log.e("Error", error.toString());
            }
        }, true);
    }
    private static Scope buildScope() {
        return Scope.build(Scope.R_FULLPROFILE, Scope.W_SHARE);
    }

    private void setUpdateState() {
        LISessionManager sessionManager = LISessionManager.getInstance(getApplicationContext());
        LISession session = sessionManager.getSession();
        boolean accessTokenValid = session.isValid();
        session.getAccessToken();
        Log.e("accessTokenValid", session.getAccessToken().toString());
        if (accessTokenValid) {
            APIHelper.getInstance(this).getRequest(this, Constants.LinkedIn_API, new ApiListener() {
                @Override
                public void onApiSuccess(ApiResponse apiResponse) {
                    Log.e("accessTokenValid1", "/" + apiResponse.getResponseDataAsJson());
                    try {
                        Log.e("name:", "" + apiResponse.getResponseDataAsJson().get("firstName").toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        JSONObject s = apiResponse.getResponseDataAsJson();
                        String headline = s.has("headline") ? s.getString("headline") : "";
                        String pictureUrl = s.has("pictureUrl") ? s.getString("pictureUrl") : null;
                        JSONObject location = s.getJSONObject("location");
                        String locationName = location != null && location.has("name") ? location.getString("name") : "";
                        Log.e("accessTokenValid1111111", locationName + "/" + headline + "/ " + pictureUrl);
                        if (pictureUrl != null) {
                            //new FetchImageTask(attendeeImageView).execute(pictureUrl);
                        } else {
                            //attendeeImageView.setImageResource(R.drawable.ghost_person);
                        }
                    } catch (JSONException e) {
                    }
                }
                @Override
                public void onApiError(LIApiError apiError) {
                    Log.e("accessTokenValid",  "/"+apiError.toString() );
                }
            });
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isContentNull(String content) {
        //TODO: Replace this with your own logic
        return content.length()==0;
    }
}
