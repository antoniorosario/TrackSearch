package com.antoniorosario.musicsearch.service;

import com.antoniorosario.musicsearch.models.MusixMatchBaseJsonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusixMatchApi {

    @GET("/ws/1.1/matcher.lyrics.get")
    Call<MusixMatchBaseJsonResponse> getLyrics(
            @Query("apikey") String apiKey,
            @Query("q_artist") String artist,
            @Query("q_track") String track
    );
}
