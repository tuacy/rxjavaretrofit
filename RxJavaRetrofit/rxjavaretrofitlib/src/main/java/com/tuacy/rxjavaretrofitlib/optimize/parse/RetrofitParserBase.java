package com.tuacy.rxjavaretrofitlib.optimize.parse;

public abstract class RetrofitParserBase<T> {

	/**
	 * 获取解析对象
	 */
	protected abstract RetrofitParser<T> getRetrofitParser();

}
