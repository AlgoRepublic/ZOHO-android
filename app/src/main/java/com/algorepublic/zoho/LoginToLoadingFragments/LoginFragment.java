package com.algorepublic.zoho.LoginToLoadingFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.LocaleHelper;
import com.androidquery.AQuery;

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
            aq.id(R.id.lang_text).text(R.string.english);
        }else{
            aq.id(R.id.lang_text).text(R.string.arabic);
        }
        aq.id(R.id.lang_text).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (baseClass.getUserLanguage().equalsIgnoreCase("en")) {
                    LocaleHelper.setLocale(baseClass.getApplicationContext(), "ar");
                    baseClass.setUserLanguage(getString(R.string.lang_arabic));
                } else {
                    LocaleHelper.setLocale(baseClass.getApplicationContext(), "en");
                    baseClass.setUserLanguage(getString(R.string.lang_english));
                }
                BaseActivity.imeManager.showInputMethodPicker();
                getActivity().recreate();
            }
        });
        aq.id(R.id.email_sign_in_button).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseClass.db.putString("SavePassword", aq.id(R.id.password).getText().toString());
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

        aq.id(R.id.password).getTextView().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                LoginClick(v);
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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isContentNull(String content) {
        //TODO: Replace this with your own logic
        return content.length()==0;
    }
}

