package com.antoniorosario.musicsearch.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MusixMatchBody {

    @SerializedName("lyrics")
    @Expose
    private Lyrics lyrics;

    public Lyrics getLyrics() {
        return lyrics;
    }

    public void setLyrics(Lyrics lyrics) {
        this.lyrics = lyrics;
    }
}
