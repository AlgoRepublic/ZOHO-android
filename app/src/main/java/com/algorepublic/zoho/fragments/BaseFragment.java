package com.algorepublic.zoho.fragments;


import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {
    public static SearchView searchView;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void changeLanguage(String local_language){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(local_language);
        res.updateConfiguration(conf, dm);
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
    public ACProgressFlower InitializeDialog(Context context){
        ACProgressFlower dialogAC = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        dialogAC.setCancelable(true);
        return dialogAC;
    }
    public File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Zoho");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp
                + ".jpg");
        return mediaFile;
    }
    public String getRealPathFromURI(Uri contentUri)
    {
        String path = null;
        String[] projection = new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.ImageColumns.ORIENTATION};
        Cursor cursor = getActivity().getContentResolver().
                query(contentUri,
                        projection, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
        cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
                Uri uri = Uri.parse(cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA)));
                path = uri.toString();
            cursor.close();
    }
        return path.toString();
    }

    protected int getPriorityWiseColor(int priority){
        switch (priority){
            case 0:
                return getActivity().getResources().getColor(android.R.color.darker_gray);
            case 1:
                return getResources().getColor(R.color.medium_priority);
            case 2:
                return getResources().getColor(R.color.low_priority);
            case 3:
                return getResources().getColor(R.color.high_priority);
            default:
                return getActivity().getResources().getColor(android.R.color.darker_gray);
        }
    }
    protected void callFragment(int containerId, Fragment fragment, String tag){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(containerId, fragment, tag)
                .setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit,
                        R.anim.slide_pop_enter, R.anim.slide_pop_exit)
                .commit();
    }
    protected void callFragmentWithAddBackStack(int containerId, Fragment fragment, String tag){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(containerId, fragment, tag)
                .setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit,
                        R.anim.slide_pop_enter, R.anim.slide_pop_exit)
                .addToBackStack(null)
                .commit();
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit,
                        R.anim.slide_pop_enter, R.anim.slide_pop_exit)
                .addToBackStack(null)
                .commit();
    }
    public void callFragmentWithReplace(int containerId, Fragment fragment, String tag){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit,
                        R.anim.slide_pop_enter, R.anim.slide_pop_exit)
                .commit();
    }
    public String DaysDifference(String milli){
        int millis;String days;
        if (System.currentTimeMillis()> Long.parseLong(milli)) {
            millis = (int)TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - Long.parseLong(milli));
            days = getString(R.string.late_by)+millis+getString(R.string.days);
        }
        else{
            millis = (int)TimeUnit.MILLISECONDS.toDays(Long.parseLong(milli) - System.currentTimeMillis());
            days = +millis+getString(R.string.days_left);
        }
        return days;
    }
    public String DateFormatter(String date){
        String a = date.replaceAll("\\D+", "");
        long timeInMillis = Long.parseLong(a);
        if(timeInMillis < 0 )
            return "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH) + 1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        return (mDay+"/"+mMonth+"/"+mYear);
    }
    public Uri getUriFromUrl(String thisUrl) {
        Uri.Builder builder;
        URL url = null;
        try {
            url = new URL(thisUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        builder =  new Uri.Builder()
                .scheme(url.getProtocol())
                .authority(url.getAuthority())
                .appendPath(url.getPath());
        return builder.build();
    }
    public long DateHeader(String date){
        String a = date.replaceAll("\\D+", "");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(a));
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    public String GetDateTime(){
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(calendar.getTime());
        String delegate = "hh:mm aaa";
        String time  = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());

        return (month_name+" "+mDay +", "+mYear+" "+time);
    }
    public String GetDateTimeComment(String milli){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(DateMilli(milli)));
        int mYear = calendar.get(Calendar.YEAR);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(calendar.getTime());
        String delegate = "hh:mm aaa";
        String time  = (String) DateFormat.format(delegate, calendar.getTime());

        return (month_name+" "+mDay +", "+mYear+" "+time);
    }

    public String DateMilli(String date) {
        String a = date.replaceAll("\\D+", "");
        return a;
    }

    public void setToolbar(){
//        ((AppCompatActivity)getActivity()).setSupportActionBar(BaseActivity.toolbar);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                getActivity(), BaseActivity.drawer, BaseActivity.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        BaseActivity.drawer.setDrawerListener(toggle);
//        toggle.syncState();
    }

    protected Toolbar getToolbar(){
        return BaseActivity.toolbar;
    }

    public void ClearAllFragments(){
        for(int loop=0;loop<getActivity().getSupportFragmentManager().getBackStackEntryCount();loop++)
        {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    protected void applyDarkBackground(View view, BaseClass baseClass){
        if(baseClass.getThemePreference() == R.style.AppThemeBlue)
            view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryBlue));
        else
            view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorBaseHeader));
    }

    protected void applyLightBackground(View view, BaseClass baseClass){
        if(baseClass.getThemePreference() == R.style.AppThemeBlue)
            view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorSecondaryBlue));
        else
            view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorContentWrapper));
    }
}
