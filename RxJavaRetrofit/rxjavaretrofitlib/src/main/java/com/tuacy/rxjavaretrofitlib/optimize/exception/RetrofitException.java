package com.tuacy.rxjavaretrofitlib.optimize.exception;


public class RetrofitException extends Exception {

	/**
	 * 用来标示不同的异常
	 */
	private int mExceptionCode;

	public RetrofitException(Throwable throwable, @RetrofitExceptionCode.CodeEp int exceptionCode) {
		super(throwable);
		mExceptionCode = exceptionCode;
	}

	public int getExceptionCode() {
		return mExceptionCode;
	}

	public void setExceptionCode(@RetrofitExceptionCode.CodeEp int exceptionCode) {
		mExceptionCode = exceptionCode;
	}

}
