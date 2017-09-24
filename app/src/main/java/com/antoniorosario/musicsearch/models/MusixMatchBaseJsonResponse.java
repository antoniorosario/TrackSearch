package com.antoniorosario.musicsearch.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MusixMatchBaseJsonResponse {

    @SerializedName("message")
    @Expose
    private MusixMatchMessage musixMatchMessage;

    public MusixMatchMessage getMusixMatchMessage() {
        return musixMatchMessage;
    }

    public void setMusixMatchMessage(MusixMatchMessage musixMatchMessage) {
        this.musixMatchMessage = musixMatchMessage;
    }

}