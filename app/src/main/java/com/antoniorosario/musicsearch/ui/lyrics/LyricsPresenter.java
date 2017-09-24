package com.antoniorosario.musicsearch.ui.lyrics;


import android.support.annotation.NonNull;
import android.util.Log;

import com.antoniorosario.musicsearch.models.MusixMatchBaseJsonResponse;
import com.antoniorosario.musicsearch.service.MusixMatchApi;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class LyricsPresenter {
    private static final String LOG_TAG = "LyricsPresenter";
    private static final String MUSIX_MATCH_BASE_URL = "http://api.musixmatch.com";
    private static final String API_KEY = "5bd6b052580135f671c52c83146b77cc";

    private LyricsView lyricsview;
    private MusixMatchApi client;

    public void setView(LyricsView lyricsview) {
        this.lyricsview = lyricsview;
    }

    public void getLyrics(String artist, String track) {

        if (client == null) {
            setUpRetroFit();
        }
        makeNetworkCall(artist, track);
    }

    private void setUpRetroFit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(MUSIX_MATCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build())
                .build();

        client = retrofit.create(MusixMatchApi.class);
    }

    private void makeNetworkCall(String artist, String track) {

        Call<MusixMatchBaseJsonResponse> call = client.getLyrics(API_KEY, artist, track);

        call.enqueue(new Callback<MusixMatchBaseJsonResponse>() {
            @Override
            public void onResponse(@NonNull Call<MusixMatchBaseJsonResponse> call, @NonNull Response<MusixMatchBaseJsonResponse> response) {
                if (response.isSuccessful()) {

                    String lyrics = response.body().getMusixMatchMessage().getMusixMatchBody().getLyrics().getLyricsBody();
                    Log.d(LOG_TAG, "JSON: " + new GsonBuilder().disableHtmlEscaping()
                            .setPrettyPrinting().create().toJson(response));
                    if (lyricsview != null) {
                        lyricsview.onLyricsLoaded(lyrics);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MusixMatchBaseJsonResponse> call, @NonNull Throwable t) {
                lyricsview.onLyricsFailedToLoad();
                t.printStackTrace();
            }
        });
    }

}
