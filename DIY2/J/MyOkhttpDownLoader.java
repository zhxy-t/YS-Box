/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tvbox.osc.picasso;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import com.squareup.picasso.Downloader;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Headers;
import okhttp3.HttpUrl;

import android.util.ArrayMap;

/**
 * A {@link Downloader} which uses OkHttp to download images.
 */
public final class MyOkhttpDownLoader implements Downloader {
    private final OkHttpClient mOk;
    
    @VisibleForTesting
    final Call.Factory client;
    private final Cache cache;
    private boolean sharedClient = true;

    /**
     * Create a new downloader that uses the specified OkHttp instance. A response cache will not be
     * automatically configured.
     */
    public MyOkhttpDownLoader(OkHttpClient client) {
        this.client = client;
        this.cache = client.cache();
    }

    /**
     * Create a new downloader that uses the specified {@link Call.Factory} instance.
     */
    public MyOkhttpDownLoader(Call.Factory client) {
        this.client = client;
        this.cache = null;
    }

    
     public static MyOkhttpDownLoader get() {
        return Loader.INSTANCE;
    }

    public MyOkhttpDownLoader() {
        mOk = getBuilder().build();
    }
/*
    private OkHttpClient.Builder getBuilder() {
        return new OkHttpClient.Builder().addInterceptor(CronetInterceptor.newBuilder(new CronetEngine.Builder(App.get()).build()).build()).callTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS).hostnameVerifier(SSLSocketFactoryCompat.hostnameVerifier).sslSocketFactory(new SSLSocketFactoryCompat(), SSLSocketFactoryCompat.trustAllCert);
    }
*/
    private OkHttpClient client() {
        return mOk;
    }

    public static Call newCall(String url) {
        return get().client().newCall(new Request.Builder().url(url).build());
    }

    public static Call newCall(String url, Headers headers) {
        return get().client().newCall(new Request.Builder().url(url).headers(headers).build());
    }

    public static Call newCall(String url, ArrayMap<String, String> params) {
        return get().client().newCall(new Request.Builder().url(buildUrl(url, params)).build());
    }

    /*
    private static HttpUrl buildUrl(String url, ArrayMap<String, String> params) {
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) builder.addQueryParameter(entry.getKey(), entry.getValue());
        return builder.build();
    }
    */
    
    @NonNull
    @Override
    public Response load(@NonNull Request request) throws IOException {
        String url = request.url().toString();

        String refer = null;
        String ua = null;
        String cookie = null;

        //检查链接里面是否有自定义cookie
        String[] cookieUrl = url.split("@Cookie=");
        if (cookieUrl.length > 1) {
            url = cookieUrl[0];
            cookie = cookieUrl[1];
        }

        //检查链接里面是否有自定义UA和referer
        String[] s = url.split("@Referer=");
        if (s.length > 1) {
            if (s[0].contains("@User-Agent=")) {
                refer = s[1];
                url = s[0].split("@User-Agent=")[0];
                ua = s[0].split("@User-Agent=")[1];
            } else if (s[1].contains("@User-Agent=")) {
                refer = s[1].split("@User-Agent=")[0];
                url = s[0];
                ua = s[1].split("@User-Agent=")[1];
            } else {
                refer = s[1];
                url = s[0];
            }
        } else {
            if (url.contains("@Referer=")) {
                url = url.replace("@Referer=", "");
                refer = "";
            }
            if (url.contains("@User-Agent=")) {
                ua = url.split("@User-Agent=")[1];
                url = url.split("@User-Agent=")[0];
            }
        }
        Request.Builder mRequestBuilder = new Request.Builder().url(url);

        if (!TextUtils.isEmpty(cookie)) {
            mRequestBuilder.addHeader("cookie", cookieUrl[1]);
        }
        if (!TextUtils.isEmpty(ua)) {
            if (TextUtils.isEmpty(refer)) {
                mRequestBuilder.addHeader("user-agent", ua);
            } else {
                mRequestBuilder.addHeader("user-agent", ua).addHeader("referer", refer);
            }
        } else {
            if (!TextUtils.isEmpty(refer)) {
                mRequestBuilder.addHeader("referer", refer);
            }
        }
        return client.newCall(mRequestBuilder.build()).execute();
    }

    @Override
    public void shutdown() {
        if (!sharedClient && cache != null) {
            try {
                cache.close();
            } catch (IOException ignored) {
            }
        }
    }
}
