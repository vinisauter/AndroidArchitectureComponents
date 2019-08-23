package com.vas.architecture;

import android.app.Application;

public class AppApplication extends Application {
    private static AppApplication instance_;

    public static AppApplication getInstance() {
        return instance_;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance_ = this;
    }
}
