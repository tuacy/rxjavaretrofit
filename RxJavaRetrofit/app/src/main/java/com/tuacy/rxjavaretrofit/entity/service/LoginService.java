package com.tuacy.rxjavaretrofit.entity.service;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface LoginService {

	@POST("AppApi/Login/AppLogin")
	Observable<String> login(@Body String body);

}
