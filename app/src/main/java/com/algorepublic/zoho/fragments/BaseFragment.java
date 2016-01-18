package com.algorepublic.zoho.fragments;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;

import com.algorepublic.zoho.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    public BaseFragment() {
        // Required empty public constructor
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
        Cursor cursor = getActivity().getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    protected int getPriorityWiseColor(int priority){
        switch (priority){
            case 0:
                return getResources().getColor(android.R.color.darker_gray);
            case 1:
                return getResources().getColor(R.color.medium_priority);
            case 2:
                return getResources().getColor(R.color.low_priority);
            case 3:
                return getResources().getColor(R.color.high_priority);
            default:
                return getResources().getColor(android.R.color.darker_gray);
        }
    }
    protected void callFragment(int containerId, Fragment fragment, String tag){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(containerId, fragment, tag)
                .commit();
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();
    }
    public void callFragmentWithReplace(int containerId, Fragment fragment, String tag){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .commit();
    }
    public String DaysDifference(String milli){
        int millis;String days;
        if (System.currentTimeMillis()> Long.parseLong(milli)) {
            millis = (int)TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - Long.parseLong(milli));
            days = "Late by "+millis+" day(s)";
        }
        else{
            millis = (int)TimeUnit.MILLISECONDS.toDays(Long.parseLong(milli) - System.currentTimeMillis());
            days = +millis+" day(s) Left";
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
        return (mMonth+"/"+mDay+"/"+mYear);
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
    public String DateMilli(String date) {
        String a = date.replaceAll("\\D+", "");
        return a;
    }
    public long CharToASCII(String name){
        long value;
        if(name == null)
        {
            value =0;
        }else {
            StringBuilder ascii =  new StringBuilder();
            for (int i = 0; i < name.length(); i++) {
                ascii.append(String.valueOf((int) name.charAt(i)));
            }
            value =  Long.parseLong(ascii.toString());
        }
        return value;
    }
}
