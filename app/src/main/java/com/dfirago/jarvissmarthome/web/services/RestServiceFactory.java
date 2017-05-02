package com.dfirago.jarvissmarthome.web.services;

import com.dfirago.jarvissmarthome.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by dmfi on 04/01/2017.
 */

public class RestServiceFactory {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.HUB_BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .client(client)
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    private RestServiceFactory() {

    }
}
