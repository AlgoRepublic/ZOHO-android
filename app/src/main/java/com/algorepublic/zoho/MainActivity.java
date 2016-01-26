package com.algorepublic.zoho;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.algorepublic.zoho.fragments.CalendarFragment;
import com.algorepublic.zoho.fragments.DocumentsListFragment;
import com.algorepublic.zoho.fragments.FeedFragment;
import com.algorepublic.zoho.fragments.ProjectsFragment;
import com.algorepublic.zoho.fragments.TasksListFragment;
import com.algorepublic.zoho.fragments.dummy.DummyContent;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

public class MainActivity extends BaseActivity
        implements FeedFragment.OnListFragmentInteractionListener {

    AQuery aq,aq_header;
    BaseClass baseClass;
    DrawerLayout drawer;
    RadioGroup radioGroup1,radioGroup2,radioGroup3;
    RadioGroup.OnCheckedChangeListener changeListener1,changeListener2,changeListener3;

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
        radioGroup1 = (RadioGroup) headerLayout.findViewById(R.id.radioGroup1);
        radioGroup2 = (RadioGroup) headerLayout.findViewById(R.id.radioGroup2);
        radioGroup3 = (RadioGroup) headerLayout.findViewById(R.id.radioGroup3);
        aq= new AQuery(this);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            aq_header.id(R.id.view).visibility(View.GONE);
        }
        aq_header.id(R.id.user_name).text(baseClass.getFirstName());
        aq_header.id(R.id.email).text(baseClass.getEmail());
        changeListener1 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioGroup1(checkedId);
                UpdateRadioGroup2();
                UpdateRadioGroup3();
            }
        };
        changeListener2 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioGroup2(checkedId);
                UpdateRadioGroup1();
                UpdateRadioGroup3();
            }
        };
        changeListener3 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioGroup3(checkedId);
                UpdateRadioGroup1();
                UpdateRadioGroup2();
            }
        };

        radioGroup1.setOnCheckedChangeListener(changeListener1);
        radioGroup2.setOnCheckedChangeListener(changeListener2);
        radioGroup3.setOnCheckedChangeListener(changeListener3);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


//        ImageView settings = (ImageView) headerLayout.findViewById(R.id.settings);
//
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
//            }
//        });


       // navigationView.setNavigationItemSelectedListener(this);
    }
    public void RadioGroup1(int checkedId) {
        switch (radioGroup1.indexOfChild(findViewById(checkedId))) {
            case 0:
                getSupportActionBar().setTitle(getString(R.string.feeds));
                getSupportFragmentManager().beginTransaction().replace(R.id.container, FeedFragment.newInstance(), "FragmentFeeds").commit();
                aq.id(R.id.feed_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.schedule_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
            case 1:
                getSupportActionBar().setTitle(getString(R.string.calendar));
                getSupportFragmentManager().beginTransaction().replace(R.id.container, CalendarFragment.newInstance(), "FragmentCalendar").commit();
                aq.id(R.id.schedule_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.feed_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
    }
    public void RadioGroup2(int checkedId) {
        switch (radioGroup2.indexOfChild(findViewById(checkedId))) {
            case 0:
                getSupportActionBar().setTitle(getString(R.string.tasks));
                getSupportFragmentManager().beginTransaction().replace(R.id.container, TasksListFragment.newInstance(), "TasksListFragment").commit();
                aq.id(R.id.tasks_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.documents_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
            case 1:
                getSupportActionBar().setTitle(getString(R.string.documents));
                getSupportFragmentManager().beginTransaction().replace(R.id.container, DocumentsListFragment.newInstance(), "DocumentsListFragment").commit();
                aq.id(R.id.documents_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.tasks_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
    }
    public void RadioGroup3(int checkedId) {
        switch (radioGroup3.indexOfChild(findViewById(checkedId))) {
            case 0:
                aq.id(R.id.user_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.project_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
            case 1:
                getSupportActionBar().setTitle(getString(R.string.projects));
                getSupportFragmentManager().beginTransaction().replace(R.id.container, ProjectsFragment.newInstance(), "ProjectsFragment").commit();
                aq.id(R.id.project_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.user_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
    }
    public void UpdateRadioGroup1()
    {
        radioGroup1.setOnCheckedChangeListener(null);
        radioGroup1.clearCheck();
        radioGroup1.setOnCheckedChangeListener(changeListener1);
        aq.id(R.id.feed_radioButton).textColor(getResources().getColor(android.R.color.white));
        aq.id(R.id.schedule_radioButton).textColor(getResources().getColor(android.R.color.white));
    }
    public void UpdateRadioGroup2()
    {
        radioGroup2.setOnCheckedChangeListener(null);
        radioGroup2.clearCheck();
        radioGroup2.setOnCheckedChangeListener(changeListener2);
        aq.id(R.id.tasks_radioButton).textColor(getResources().getColor(android.R.color.white));
        aq.id(R.id.documents_radioButton).textColor(getResources().getColor(android.R.color.white));
    }
    public void UpdateRadioGroup3()
    {
        radioGroup3.setOnCheckedChangeListener(null);
        radioGroup3.clearCheck();
        radioGroup3.setOnCheckedChangeListener(changeListener3);
        aq.id(R.id.user_radioButton).textColor(getResources().getColor(android.R.color.white));
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

//    public void TasksList(Object caller, Object model) {
//        TasksListModel.getInstance().setList((TasksListModel) model);
//        if (TasksListModel.getInstance().responseCode == 100) {
//            GetGeneralList();
//        }
//        else
//        {
//            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
//        }
//    }

    public void ProjectsListCallback(Object caller, Object model){


    }


//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        if (id == R.id.feed) {
//            getSupportActionBar().setTitle(getString(R.string.feeds));
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, FeedFragment.newInstance(1), "FeedFragment").commit();
//        } else if (id == R.id.tasks) {
//            getSupportActionBar().setTitle(getString(R.string.tasks));
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, TasksListFragment.newInstance(), "TasksListFragment").commit();
//        } else if (id == R.id.documents) {
//            getSupportActionBar().setTitle(getString(R.string.documents));
//        } else if (id == R.id.settings) {
//            getSupportActionBar().setTitle(getString(R.string.settings));
//        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
