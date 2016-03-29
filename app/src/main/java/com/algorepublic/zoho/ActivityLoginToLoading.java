package com.algorepublic.zoho;


import android.os.Bundle;

import com.algorepublic.zoho.LoginToLoadingFragments.LoginFragment;
import com.algorepublic.zoho.utils.BaseClass;

/**
 * Created by android on 12/10/15.
 */
public class ActivityLoginToLoading extends BaseActivity{

    BaseClass baseClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseClass = ((BaseClass) getApplicationContext());
        if (baseClass.getThemePreference() == R.style.AppThemeBlue)
            setTheme(R.style.AppThemeBlue);
        else
            setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_login_to_loading);
        callFragment(R.id.logintoloading_container, LoginFragment.newInstance(),"LoginFragment");
    }
}
