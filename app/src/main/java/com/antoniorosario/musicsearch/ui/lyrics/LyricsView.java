package com.antoniorosario.musicsearch.ui.lyrics;

interface LyricsView {
    void onLyricsLoaded(String lyrics);

    void onLyricsFailedToLoad();
}
