package com.algorepublic.zoho;


import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.algorepublic.zoho.SplashToLoadingFragments.FragmentSplash;
import com.algorepublic.zoho.SplashToLoadingFragments.LoginFragment;
import com.algorepublic.zoho.services.LoginService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by android on 12/10/15.
 */
public class ActivitySplashToLoading extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_to_loading);
        if(savedInstanceState ==null){
            callFragment(R.id.splashtoloading_container, FragmentSplash.newInstance(),"FragmentSplash");
        }
    }
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        final int splashScreenDispalyTime = 3000;
        Thread welcomeScreenThread = new Thread(){
            int wait = 0;
            @Override
            public void run() {
                try {
                    super.run();
                    while (wait < splashScreenDispalyTime){
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e){
                } finally {
                    callFragmentWithStateLoss(R.id.splashtoloading_container, LoginFragment.newInstance(),"LoginFragment");
                }
            }
        };
        welcomeScreenThread.start();
        return super.onCreateView(parent, name, context, attrs);
    }
}
