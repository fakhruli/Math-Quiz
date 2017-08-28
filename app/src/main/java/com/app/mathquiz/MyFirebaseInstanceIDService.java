package com.app.mathquiz;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by islam on 09/08/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // display token on logcat
        Log.d(TAG, "TokenRefresh: " + refreshedToken);
        // calling method for store token
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {
        // save token to shared preference
        SharedPrefManager.getmInstance(getApplicationContext()).saveDeviceToken(token);
    }
}
