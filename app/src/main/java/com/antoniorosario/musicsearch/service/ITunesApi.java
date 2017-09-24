package com.antoniorosario.musicsearch.service;

import com.antoniorosario.musicsearch.models.ITunesBaseJsonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ITunesApi {

    @GET("/search")
    Call<ITunesBaseJsonResponse> executeQuery(@Query("term") String query);
}
