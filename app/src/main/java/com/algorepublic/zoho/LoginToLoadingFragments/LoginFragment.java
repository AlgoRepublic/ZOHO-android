package com.algorepublic.zoho.LoginToLoadingFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import android.widget.EditText;
import android.widget.TextView;


import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.algorepublic.zoho.utils.LocaleHelper;
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

/**
 * Created by ahmad on 6/22/15.
 */
public class LoginFragment extends BaseFragment {

    static LoginFragment fragment;
    View view;AQuery aq;
    BaseClass baseClass;
    EditText editText;
    public static boolean flag= false;
    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        if(!baseClass.getUserId().isEmpty()) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
        view  = inflater.inflate(R.layout.fragment_login, container, false);

        aq= new AQuery(getActivity(),view);
        editText=(EditText)view.findViewById(R.id.password);
        if(!BaseClass.db.getString("Password").equalsIgnoreCase("")){
            aq.id(R.id.password).text(BaseClass.db.getString("Password"));
        }
        if (LocaleHelper.getLanguage(getContext()).equalsIgnoreCase("ar")) {
            aq.id(R.id.lang_text).text(R.string.arabic);

            //changeLanguage(getString(R.string.lang_arabic));
        }else{
           // changeLanguage(getString(R.string.lang_english));
                aq.id(R.id.lang_text).text(R.string.english);
        }
        aq.id(R.id.lang_text).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (baseClass.getUserLanguage().equalsIgnoreCase("en")) {

                    LocaleHelper.setLocale(baseClass.getApplicationContext(), "ar");
                  //  changeLanguage(getString(R.string.lang_arabic));
                    baseClass.setUserLanguage(getString(R.string.lang_arabic));
                    aq.id(R.id.lang_text).text(R.string.arabic);
                } else {
                    LocaleHelper.setLocale(baseClass.getApplicationContext(), "en");
                   // changeLanguage(getString(R.string.lang_english));
                    baseClass.setUserLanguage(getString(R.string.lang_english));
                    aq.id(R.id.lang_text).text(R.string.english);
                }
                getActivity().recreate();
//                startActivity(new Intent(getActivity(), ActivityLoginToLoading.class));
//                getActivity().finish();
            }
        });
        aq.id(R.id.email_sign_in_button).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginClick(v);
            }
        });
           aq.id(R.id.checkbox).getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   baseClass.hideKeyPad(view);
                   if (isChecked == true) {
                       BaseClass.db.putString("Password", aq.id(R.id.password).getText().toString());
                   } else
                       BaseClass.db.putString("Password", "");
               }
           });
        aq.id(R.id.linkedin_here).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkedInClick(v);
            }
        });

        aq.id(R.id.password).getTextView().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == getResources().getInteger(R.integer.add_comment)) {
                    flag = true;
                    if (flag == true) {
                        LoginClick(v);
                    }
                    return true;
                }
                return false;
            }
        });

        aq.id(R.id.main).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           baseClass.hideKeyPad(v);
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
        baseClass.hideKeyPad(view);
        callFragmentWithReplace(R.id.logintoloading_container, LoginLoadingFragment.newInstance(aq), "LoginLoadingFragment");
    }

    public void LinkedInClick(final View view) {
        LISessionManager.getInstance(getActivity()).init(getActivity(), buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                Snackbar.make(getView(), getString(R.string.login), Snackbar.LENGTH_LONG).show();
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

