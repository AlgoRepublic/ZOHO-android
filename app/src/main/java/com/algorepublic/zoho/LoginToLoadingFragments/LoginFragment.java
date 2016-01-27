package com.algorepublic.zoho.LoginToLoadingFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.ActivityLoginToLoading;
import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
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

import java.lang.reflect.Field;

/**
 * Created by ahmad on 6/22/15.
 */
public class LoginFragment extends BaseFragment {

    static LoginFragment fragment;
    View view;AQuery aq;
    BaseClass baseClass;
    public LoginFragment() {
    }

    @SuppressWarnings("unused")
    public static LoginFragment newInstance() {
        if (fragment == null) {
            fragment = new LoginFragment();
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        if(!baseClass.getUserId().isEmpty()) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
        if(baseClass.getUserLanguage().equals(getString(R.string.lang_arabic))) {
            changeLanguage(getString(R.string.lang_arabic));
            view  = inflater.inflate(R.layout.fragment_login, container, false);
        }else {
            changeLanguage(getString(R.string.lang_english));
            view  = inflater.inflate(R.layout.fragment_login, container, false);
        }
        aq= new AQuery(getActivity(),view);
        aq.id(R.id.lang_text).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aq.id(R.id.lang_text).getText().toString().equalsIgnoreCase("English")) {
                    changeLanguage(getString(R.string.lang_english));
                    baseClass.setUserLanguage(getString(R.string.lang_english));
                } else {
                    changeLanguage(getString(R.string.lang_arabic));
                    baseClass.setUserLanguage(getString(R.string.lang_arabic));
                }
                startActivity(new Intent(getActivity(), ActivityLoginToLoading.class));
                getActivity().finish();
            }
        });
        aq.id(R.id.email_sign_in_button).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               LoginClick(v);
            }
        });
        aq.id(R.id.linkedin_here).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               LinkedInClick(v);
            }
        });

        return view;
    }

    public void LoginClick(final View view) {
        if(isContentNull(aq.id(R.id.email).getText().toString())) {
            aq.id(R.id.email).getEditText().requestFocus();
            aq.id(R.id.email).getEditText().setError(getString(R.string.enter_email));
            return;
        }
        if(!isEmailValid(aq.id(R.id.email).getText().toString())) {
            aq.id(R.id.email).getEditText().requestFocus();
            aq.id(R.id.email).getEditText().setError(getString(R.string.error_invalid_email));
            return;
        }
        if(isContentNull(aq.id(R.id.password).getText().toString())) {
            aq.id(R.id.password).getEditText().requestFocus();
            aq.id(R.id.password).getEditText().setError(getString(R.string.enter_password));
            return;
        }
        if(!baseClass.isNetworkAvailble(getActivity()))
            return;
        hideKeyPad(view);
        callFragmentWithReplace(R.id.logintoloading_container, LoginLoadingFragment.newInstance(aq), "LoginLoadingFragment");
    }

    public void LinkedInClick(final View view) {
        LISessionManager.getInstance(getActivity()).init(getActivity(), buildScope(), new AuthListener() {
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
        LISessionManager sessionManager = LISessionManager.getInstance(getActivity());
        LISession session = sessionManager.getSession();
        boolean accessTokenValid = session.isValid();
        session.getAccessToken();
        Log.e("accessTokenValid", session.getAccessToken().toString());
        if (accessTokenValid) {
            APIHelper.getInstance(getActivity()).getRequest(getActivity(), Constants.LinkedIn_API, new ApiListener() {
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
