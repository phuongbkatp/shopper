package com.mcc.fshopper.app;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Ashiq on 4/13/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("==", "Refreshed token: " + refreshedToken);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

    }
}
