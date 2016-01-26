package com.algorepublic.zoho;


import android.os.Bundle;

import com.algorepublic.zoho.LoginToLoadingFragments.LoginFragment;

/**
 * Created by android on 12/10/15.
 */
public class ActivityLoginToLoading extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_to_loading);
        if(savedInstanceState ==null){
            callFragment(R.id.logintoloading_container, LoginFragment.newInstance(),"LoginFragment");
        }
    }
}
