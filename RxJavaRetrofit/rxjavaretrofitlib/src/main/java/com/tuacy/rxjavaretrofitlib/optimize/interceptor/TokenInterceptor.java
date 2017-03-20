package com.tuacy.rxjavaretrofitlib.optimize.interceptor;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 我们自定义一些header信息
 */
public class TokenInterceptor implements Interceptor {

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request originalRequest = chain.request();
		if (true) {
			return chain.proceed(originalRequest);
		}
		Request authorised = originalRequest.newBuilder().header("tuacy", "mi 2s").build();
		return chain.proceed(authorised);
	}
}
