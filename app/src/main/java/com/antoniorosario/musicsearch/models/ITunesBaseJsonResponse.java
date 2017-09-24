package com.antoniorosario.musicsearch.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ITunesBaseJsonResponse {

    @SerializedName("resultCount")
    @Expose
    private Integer trackCount;
    @SerializedName("results")
    @Expose
    private List<Track> tracks = null;

    public Integer getResultCount() {
        return trackCount;
    }

    public void setResultCount(Integer trackCount) {
        this.trackCount = trackCount;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}