package com.ibalashov.tenseconds;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

/**
 * Created by ibalashov on 7/13/2015.
 */
public class TenSecondsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseCrashReporting.enable(this);
        Parse.initialize(this, "zT6A8BeZ5v0ruPsbO4CLEEpHuxqhFivUnl08OpbP", "8TruLCUMtDiVyLlobcCBz2lmuY4PBd7zvYfu1wEl");

    }
}
