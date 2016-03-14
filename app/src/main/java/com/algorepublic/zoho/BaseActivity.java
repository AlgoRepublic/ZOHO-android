package com.algorepublic.zoho;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressBaseDialog;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class BaseActivity extends AppCompatActivity {
    public static ACProgressFlower dialogAC;
    public static DrawerLayout drawer;public static Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void callFragment(int containerId, Fragment fragment, String tag){
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerId, fragment, tag)
                .commit();
    }
    public void callFragmentWithReplace(int containerId, Fragment fragment, String tag){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .commit();

    }
    public void callFragmentWithStateLoss(int containerId, Fragment fragment, String tag){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .commitAllowingStateLoss();
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();
    }
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public String getRealPathFromURI(Uri contentUri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        Log.e("Link",cursor.getString(column_index));
        return cursor.getString(column_index);
    }
    public void setToolbar(){
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }
    public void InitializeDialog(Context context){
        dialogAC = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        dialogAC.setCancelable(true);
    }

}
