package com.softdesig.devintensive.data.network.interceptors;

import com.softdesig.devintensive.data.managers.DataManager;
import com.softdesig.devintensive.data.managers.PreferenceManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Admin on 12.07.2016.
 */
public class HeaderInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        PreferenceManager preferenceManager = DataManager.getInstance().getPreferenceManager();
        Request original = chain.request();
        Request.Builder reqBuilder = original.newBuilder()
                .header("X-Access-Tocken", preferenceManager.getAuthTocken())
                .header("Request-User-Id", preferenceManager.getUserId())
                .header("User-Agent", "DevIntensiveApp");
        Request request = reqBuilder.build();
        return chain.proceed(request);
    }
}
