package com.tuacy.rxjavaretrofitlib.optimize;


import retrofit2.Retrofit;
import rx.Observable;

public abstract class RetrofitBaseApi {

	private String mBaseUrl = "http://cloud.pmac.com.cn/IntelligentApp/";

	/**
	 * 连接时间(单位 s)
	 */
	private int mConnectTime = 15;

	/**
	 * 设置参数
	 */
	public abstract Observable<String> getObservable(Retrofit retrofit);

	/**
	 * 用来唯一标识每个请求
	 * @return
	 */
	public abstract int uuid();

	/**
	 * 获取连接时间
	 */
	public int getConnectTime() {
		return mConnectTime;
	}

	/**
	 * 设置连接时间
	 */
	public void setConnectTime(int connectTime) {
		mConnectTime = connectTime;
	}

	/**
	 * 获取base url
	 */
	public String getBaseUrl() {
		return mBaseUrl;
	}

	/**
	 * 设置base url
	 */
	public void setBaseUrl(String baseUrl) {
		mBaseUrl = baseUrl;
	}
}
