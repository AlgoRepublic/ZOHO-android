package com.algorepublic.zoho.utils;

import android.util.Log;

public class _ {

	private static boolean DEBUG = true;

	public static void log(Object msg) {
		if (DEBUG) {
			Log.e("Doc To Me: >", "" + msg.toString());
		}

	}
}
