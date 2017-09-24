package com.antoniorosario.musicsearch.ui.tracksearch;

import android.support.annotation.NonNull;
import android.util.Log;

import com.antoniorosario.musicsearch.models.ITunesBaseJsonResponse;
import com.antoniorosario.musicsearch.models.Track;
import com.antoniorosario.musicsearch.service.ITunesApi;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class TrackSearchPresenter {

    private static final String LOG_TAG = "TrackSearchPresenter";
    private static final String ITUNES_BASE_URL = "http://itunes.apple.com";

    private TrackSearchView trackSearchView;
    private ITunesApi client;

    public void setView(TrackSearchView trackSearchView) {
        this.trackSearchView = trackSearchView;
    }

    public void executeQuery(String query) {

        if (client == null) {
            setUpRetroFit();
        }
        makeNetworkCall(query);
    }

    private void setUpRetroFit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ITUNES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build())
                .build();

        client = retrofit.create(ITunesApi.class);
    }

    private void makeNetworkCall(String query) {

        Call<ITunesBaseJsonResponse> call = client.executeQuery(query);

        trackSearchView.setRefreshing(true);
        trackSearchView.showSearchingView();
        call.enqueue(new Callback<ITunesBaseJsonResponse>() {
            @Override
            public void onResponse(@NonNull Call<ITunesBaseJsonResponse> call, @NonNull Response<ITunesBaseJsonResponse> response) {
                if (response.isSuccessful()) {
                    List<Track> tracks = response.body().getTracks();
                    Log.d(LOG_TAG, "JSON: " + new GsonBuilder().disableHtmlEscaping()
                            .setPrettyPrinting().create().toJson(response));
                    if (trackSearchView != null) {
                        trackSearchView.setRefreshing(false);
                        trackSearchView.onTracksLoaded(tracks);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ITunesBaseJsonResponse> call, @NonNull Throwable t) {
                trackSearchView.setRefreshing(false);
                trackSearchView.onTracksFailedToLoad();
            }
        });
    }
}
