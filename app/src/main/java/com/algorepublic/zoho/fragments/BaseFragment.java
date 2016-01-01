package com.algorepublic.zoho.fragments;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

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
                return getResources().getColor(android.R.color.holo_orange_light);
            case 2:
                return getResources().getColor(android.R.color.holo_green_light);
            case 3:
                return getResources().getColor(android.R.color.holo_red_light);
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
}
