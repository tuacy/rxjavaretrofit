package com.tuacy.rxjavaretrofitlib.optimize.interceptor;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {

	private Context mContext;

	public CacheInterceptor(Context context) {
		this.mContext = context;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		if (!isConnected(mContext)) {//没网强制从缓存读取(必须得写，不然断网状态下，退出应用，或者等待一分钟后，就获取不到缓存）
			request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
		}
		Response response = chain.proceed(request);
		Response responseLatest;
		if (isConnected(mContext)) {
			int maxAge = 60; //有网失效一分钟
			responseLatest = response.newBuilder()
									 .removeHeader("Pragma")
									 .removeHeader("Cache-Control")
									 .header("Cache-Control", "public, max-age=" + maxAge)
									 .build();
		} else {
			int maxStale = 60 * 60 * 6; // 没网失效6小时
			responseLatest = response.newBuilder()
									 .removeHeader("Pragma")
									 .removeHeader("Cache-Control")
									 .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
									 .build();
		}
		return responseLatest;
	}

	/**
	 * 判断网络是否连接
	 *
	 * @param context 上下文
	 */
	private boolean isConnected(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != connectivity) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}
}
