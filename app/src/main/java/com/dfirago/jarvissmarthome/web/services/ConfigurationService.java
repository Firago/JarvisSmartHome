package com.dfirago.jarvissmarthome.web.services;

import com.dfirago.jarvissmarthome.web.model.NetworkSelectRequest;
import com.dfirago.jarvissmarthome.web.model.NetworksResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by dmfi on 03/01/2017.
 */
public interface ConfigurationService {

    @GET("networks")
    Call<NetworksResponse> getNetworks();

    @POST("configuration/network")
    Call<Void> selectNetwork(@Body NetworkSelectRequest request);
}
