package com.tuacy.rxjavaretrofitlib.optimize;

import android.support.annotation.Nullable;

import com.tuacy.rxjavaretrofitlib.optimize.exception.RetrofitException;
import com.tuacy.rxjavaretrofitlib.optimize.exception.RetrofitExceptionCode;
import com.tuacy.rxjavaretrofitlib.optimize.parse.RetrofitBridgeBase;

import rx.Subscriber;

class RetrofitSubscriber<T> extends Subscriber<String> {

	/**
	 * 用于唯一标识请求
	 */
	private int                   mRequestId;
	/**
	 * 解析数据
	 */
	private RetrofitBridgeBase<T> mBridge;

	RetrofitSubscriber(int requestId, @Nullable RetrofitBridgeBase<T> bridge) {
		mRequestId = requestId;
		mBridge = bridge;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mBridge != null) {
			mBridge.onRequestStart(mRequestId);
		}
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		if (mBridge != null) {
			if (e instanceof RetrofitException) {
				mBridge.onRequestError(mRequestId, (RetrofitException) e);
			} else {
				mBridge.onRequestError(mRequestId, new RetrofitException(e, RetrofitExceptionCode.UNKNOWN_ERROR));
			}
		}
	}

	@Override
	public void onNext(String s) {
		if (mBridge != null) {
			try {
				mBridge.onRequestComplete(mRequestId, mBridge.getRetrofitParser().parse(s));
			} catch (RetrofitException e) {
				e.printStackTrace();
				mBridge.onRequestError(mRequestId, e);
			}
		}
	}
}
