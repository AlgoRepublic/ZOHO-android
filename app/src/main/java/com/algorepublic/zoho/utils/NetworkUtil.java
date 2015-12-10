package com.algorepublic.zoho.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

public class NetworkUtil {
    private static Activity mContext;
    private static AlertDialog.Builder mAlertDialogBuilder = null;
    private static Dialog mDialog = null;

    public static boolean isInternetConnected(Activity context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {

                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    status = true;
                }
                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    status = true;
                }
                status = activeNetwork != null &&
                        activeNetwork.isConnected();
            }
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    /**
     * @param context
     */
    public static void internetFailedDialog(Context context) {
        try {
            if(mAlertDialogBuilder != null && mDialog != null && mDialog.isShowing()){
                mDialog.cancel();
                mDialog.dismiss();
                mDialog = null;
                mAlertDialogBuilder = null;
            }
                mAlertDialogBuilder = new AlertDialog.Builder(
                        context);
                mAlertDialogBuilder.setTitle("Internet Problem");
                mAlertDialogBuilder
                        .setMessage("Please check your internet connection than try again!");
                mAlertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                mAlertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialog.cancel();
                        mAlertDialogBuilder = null;
                    }
                });
                mDialog = mAlertDialogBuilder.create();
                mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param act
     * @param message
     * @param
     */
    public static void showStatusDialog(final Activity act, String title, String message, final boolean isToken) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                act);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        alertDialogBuilder.create().show();

    }

    /**
     * Checks if internet is connected/available by pinning google.com
     * @return
     * boolean i.e. true if internet is connected and false otherwise.
     */
    public static Boolean isOnline() {
         Runtime runtime = Runtime.getRuntime();
            try {

                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int     exitValue = ipProcess.waitFor();
                return (exitValue == 0);

            } catch (IOException e)          { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }

            return false;

    }
}
