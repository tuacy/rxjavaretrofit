package com.tuacy.rxjavaretrofit;

import android.os.Bundle;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.tuacy.rxjavaretrofit.entity.api.LoginApi;
import com.tuacy.rxjavaretrofit.entity.bridge.LoginBridge;
import com.tuacy.rxjavaretrofitlib.optimize.RetrofitClient;

public class MainActivity extends RxAppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
	}

	private void initData() {
		RetrofitClient manager = RetrofitClient.getInstance();
		manager.doRequest(new LoginApi(), new LoginBridge(), this.<String>bindUntilEvent(ActivityEvent.DESTROY));
	}
}
