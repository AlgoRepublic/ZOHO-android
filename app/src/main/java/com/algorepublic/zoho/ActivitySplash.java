package com.algorepublic.zoho;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ahmad on 6/22/15.
 */
public class ActivitySplash extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_zoho);
        final int splashScreenDispalyTime = 1000;
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
                    startActivity(new Intent(ActivitySplash.this, ActivityLoginToLoading.class));
                    ActivitySplash.this.finish();
                }
            }
        };
        welcomeScreenThread.start();
        return;
    }
}

