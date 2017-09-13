package com.selfimpr.map;

import android.app.Application;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2017/9/13 下午1:55<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class MApplication extends Application {
    private static MApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static MApplication getApplication() {
        return application;
    }
}
