package com.tuacy.rxjavaretrofitlib.optimize.parse;

import com.tuacy.rxjavaretrofitlib.optimize.exception.RetrofitException;

/**
 * 中间数据转换的一个帮助类
 *
 * @param <T> 我们要转换到的对象类型
 */
public abstract class RetrofitBridgeBase<T> {

	/**
	 * 获取解析对象，转换成我们制定的格式信息
	 */
	public abstract RetrofitParser<T> getRetrofitParser();

	/**
	 * 开始请求网络数据
	 *
	 * @param requestId request uuid
	 */
	public abstract void onRequestStart(int requestId);

	/**
	 * 请求数据成功
	 *
	 * @param requestId request uuid
	 * @param result    请求结果信息
	 */
	public abstract void onRequestComplete(int requestId, T result);

	/**
	 * 请求数据失败
	 *
	 * @param requestId request uuid
	 * @param exception 异常信息
	 */
	public abstract void onRequestError(int requestId, RetrofitException exception);

}
