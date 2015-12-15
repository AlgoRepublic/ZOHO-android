package com.algorepublic.zoho.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.ProgressDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hasanali on 27/08/14.
 */
public class BaseClass extends Application {
    /**
     * Init preferences
     */

    public SharedPreferences appSharedPrefs;
    public SharedPreferences.Editor prefsEditor;
    public String SHARED_NAME = "com.algorepublic.zoho";
    //Shared preferences tags
    private String UserId = "UserId";
    private String FirstName = "FirstName";
    private String Email = "Email";
    private String UserLanguage = "UserLanguage";
    private String UserLocation = "UserLocation";
    private String UserAboutMe = "UserAboutMe";
    private String UserImageUrl = "UserImageUrl";


    @Override
    public void onCreate() {
        super.onCreate();
        this.appSharedPrefs = getSharedPreferences(SHARED_NAME,
                Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public void setEmail(String email) {
        prefsEditor.putString(Email, email).commit();
    }

    public String getEmail() {
        return appSharedPrefs.getString(Email, "");
    }

    public void setFirstName(String userName) {
        prefsEditor.putString(FirstName, userName).commit();
    }

    public String getFirstName() {
        return appSharedPrefs.getString(FirstName, "");
    }

    public void setUserId(String userName) {
        prefsEditor.putString(UserId, userName).commit();
    }

    public String getUserId() {
        return appSharedPrefs.getString(UserId, "");
    }

    public void setUserLanguage(String userLanguage) {
        prefsEditor.putString(UserLanguage, userLanguage).commit();
    }

    public String getUserLanguage() {
        return appSharedPrefs.getString(UserLanguage, "");
    }

    public void setUserLocation(String userLocation) {
        prefsEditor.putString(UserLocation, userLocation).commit();
    }

    public String getUserLocation() {
        return appSharedPrefs.getString(UserLocation, "");
    }

    public void setUserAboutMe(String userAboutme) {
        prefsEditor.putString(UserAboutMe, userAboutme).commit();
    }

    public String getUserAboutMe() {
        return appSharedPrefs.getString(UserAboutMe, "");
    }

    public void setUserImageUrl(String userImage) {
        prefsEditor.putString(UserImageUrl, userImage).commit();
    }

    public String getUserImageUrl() {
        return appSharedPrefs.getString(UserImageUrl, "");
    }

    public void clearSharedPrefs() {
        prefsEditor.clear().commit();
    }

    public boolean isNetworkAvailble(Context ctx) {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork == null ||
                !activeNetwork.isConnectedOrConnecting()) {
//            Crouton.makeText((Activity) ctx,
//                    "Please check internet or server not responding", Style.ALERT).show();
            return false;
        } else
            return true;
    }

    public static int isTabletDevice(Context activityContext) {
        boolean xlarge = ((activityContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        UiModeManager uiModeManager = (UiModeManager) activityContext.getSystemService(UI_MODE_SERVICE);

        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            return 2;
        } else {
            if (xlarge) {
                DisplayMetrics metrics = new DisplayMetrics();
                Activity activity = (Activity) activityContext;
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                        || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                        || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                        || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                        || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                    return 1;
                }
            }
            return 0;
        }
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public Boolean isActivityRunning(Class activityClass) {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }
        return false;
    }

    public String TimeAgo(long time) {

        List<Long> times = Arrays.asList(
                TimeUnit.DAYS.toMillis(365),
                TimeUnit.DAYS.toMillis(30),
                TimeUnit.DAYS.toMillis(1),
                TimeUnit.HOURS.toMillis(1),
                TimeUnit.MINUTES.toMillis(1),
                TimeUnit.SECONDS.toMillis(1));
        List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");
        return toDuration(time, times, timesString);
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public String toDuration(long duration, List<Long> times, List<String> timesString) {

        StringBuffer res = new StringBuffer();
        for (int i = 0; i < times.size(); i++) {
            Long current = times.get(i);
            long temp = duration / current;
            if (temp > 0) {
                res.append(temp).append(" ").append(timesString.get(i)).append(temp > 1 ? "s" : "").append(" ago");
                break;
            }
        }
        if ("".equals(res.toString()))
            return "0 second ago";
        else
            return res.toString();
    }

    public static Long getTimeStampToMilli(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date obj = sdf.parse(date);
            return obj.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public static int getDpValue(int val, Context ctx) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, val, ctx.getResources()
                        .getDisplayMetrics());
    }
}
