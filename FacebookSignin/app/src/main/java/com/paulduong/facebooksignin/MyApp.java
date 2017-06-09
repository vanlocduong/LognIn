package com.paulduong.facebooksignin;

import android.app.Application;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by PaulDuong on 6/9/2017.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppEventsLogger.activateApp(this);
    }
}
