package com.example.jadynai.globalwidget;

import android.app.Application;

/**
 * 来画APP的application
 * Created by wj on 2017/9/12.
 */

public class BaseApplication extends Application {

    private final String TAG = this.getClass().getSimpleName();
    private static BaseApplication sInstance = null;


    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
    }
    
    private static void setInstance(BaseApplication instance) {
        sInstance = instance;
    }

    public static BaseApplication getInstance() {
        return sInstance;
    }
    
}
