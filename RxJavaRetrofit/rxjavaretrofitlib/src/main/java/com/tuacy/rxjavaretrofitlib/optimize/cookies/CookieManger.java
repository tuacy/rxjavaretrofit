package com.tuacy.rxjavaretrofitlib.optimize.cookies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Cookie 管理
 */
public class CookieManger implements CookieJar {

	private final HashMap<HttpUrl, List<Cookie>> mCookieStore = new HashMap<>();

	@Override
	public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
		mCookieStore.put(url, cookies);
	}

	@Override
	public List<Cookie> loadForRequest(HttpUrl url) {
		List<Cookie> cookies = mCookieStore.get(url);
		return cookies != null ? cookies : new ArrayList<Cookie>();
	}
}