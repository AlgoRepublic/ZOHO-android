package com.algorepublic.zoho.LoginToLoadingFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.Models.GetUserModel;
import com.algorepublic.zoho.Models.UserModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.LoginService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.lang.reflect.Field;

/**
 * Created by android on 1/25/16.
 */
public class LoginLoadingFragment extends BaseFragment {

    static LoginLoadingFragment fragment;
    View view; static AQuery aq;
    BaseClass baseClass;
    LoginService loginService;
    public LoginLoadingFragment() {
    }

    @SuppressWarnings("unused")
    public static LoginLoadingFragment newInstance(AQuery aQuery) {
        aq= aQuery;
        if (fragment == null) {
            fragment = new LoginLoadingFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_loading, container, false);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        loginService = new LoginService(getActivity());
        loginService.login(aq.id(R.id.email).getText().toString(), aq.id(R.id.password).getText().toString()
                , true, new CallBack(LoginLoadingFragment.this, "LoginCall"));
        return view;
    }
    public void LoginCall(Object caller, Object model) {
        UserModel.getInstance().setList((UserModel) model);
        if (UserModel.getInstance().responseCode.equalsIgnoreCase("100")
                && !UserModel.getInstance().userToken.equalsIgnoreCase("0")) {
            baseClass.setUserId(UserModel.getInstance().userToken);
            loginService.GetById(baseClass.getUserId(),true,new CallBack(LoginLoadingFragment.this,"GetById"));
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
            callFragmentWithReplace(R.id.logintoloading_container, LoginFragment.newInstance(), "LoginFragment");
        }
    }
    public void GetById(Object caller,Object model) {
        GetUserModel.getInstance().setList((GetUserModel) model);
        if (GetUserModel.getInstance().responseCode.equalsIgnoreCase("100")
                && GetUserModel.getInstance().user.toString() !="null") {
            baseClass.setFirstName(GetUserModel.getInstance().user.firstName);
            baseClass.setEmail(GetUserModel.getInstance().user.eMail);
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }else {
            Toast.makeText(getActivity(), getString(R.string.error_login_response), Toast.LENGTH_SHORT).show();
            callFragmentWithReplace(R.id.logintoloading_container, LoginFragment.newInstance(), "LoginFragment");
        }
    }
}