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
import com.algorepublic.zoho.fragments.FeedFragment;
import com.algorepublic.zoho.fragments.dummy.DummyContent;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

public class MainActivity extends BaseActivity
        implements FeedFragment.OnListFragmentInteractionListener {

    AQuery aq,aq_header;
    BaseClass baseClass;
    public static GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setToolbar();
        baseClass = ((BaseClass) getApplicationContext());
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout =navigationView.getHeaderView(0);
        gridView = (GridView) headerLayout.findViewById(R.id.gridview_menu);
                aq_header = new AQuery(headerLayout);

        aq= new AQuery(this);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            aq_header.id(R.id.view).visibility(View.GONE);
        }
        aq_header.id(R.id.user_name).text(baseClass.getFirstName());
        aq_header.id(R.id.email).text(baseClass.getEmail());

        ImageView settings = (ImageView) headerLayout.findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
        gridView.setAdapter(new AdapterMenuItems(this));
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

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
