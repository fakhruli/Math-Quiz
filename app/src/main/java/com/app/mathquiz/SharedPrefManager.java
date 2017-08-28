package com.app.mathquiz;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by islam on 09/08/17.
 */

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context ctx) {
        this.mCtx = ctx;
    }

    public static synchronized SharedPrefManager getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    // save token to shared preference
    public boolean saveDeviceToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN,token);
        editor.apply();
        return true;
    }

    // fetch token from shared preference
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_TOKEN,null);
    }
}
