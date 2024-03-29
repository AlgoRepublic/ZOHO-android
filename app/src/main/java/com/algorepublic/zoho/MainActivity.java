package com.algorepublic.zoho;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.algorepublic.zoho.adapters.AdapterMenuItems;
import com.algorepublic.zoho.fragments.HomeFragment;
import com.algorepublic.zoho.fragments.StarRatingFragment;
import com.algorepublic.zoho.fragments.UploadDocsFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    AQuery aq,aq_header;
    BaseClass baseClass;
    public static int themeType;
    public static GridView gridView;
    private List<String> menu_names=null;
    private List<Integer> menu_icon_white=null;
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
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View v) {
                baseClass.hideKeyPad(v);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

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

        callFragmentWithBackStack(R.id.container, HomeFragment.newInstance(), getString(R.string.dashboard));
        initLists();
        gridView.setAdapter(new AdapterMenuItems(this, menu_names, menu_icon_white));
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(getSupportFragmentManager().getBackStackEntryCount()>0) {
                    FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
                    String name = backStackEntry.getName();
                    Log.i("TopOfStack", name + "");
                    if(name !=null)
                    toolbar.setTitle(name);
                }
            }
        });
    }

    /**
     * create Menu Items and apply permissions.
     */
    private void initLists(){
        menu_names = new ArrayList<>();
        menu_icon_white= new ArrayList<>();
        menu_names.add(getString(R.string.dashboard));
        menu_names.add(getString(R.string.projects));
        menu_names.add(getString(R.string.tasks));
        menu_names.add(getString(R.string.calendar));
        menu_names.add(getString(R.string.documents));
        menu_names.add(getResources().getString(R.string.users));
        menu_names.add(getResources().getString( R.string.forums));
        menu_names.add(getResources().getString(R.string.star_rating));
        menu_names.add(getResources().getString(R.string.departments));

        menu_icon_white.add(R.drawable.dashboard);
        menu_icon_white.add(R.drawable.projects);
        menu_icon_white.add(R.drawable.task);
        menu_icon_white.add(R.drawable.calendar);
        menu_icon_white.add(R.drawable.documents);
        menu_icon_white.add(R.drawable.users);
        menu_icon_white.add(R.drawable.forums);
        menu_icon_white.add(R.drawable.starrating);
        menu_icon_white.add(R.drawable.departments);
        applyPermissions(this);
    }

    private void applyPermissions(Context context){
        if(!baseClass.hasPermission(context.getString(R.string.star_rating_view))){
            updateListsOnPermissions(getString(R.string.star_rating),R.drawable.starrating);
        }

        if(!baseClass.hasPermission(context.getString(R.string.users_view))){
            updateListsOnPermissions(getString(R.string.users),R.drawable.users);
        }

        if(!baseClass.hasPermission(context.getString(R.string.documents_view))){
            updateListsOnPermissions(getString(R.string.documents),R.drawable.documents);
        }
        if(!baseClass.hasPermission(context.getString(R.string.calendar_view))){
            updateListsOnPermissions(getString(R.string.calendar),R.drawable.calendar);
        }
        if(!baseClass.hasPermission(context.getString(R.string.projects_view))){
            updateListsOnPermissions(getString(R.string.projects),R.drawable.projects);
        }
        if(!baseClass.hasPermission(context.getString(R.string.tasks_view))){
            updateListsOnPermissions(getString(R.string.tasks),R.drawable.task);
        }
    }

    private void updateListsOnPermissions(String mName,int mIcon){
        menu_names.remove(mName);
        menu_icon_white.remove((Object)mIcon);
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
        StarRatingFragment.levelOneHead.clear();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int stackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (stackEntryCount==1 && getSupportFragmentManager().getBackStackEntryAt(0).getName().equalsIgnoreCase(getString(R.string.dashboard))){
            String content = getString(R.string.exit);
            final NormalDialog dialog = new NormalDialog(((AppCompatActivity) this));
            dialog.isTitleShow(false)//
                    .bgColor(this.getResources().getColor(R.color.colorBaseWrapper))//
                    .cornerRadius(5)//
                    .content(content)//
                    .contentGravity(Gravity.CENTER)//
                    .contentTextColor(this.getResources().getColor(R.color.colorBaseHeader))//
                    .dividerColor(this.getResources().getColor(R.color.colorContentWrapper))//
                    .btnTextSize(15.5f, 15.5f)//
                    .btnTextColor(this.getResources().getColor(R.color.colorBaseHeader)
                            , this.getResources().getColor(R.color.colorBaseHeader))//
                    .btnPressColor(this.getResources().getColor(R.color.colorBaseMenu))//
                    .widthScale(0.85f)//
                    .showAnim(new BounceLeftEnter())//
                    .dismissAnim(new SlideRightExit())//
                    .show();

            dialog.setOnBtnClickL(
                    new OnBtnClickL() {
                        @Override
                        public void onBtnClick() {
                            dialog.dismiss();
                        }
                    },
                    new OnBtnClickL() {
                        @Override
                        public void onBtnClick() {
                            dialog.dismiss();
                            finish();
                        }
                    });

        }else {
            super.onBackPressed();
            if(getSupportFragmentManager().getBackStackEntryCount()==0) {
                callFragmentWithBackStack(R.id.container, HomeFragment.newInstance(), getString(R.string.dashboard));
                ((AdapterMenuItems)gridView.getAdapter()).lastPosition=0;
                ((AdapterMenuItems)gridView.getAdapter()).notifyDataSetChanged();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("code1", requestCode + "/" + resultCode + "/");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if(fragment instanceof UploadDocsFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
