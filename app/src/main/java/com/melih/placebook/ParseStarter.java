package com.melih.placebook;

import android.app.Application;

import com.parse.Parse;

public class ParseStarter extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

         Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
         Parse.initialize(new Parse.Configuration.Builder(this)
                 .applicationId("4fWQO2MjAn2Ikl1Ri3KrnooQ5UkdRtlUmSKZA9zT")
                 .clientKey("TGSfCspsfSDz5W2N2WCuRSRiQp6OuLZ5M06Npm10")
                 .server("https://parseapi.back4app.com/").build());
    }
}
