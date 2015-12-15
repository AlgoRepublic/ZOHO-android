package com.algorepublic.zoho;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    public static android.content.res.Configuration conf;
    Resources res; DisplayMetrics dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         res = getResources();
        dm = res.getDisplayMetrics();
        conf = res.getConfiguration();
    }
    public void hideKeyPad(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
    public void changeLanguage(String local_language){
        conf.locale = new Locale(local_language);
        res.updateConfiguration(conf, dm);
    }
}
