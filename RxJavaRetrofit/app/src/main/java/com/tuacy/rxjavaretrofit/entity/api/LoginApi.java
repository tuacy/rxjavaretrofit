package com.tuacy.rxjavaretrofit.entity.api;


import com.google.gson.Gson;

import com.tuacy.rxjavaretrofit.entity.request.LoginRequest;
import com.tuacy.rxjavaretrofit.entity.service.LoginService;
import com.tuacy.rxjavaretrofitlib.optimize.RetrofitBaseApi;

import retrofit2.Retrofit;
import rx.Observable;

public class LoginApi extends RetrofitBaseApi {

	@Override
	public Observable<String> getObservable(Retrofit retrofit) {
		LoginRequest request = new LoginRequest("xgadmin", "pilot@");
		return retrofit.create(LoginService.class).login(new Gson().toJson(request));
	}

	@Override
	public int uuid() {
		return 0;
	}
}
