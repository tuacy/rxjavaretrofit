package com.tuacy.rxjavaretrofitlib.optimize;

import rx.Subscriber;

public class RetrofitSubscriber extends Subscriber<String> {

	private int mRequestId;

	public RetrofitSubscriber(int requestId) {
		mRequestId = requestId;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {

	}

	@Override
	public void onNext(String s) {
	}
}
