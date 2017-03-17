package com.tuacy.rxjavaretrofitlib.optimize.exception;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * 重连机制
 */
public class RetryNetworkException implements Func1<Observable<? extends Throwable>, Observable<?>> {

	/*retry次数*/
	private int  mCount         = 3;
	/*延迟*/
	private long mDelay         = 3000;
	/*叠加延迟*/
	private long mIncreaseDelay = 3000;

	public RetryNetworkException() {

	}

	public RetryNetworkException(int count, long delay) {
		this.mCount = count;
		this.mDelay = delay;
	}

	public RetryNetworkException(int count, long delay, long increaseDelay) {
		this.mCount = count;
		this.mDelay = delay;
		this.mIncreaseDelay = increaseDelay;
	}

	@Override
	public Observable<?> call(Observable<? extends Throwable> observable) {
		return observable.zipWith(Observable.range(1, mCount + 1), new Func2<Throwable, Integer, Wrapper>() {
			@Override
			public Wrapper call(Throwable throwable, Integer integer) {
				return new Wrapper(throwable, integer);
			}
		}).flatMap(new Func1<Wrapper, Observable<?>>() {
			@Override
			public Observable<?> call(Wrapper wrapper) {
				if ((wrapper.mThrowable instanceof ConnectException || wrapper.mThrowable instanceof SocketTimeoutException ||
					 wrapper.mThrowable instanceof TimeoutException) && wrapper.mIndex < mCount + 1) { //如果超出重试次数也抛出错误，否则默认是会进入onCompleted
					return Observable.timer(mDelay + (wrapper.mIndex - 1) * mIncreaseDelay, TimeUnit.MILLISECONDS);
				}
				return Observable.error(wrapper.mThrowable);
			}
		});
	}

	private class Wrapper {

		private int       mIndex;
		private Throwable mThrowable;

		Wrapper(Throwable throwable, int index) {
			mIndex = index;
			mThrowable = throwable;
		}
	}
}
