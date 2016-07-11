package com.softdesig.devintensive.data.network;

import com.softdesig.devintensive.data.network.interceptors.HeaderInterceptor;
import com.softdesig.devintensive.utils.AppConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
    private static Retrofit.Builder sBuilder =
            new Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor((new HeaderInterceptor()));
        httpClientBuilder.addInterceptor(loggingInterceptor);
        Retrofit retrofit = sBuilder
                .client(httpClientBuilder.build())
                .build();
        return retrofit.create(serviceClass);
    }
}
