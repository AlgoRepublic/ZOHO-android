package com.algorepublic.zoho.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;

import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.R;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private String LastName = "LastName";
    private String ProfileImagePath = "ProfileImagePath";
    private String ProfileImageID = "ProfileImageID";
    private String Email = "Email";
    private String UserLanguage = "UserLanguage";
    private String SELECTED_PROJECT = "selected_project";
    private String SELECTED_TASKPROJECT = "selected_taskproject";
    private String TaskFilterType = "TaskFilterType";
    private String TaskSortType = "TaskSortType";
    private String DocsSortType = "DocsSortType";
    private String taskListName;
    public static boolean PERMISSION=false;
    public static TinyDB db;
    private String themePreference = "themePreference";
    private static BaseClass instance;

    public static BaseClass getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.appSharedPrefs = getSharedPreferences(SHARED_NAME,
                Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
        db = new TinyDB(this);
        if (LocaleHelper.getLanguage(this).equalsIgnoreCase("ar"))
            LocaleHelper.onCreate(this, "ar");
        else
            LocaleHelper.onCreate(this, "en");

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public void setSelectedProject(String selectedProject) {
        prefsEditor.putString(SELECTED_PROJECT, selectedProject).commit();
    }
    public String getSelectedProject() {
        return appSharedPrefs.getString(SELECTED_PROJECT, "0");
    }

    public void setSelectedTaskProject(String selectedProject) {
        prefsEditor.putString(SELECTED_TASKPROJECT, selectedProject).commit();
    }
    public String getSelectedTaskProject() {
        return appSharedPrefs.getString(SELECTED_TASKPROJECT, "0");
    }

    public void setTaskListName(String taskList) {
        taskListName = taskList;
    }

    public String getTaskListName() {
        return taskListName;
    }

    public void setEmail(String email) {
        prefsEditor.putString(Email, email).commit();
    }

    public String getEmail() {
        return appSharedPrefs.getString(Email, "");
    }

    public void setThemePreference(int theme) {
        prefsEditor.putInt(themePreference, theme).commit();
    }

    public int getThemePreference() {
        return appSharedPrefs.getInt(themePreference, 0);
    }

    public void setLastName(String lastName) {
        prefsEditor.putString(LastName, lastName).commit();
    }

    public String getLastName() {
        return appSharedPrefs.getString(LastName, "");
    }

    public void setProfileImage(String profileImage) {
        prefsEditor.putString(ProfileImagePath, profileImage).commit();
    }

    public String getProfileImage() {
        return appSharedPrefs.getString(ProfileImagePath, "");
    }

    public void setProfileImageID(Integer profileImage) {
        prefsEditor.putInt(ProfileImageID, profileImage).commit();
    }

    public Integer getProfileImageID() {
        return appSharedPrefs.getInt(ProfileImageID, 0);
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
        return appSharedPrefs.getString(UserLanguage, "en");
    }

    public void setTaskFilterType(String taskFilterType) {
        prefsEditor.putString(TaskFilterType, taskFilterType).commit();
    }

    public String getTaskFilterType() {
        return appSharedPrefs.getString(TaskFilterType, "DueDate");
    }
    public void setTaskSortType(String taskSortType) {
        prefsEditor.putString(TaskSortType, taskSortType).commit();
    }

    public String getTaskSortType() {
        return appSharedPrefs.getString(TaskSortType, "All");
    }

    public void setDocsSortType(String docsSortType) {
        prefsEditor.putString(DocsSortType, docsSortType).commit();
    }

    public String getDocsSortType() {
        return appSharedPrefs.getString(DocsSortType, "AllFiles");
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
    public EditText.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                showKeyPad(v);
            }else
                hideKeyPad(v);
        }
    };
    public Bitmap adjustImageOrientation(File f) {
        int rotate = 0;
        try {

            ExifInterface exif = new ExifInterface(f.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                    rotate = 0;
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap,80,80,true);
        if (rotate != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }
        return bitmap;
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

    static int icon[] = {
            R.drawable.ic_listview_txt,
            R.drawable.ic_listview_jpg,
            R.drawable.ic_listview_jpeg,
            R.drawable.ic_listview_png,
            R.drawable.ic_listview_gif,
            R.drawable.ic_listview_pdf,
            R.drawable.ic_listview_word,
            R.drawable.ic_listview_word,
            R.drawable.ic_listview_mp3,
            R.drawable.ic_listview_mp4a,
            R.drawable.ic_listview_wav,
            R.drawable.ic_listview_mp4,
            R.drawable.ic_listview_3gp,
            R.drawable.ic_listview_txt,
            R.drawable.ic_listview_xml,
            R.drawable.ic_listview_html,
    };
    public void hideKeyPad(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
    public void showKeyPad(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_HIDDEN);
    }
    public static int getIcon(int type){
        return icon[type];
    }
    public static String getExtensionType(String fileName) {
        String encoded;
        try {
            encoded = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            encoded = fileName;
        }
        return MimeTypeMap.getFileExtensionFromUrl(encoded).toLowerCase();
    }
    public static int getExtension(String fileName) {
        String encoded;int type=0;
        try { encoded = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20"); }
        catch(UnsupportedEncodingException e) { encoded = fileName; }
        String extention = MimeTypeMap.getFileExtensionFromUrl(encoded).toLowerCase();
        if(extention.contains("jpg"))
            type=1;
        if(extention.contains("jpeg"))
            type=2;
        if(extention.contains("png"))
            type=3;
        if(extention.contains("gif"))
            type=4;
        if(extention.contains("pdf"))
            type=5;
        if(extention.contains("doc"))
            type=6;
        if(extention.contains("docx"))
            type=7;
        if(extention.contains("mp3/mpeg"))
            type=8;
        if(extention.contains("mp4a"))
            type=9;
        if(extention.contains("wav"))
            type=10;
        if(extention.contains("mp4"))
            type=11;
        if(extention.contains("3gpp"))
            type=12;
        if(extention.contains("text/plain"))
            type=13;
        if(extention.contains("xml"))
            type=14;
        if(extention.contains("html"))
            type=15;

        return type;
    }


    public String GetTime(String milli){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milli));

        String delegate = "hh:mm aaa";
        String time  = (String) DateFormat.format(delegate, calendar.getTime());
        return (time);
    }
    public String DateMilli(String date) {
        String a = date.replaceAll("\\D+", "");
        return a;
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
    /** Update background and text color on item selection*/
    public static void setViewAccordingToTheme(Context mContext,View convertView,TextView lblHeader) {
        if (MainActivity.themeType == 1) {
            lblHeader.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            convertView.findViewById(R.id.listClick).setBackground(mContext.getResources().getDrawable(R.drawable.roundable_cornor_selected_blue));
            // ((ImageView) convertView.findViewById(R.id.imageViewlevelone)).setImageDrawable(drawable);
        } else {
            lblHeader.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            convertView.findViewById(R.id.listClick).setBackground(mContext.getResources().getDrawable(R.drawable.roundable_corner_selected));
            // ((ImageView) convertView.findViewById(R.id.imageViewlevelone)).setImageDrawable(drawable);
        }
    }

    /**
     * @param permissionID
     */
    public static boolean hasPermission(String permissionID){
        for(int contain: db.getListInt("Permissions")){
            if(contain==Integer.parseInt(permissionID))
                return true;
        }
        return false;
    }

    public static Map<String, String> getHashMapResource(Context c, int hashMapResId) {
        Map<String, String> map = null;
        XmlResourceParser parser = c.getResources().getXml(hashMapResId);

        String key = null, value = null;

        try {
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("utils", "Start document");
                }
                else if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("map")) {
                        boolean isLinked = parser.getAttributeBooleanValue(null, "linked", false);

                        map = isLinked
                                ? new LinkedHashMap<String, String>()
                                : new HashMap<String, String>();
                    }
                    else if (parser.getName().equals("entry")) {
                        key = parser.getAttributeValue(null, "key");

                        if (null == key) {
                            parser.close();
                            return null;
                        }
                    }
                }
                else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("entry")) {
                        map.put(key, value);
                        key = null;
                        value = null;
                    }
                }
                else if (eventType == XmlPullParser.TEXT) {
                    if (null != key) {
                        value = parser.getText();
                    }
                }
                eventType = parser.next();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return map;
    }
}
