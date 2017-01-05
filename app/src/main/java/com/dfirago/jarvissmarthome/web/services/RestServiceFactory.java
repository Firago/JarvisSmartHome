package com.dfirago.jarvissmarthome.web.services;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by dmfi on 04/01/2017.
 */

public class RestServiceFactory {

    private static final String API_BASE_URL = "http://192.168.4.1";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    private RestServiceFactory() {

    }
}
