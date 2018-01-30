package com.github.js.app;

import android.app.Application;

/**
 * Created by zlove on 2018/1/29.
 */

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}
