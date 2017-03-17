package com.tuacy.rxjavaretrofitlib.optimize.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RetrofitExceptionCode {

	/**
	 * 网络错误
	 */
	public static final int NETWORK_ERROR = 0x1;
	/**
	 * http 错误
	 */
	public static final int HTTP_ERROR    = 0x2;
	/**
	 * json错误
	 */
	public static final int JSON_ERROR    = 0x3;
	/**
	 * 未知错误
	 */
	public static final int UNKNOWN_ERROR = 0x4;
	/**
	 * 运行时异常-包含自定义异常
	 */
	public static final int RUNTIME_ERROR = 0x5;
	/**
	 * 无法解析该域名
	 */
	public static final int HOST_ERROR    = 0x6;


	@IntDef({NETWORK_ERROR,
			 HTTP_ERROR,
			 RUNTIME_ERROR,
			 UNKNOWN_ERROR,
			 JSON_ERROR,
			 HOST_ERROR})
	@Retention(RetentionPolicy.SOURCE)

	public @interface CodeEp {

	}

}
