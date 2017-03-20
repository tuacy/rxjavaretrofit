package com.tuacy.rxjavaretrofitlib.optimize;

import android.content.Context;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.tuacy.rxjavaretrofitlib.optimize.cookies.CookieManger;
import com.tuacy.rxjavaretrofitlib.optimize.exception.RetrofitExceptionCode;
import com.tuacy.rxjavaretrofitlib.optimize.exception.RetrofitException;
import com.tuacy.rxjavaretrofitlib.optimize.exception.RetryNetworkException;
import com.tuacy.rxjavaretrofitlib.optimize.interceptor.NoCacheInterceptor;
import com.tuacy.rxjavaretrofitlib.optimize.interceptor.TokenInterceptor;
import com.tuacy.rxjavaretrofitlib.optimize.parse.RetrofitBridgeBase;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class RetrofitClient {

	private static final int CONNECT_TIMEOUT = 20;
	private static final int WRITE_TIMEOUT   = 10;
	private static final int READ_TIMEOUT    = 30;

	/**
	 * 上下文
	 */
	private        Context        mContext      = null;
	/**
	 * 让所有的请求都使用这一个OkHttpClient，便于一些缓存的处理
	 */
	private        OkHttpClient   mOkHttpClient = null;
	/**
	 * 单利模式
	 */
	private static RetrofitClient mInstance     = null;

	/**
	 * 构造函数
	 */
	private RetrofitClient(Context context) {
		mOkHttpClient = new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
												  .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
												  .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
												  .retryOnConnectionFailure(true)//出现错误进行重新连接
												  .cookieJar(new CookieManger())
												  .addNetworkInterceptor(
													  new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
												  .addInterceptor(new NoCacheInterceptor())
												  .addNetworkInterceptor(new NoCacheInterceptor())
												  .addNetworkInterceptor(new TokenInterceptor())
												  //												  .authenticator()
												  .build();
	}

	public static void retrofitInit(Context context) {
		if (mInstance == null) {
			synchronized (RetrofitClient.class) {
				if (mInstance == null) {
					mInstance = new RetrofitClient(context.getApplicationContext());
				}
			}
		}
	}

	/**
	 * 单利模式
	 */
	public static RetrofitClient getInstance() {
		if (mInstance == null) {
			throw new NullPointerException(
				"********** please call the RetrofitClient.retrofitInit(Context context) function in the application");
		}
		return mInstance;
	}

	/**
	 * 异常处理
	 */
	private Func1<Throwable, Observable<String>> mErrorResume = new Func1<Throwable, Observable<String>>() {
		@Override
		public Observable<String> call(Throwable throwable) {
			return Observable.error(transformerException(throwable));
		}
	};

	/**
	 * 统一管理异常
	 */
	private RetrofitException transformerException(Throwable e) {
		RetrofitException apiException = new RetrofitException(e, RetrofitExceptionCode.UNKNOWN_ERROR);
		if (e instanceof HttpException) {
			/**网络异常*/
			apiException.setExceptionCode(RetrofitExceptionCode.HTTP_ERROR);
		} else if (e instanceof ConnectException || e instanceof SocketTimeoutException) {
			/**链接异常*/
			apiException.setExceptionCode(RetrofitExceptionCode.HTTP_ERROR);
		} else if (e instanceof JSONException || e instanceof ParseException) {
			apiException.setExceptionCode(RetrofitExceptionCode.JSON_ERROR);
		} else if (e instanceof UnknownHostException) {
			/**无法解析该域名异常*/
			apiException.setExceptionCode(RetrofitExceptionCode.HOST_ERROR);
		} else {
			/**未知异常*/
			apiException.setExceptionCode(RetrofitExceptionCode.UNKNOWN_ERROR);
		}
		return apiException;
	}

	/**
	 * 请求数据(有内存泄露的处理，一般Android的四大组件会用到RxAppCompatActivity形式表现)
	 *
	 * @param api                  api信息
	 * @param bridge               中间转换，转换成我们需要的数据类型
	 * @param lifecycleTransformer 声明周期管理，防止内存泄露
	 */
	public void doRequest(RetrofitBaseApi api, RetrofitBridgeBase bridge, LifecycleTransformer<String> lifecycleTransformer) {
		//手动创建一个OkHttpClient并设置超时时间缓存等设置

		Retrofit retrofit = new Retrofit.Builder().client(mOkHttpClient)//添加OKHttpClient
												  .addConverterFactory(ScalarsConverterFactory.create())//得到的response转换为string
												  .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//让他支持，Observable
												  .baseUrl(api.getBaseUrl())//设置访问base地址
												  .build();
		/*rx处理*/
		Observable<String> observable = api.getObservable(retrofit).retryWhen(new RetryNetworkException())//失败后的retry配置
										   .onErrorResumeNext(mErrorResume)//异常处理
										   .compose(lifecycleTransformer)//生命周期管理(Activity，Fragment等销毁的时候处理)
										   .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())//http请求线程
										   .observeOn(AndroidSchedulers.mainThread());//回调线程
		/*数据注册*/
		observable.subscribe(new RetrofitSubscriber(api.uuid(), bridge));
	}

	/**
	 * 请求数据
	 *
	 * @param bridge 中间转换，转换成我们需要的数据类型
	 * @param api    api信息
	 */
	public void doRequest(RetrofitBaseApi api, RetrofitBridgeBase bridge) {
		//手动创建一个OkHttpClient并设置超时时间缓存等设置

		Retrofit retrofit = new Retrofit.Builder().client(mOkHttpClient)//添加OKHttpClient
												  .addConverterFactory(ScalarsConverterFactory.create())//得到的response转换为string
												  .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//让他支持，Observable
												  .baseUrl(api.getBaseUrl())//设置访问base地址
												  .build();
		/*rx处理*/
		Observable<String> observable = api.getObservable(retrofit).retryWhen(new RetryNetworkException())//失败后的retry配置
										   .onErrorResumeNext(mErrorResume)//异常处理
										   .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())//http请求线程
										   .observeOn(AndroidSchedulers.mainThread());//回调线程
		/*数据注册*/
		observable.subscribe(new RetrofitSubscriber(api.uuid(), bridge));
	}

}
