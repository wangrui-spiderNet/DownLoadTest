package com.zxs.test;

import android.app.Application;

import com.zxs.test.utils.AppConfig;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		AppConfig.setContext(this);
	}
}
