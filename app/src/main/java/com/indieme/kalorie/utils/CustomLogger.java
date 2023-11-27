package com.indieme.kalorie.utils;

import android.util.Log;

import com.indieme.kalorie.BuildConfig;

public class CustomLogger {

    public static void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(CustomLogger.class.getSimpleName(), message);
        }
    }

    public static void log(Exception e) {
        if (BuildConfig.DEBUG) {
            Log.d(CustomLogger.class.getSimpleName(), e.getLocalizedMessage());
        }
    }
}
