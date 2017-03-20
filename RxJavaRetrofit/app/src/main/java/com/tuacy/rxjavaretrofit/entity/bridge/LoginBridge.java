package com.tuacy.rxjavaretrofit.entity.bridge;

import android.util.Log;

import com.tuacy.rxjavaretrofit.entity.parse.LoginParse;
import com.tuacy.rxjavaretrofitlib.optimize.exception.RetrofitException;
import com.tuacy.rxjavaretrofitlib.optimize.parse.RetrofitBridgeBase;
import com.tuacy.rxjavaretrofitlib.optimize.parse.RetrofitParser;


public class LoginBridge extends RetrofitBridgeBase<String> {

	@Override
	public RetrofitParser<String> getRetrofitParser() {
		return new LoginParse();
	}

	@Override
	public void onRequestStart(int requestId) {
		Log.d("tuacy", "start");
	}

	@Override
	public void onRequestComplete(int requestId, String result) {
		Log.d("tuacy", "complete result = " + result);
	}

	@Override
	public void onRequestError(int requestId, RetrofitException exception) {
		Log.d("tuacy", "error");
	}
}
