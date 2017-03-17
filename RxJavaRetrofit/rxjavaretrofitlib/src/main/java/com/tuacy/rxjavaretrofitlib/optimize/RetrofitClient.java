package com.tuacy.rxjavaretrofitlib.optimize;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.tuacy.rxjavaretrofitlib.optimize.exception.RetrofitExceptionCode;
import com.tuacy.rxjavaretrofitlib.optimize.exception.RetrofitException;
import com.tuacy.rxjavaretrofitlib.optimize.exception.RetryNetworkException;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class RetrofitClient {

	private static final int            DEFAULT_TIMEOUT = 20;
	/**
	 * 让所有的请求都使用这一个OkHttpClient，便于一些缓存的处理
	 */
	private              OkHttpClient   mOkHttpClient   = null;
	/**
	 * 单利模式
	 */
	private static       RetrofitClient mInstance       = null;

	/**
	 * 构造函数
	 */
	private RetrofitClient() {
		mOkHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(
			new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
												  .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
												  .retryOnConnectionFailure(true)
												  .build();
	}

	/**
	 * 单利模式
	 */
	public static RetrofitClient getInstance() {
		if (mInstance == null) {
			synchronized (RetrofitClient.class) {
				if (mInstance == null) {
					mInstance = new RetrofitClient();
				}
			}
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
		RetrofitException apiException = new RetrofitException(e, RetrofitExceptionCode.HOST_ERROR);
		//TODO:
		return apiException;
	}

	/**
	 * 请求数据
	 */
	public void doRequest(RetrofitBaseApi api, LifecycleTransformer<String> lifecycleTransformer) {
		//手动创建一个OkHttpClient并设置超时时间缓存等设置

		Retrofit retrofit = new Retrofit.Builder().client(mOkHttpClient)//添加OKHttpClient
												  .addConverterFactory(ScalarsConverterFactory.create())//得到的response转换为string
												  .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//让他支持，Observable
												  .baseUrl(api.getBaseUrl())//设置访问base地址
												  .build();
		/*rx处理*/
		Observable<String> observable = api.getObservable(retrofit).retryWhen(new RetryNetworkException())//失败后的retry配置
										   .onErrorResumeNext(mErrorResume)//异常处理
										   .compose(lifecycleTransformer)//生命周期管理
										   .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())//http请求线程
										   .observeOn(AndroidSchedulers.mainThread());//回调线程
		/*数据注册*/
		observable.subscribe(new RetrofitSubscriber(api.uuid()));
	}

	public void doRequest(RetrofitBaseApi api) {
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
		observable.subscribe(new RetrofitSubscriber(api.uuid()));
	}

}
