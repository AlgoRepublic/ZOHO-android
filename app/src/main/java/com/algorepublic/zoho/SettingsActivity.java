package com.algorepublic.zoho;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.algorepublic.zoho.fragments.MenuSettingsFragment;
import com.algorepublic.zoho.utils.BaseClass;


public class SettingsActivity extends BaseActivity {

    BaseClass baseClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseClass = ((BaseClass) getApplicationContext());
        if (baseClass.getThemePreference() == R.style.AppThemeBlue)
            setTheme(R.style.AppThemeBlue);
        else
            setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.settings));
        callFragment(R.id.container, MenuSettingsFragment.newInstance(), "FragmentMenuSettings");
    }
}
