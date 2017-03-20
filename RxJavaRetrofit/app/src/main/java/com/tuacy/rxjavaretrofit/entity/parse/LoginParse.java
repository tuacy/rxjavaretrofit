package com.tuacy.rxjavaretrofit.entity.parse;

import com.tuacy.rxjavaretrofitlib.optimize.exception.RetrofitException;
import com.tuacy.rxjavaretrofitlib.optimize.parse.RetrofitParser;

public class LoginParse implements RetrofitParser<String> {

	@Override
	public String parse(String content) throws RetrofitException {
		return content;
	}
}
