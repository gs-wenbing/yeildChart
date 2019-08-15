package com.zwb.yeildchart;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * @author Administrator
 * @date 2019/8/7 15:19
 */
public class YeildApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
    }
}
