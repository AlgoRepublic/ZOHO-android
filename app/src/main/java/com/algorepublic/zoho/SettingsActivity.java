package com.algorepublic.zoho;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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
        toolbar.setTitle(baseClass.getResources().getString(R.string.settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        callFragment(R.id.container, MenuSettingsFragment.newInstance(), "FragmentMenuSettings");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this,MainActivity.class));
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
