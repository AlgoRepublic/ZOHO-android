package com.algorepublic.zoho;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

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

/**
 * Created by android on 12/10/15.
 */
public class ActivityLogin extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpdateState();
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
            String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,pictureUrl)";
            APIHelper.getInstance(this).getRequest(this, url, new ApiListener() {

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


    public void LoginClick(final View view) {

        LISessionManager.getInstance(getApplicationContext()).init(ActivityLogin.this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                Snackbar.make(view,"Login !",Snackbar.LENGTH_LONG).show();
                startActivity(new Intent(ActivityLogin.this, MainActivity.class));
            }


            //
            @Override
            public void onAuthError(LIAuthError error) {
                Log.e("TAG","onAuthError");
                Log.e("Error", error.toString());
                // Handle authentication errors
            }
        }, true);
    }
}
