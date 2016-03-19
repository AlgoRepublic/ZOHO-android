package com.algorepublic.zoho;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.algorepublic.zoho.adapters.AdapterMenuItems;
import com.algorepublic.zoho.fragments.HomeFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;

public class MainActivity extends BaseActivity {

    AQuery aq,aq_header;
    BaseClass baseClass;
    int themeType;
    public static GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseClass = ((BaseClass) getApplicationContext());

        if(baseClass.getThemePreference() == R.style.AppThemeBlue) {
            setTheme(R.style.AppThemeBlue);
            themeType =1;
        }else {
            setTheme(R.style.AppTheme);
            themeType =0;
        }
        new AgendaCalendarView(this,themeType);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setToolbar();
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    baseClass.hideKeyPad(v);
                } else {
                    baseClass.hideKeyPad(v);
                }
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout =navigationView.getHeaderView(0);
        gridView = (GridView) headerLayout.findViewById(R.id.gridview_menu);
        aq_header = new AQuery(headerLayout);

        aq= new AQuery(this);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            aq_header.id(R.id.view).visibility(View.GONE);
        }
        UpdateValues();
        if(baseClass.getThemePreference() == R.style.AppThemeBlue)
            aq_header.id(R.id.layout_menu).background(R.color.colorPrimaryBlue);
        else
            aq_header.id(R.id.layout_menu).background(R.color.colorBaseHeader);

        ImageView settings = (ImageView) headerLayout.findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                MainActivity.this.finish();
            }
        });
        callFragment(R.id.container, HomeFragment.newInstance(), "HomeFragment");
        gridView.setAdapter(new AdapterMenuItems(this));

    }

    @Override
    protected void onResume() {
        UpdateValues();
        super.onResume();
    }
    public void UpdateValues(){
        aq_header.id(R.id.first_name).text(baseClass.getFirstName());
        aq_header.id(R.id.last_name).text(baseClass.getLastName());
        aq_header.id(R.id.email).text(baseClass.getEmail());
        if(baseClass.getProfileImage() != null) {
            Glide.with(this).load(Constants.Image_URL + baseClass.getProfileImageID()
                    + "." + BaseClass.getExtensionType
                    (baseClass.getProfileImage())).into(aq_header.id(R.id.profile).getImageView());
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

}
