package com.antoniorosario.musicsearch.ui.tracksearch;

import com.antoniorosario.musicsearch.models.Track;

import java.util.List;


interface TrackSearchView {
    void onTracksLoaded(List<Track> tracks);

    void onTracksFailedToLoad();

    void setRefreshing(boolean isRefreshing);

    void showDeviceIsOfflineView();

    void showOfflineSearchView();

    void showActiveSearch();

    void showSearchingView();
}
