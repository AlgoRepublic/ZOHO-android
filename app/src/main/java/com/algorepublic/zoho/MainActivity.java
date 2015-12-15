package com.algorepublic.zoho;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.algorepublic.zoho.fragments.FeedFragment;
import com.algorepublic.zoho.fragments.TasksFragment;
import com.algorepublic.zoho.fragments.dummy.DummyContent;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, FeedFragment.OnListFragmentInteractionListener {

    AQuery aq,aq_header;
    BaseClass baseClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baseClass = ((BaseClass) getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout =navigationView.getHeaderView(0);
        aq_header = new AQuery(headerLayout);
        setSupportActionBar(toolbar);
        aq= new AQuery(this);
        aq_header.id(R.id.user_name).text(baseClass.getFirstName());
        aq_header.id(R.id.email).text(baseClass.getEmail());
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.feed) {
            getSupportActionBar().setTitle(getString(R.string.feeds));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, FeedFragment.newInstance(1), "FeedFragment").commit();
        } else if (id == R.id.tasks) {
            getSupportActionBar().setTitle(getString(R.string.tasks));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, TasksFragment.newInstance(), "TasksFragment").commit();
        } else if (id == R.id.documents) {
            getActionBar().setTitle(getString(R.string.documents));
        } else if (id == R.id.settings) {
            getActionBar().setTitle(getString(R.string.settings));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
