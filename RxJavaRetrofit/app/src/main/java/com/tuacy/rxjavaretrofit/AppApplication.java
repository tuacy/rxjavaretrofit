package com.tuacy.rxjavaretrofit;

import android.app.Application;

import com.tuacy.rxjavaretrofitlib.optimize.RetrofitClient;

public class AppApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		RetrofitClient.retrofitInit(this);
	}
}
