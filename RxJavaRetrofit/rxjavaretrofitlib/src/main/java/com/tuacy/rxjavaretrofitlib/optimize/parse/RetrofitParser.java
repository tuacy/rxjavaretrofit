package com.tuacy.rxjavaretrofitlib.optimize.parse;


import com.tuacy.rxjavaretrofitlib.optimize.exception.RetrofitException;

public interface RetrofitParser<T> {

	T parse(String content) throws RetrofitException;

}
