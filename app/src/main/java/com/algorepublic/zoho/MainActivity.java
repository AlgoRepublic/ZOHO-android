package com.algorepublic.zoho;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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

public class MainActivity extends BaseActivity {

    AQuery aq,aq_header;
    BaseClass baseClass;
    public static int themeType;
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
        StarRatingFragment.levelOneHead.clear();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (getSupportFragmentManager().getBackStackEntryCount()==0){
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

        }else
            super.onBackPressed();
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
